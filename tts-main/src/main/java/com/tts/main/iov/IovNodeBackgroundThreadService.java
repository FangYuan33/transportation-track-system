package com.tts.main.iov;

import com.tts.base.constant.IovTaskStateEnum;
import com.tts.base.constant.TtsConstant;
import com.tts.base.domain.BaseNodeHeartbeat;
import com.tts.base.service.BaseNodeHeartbeatService;
import com.tts.base.zk.BaseWorkServer;
import com.tts.framework.common.constant.IovTaskEventTypeEnum;
import com.tts.framework.common.constant.IovTaskOperTypeEnum;
import com.tts.framework.common.utils.IpUtils;
import com.tts.main.domain.TtsIovConfig;
import com.tts.main.domain.TtsIovSubscribeTask;
import com.tts.main.service.CoordinatePointService;
import com.tts.main.service.TtsIovConfigService;
import com.tts.main.service.TtsIovSubscribeTaskService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Random;

/**
 * iov的leader 节点周期性运行的任务
 * 主要职责包括:
 * 1. leader 节点
 * 1.1 分配 ALLOCATING 的任务
 * 1.2 检查任务的心跳,记录心跳超时的任务,并将其重新设置为ALLOCATING
 * <p>
 * 2. 普通节点: 查询处于  ALLOCATED 状态的任务, 启动任务线程,将任务设置为 RUNNING 状态
 *
 * @author FangYuan
 * @since 2022-12-22 14:35:31
 */
@Slf4j
@Service
public class IovNodeBackgroundThreadService {

    @Autowired
    private TtsIovConfigService ttsIovConfigService;

    @Autowired
    private TtsIovSubscribeTaskService ttsIovSubscribeTaskService;

    @Autowired
    private BaseNodeHeartbeatService baseNodeHeartbeatService;

    @Autowired
    private CoordinatePointService coordinatePointService;

    // 内部持有的线程句柄,由于 servcie 是单例模式,所以实际上就这一个线程
    private Thread innerThread;

    // 本机的ip
    private String localHostIp;

    /**
     * 这个方法只会在 application初始化的时候被调用一次
     */
    public synchronized void initAndStart() {

        //初始化线程
        this.init();

        //启动线程
        this.start();
    }

    /**
     * 1.分配 ALLOCATING 的任务
     * 2.检查任务的心跳,记录心跳超时的任务,并将其重新设置为ALLOCATING
     */
    private void init() {
        localHostIp = IpUtils.getHostIp();

        innerThread = new Thread(() -> {
            log.error("###### IOV  分配任务 线程启动");

            while (true) {

                /**
                 * leader 节点执行的内容
                 */
                try {
                    //判断当前节点是否 leader,leader 节点才执行
                    if (BaseWorkServer.isLeader()) {

                        //检查节点的心跳记录, ,将该节点上的任务重新设置为 allocating
                        checkNodeTimeout();
                        //分配处于 ALLOCATING 阶段的任务
                        allocatingTask();
                    }
                } catch (Exception e) {
                    log.error("IovNodeBackgroundThreadService leader  running  error", e);
                }

                /**
                 * 普通节点执行的内容
                 */
                try {
                    //搜索分配给当前机器的ALLOCATE 状态的任务,启动任务,将任务设置为 RUNNING 状态
                    runningTask();
                } catch (Exception e) {
                    log.error("IovNodeBackgroundThreadService node  running  error", e);
                }

                try {
                    //搜索分配给当前机器的 STOPPING 状态的任务,并且当前进程中已经没有其任务运行时实例,将任务设置为 STOPPED 状态
                    stoppedTask();
                } catch (Exception e) {
                    log.error("IovNodeBackgroundThreadService node  stopped  error", e);
                }

                try {
                    //搜索分配给当前机器的 RUNNING  状态的任务,并且当前进程中已经没有其任务运行时实例,将任务 重新设置为 ALLOCATING  状态
                    reAllocatingTask();
                } catch (Exception e) {
                    log.error("IovNodeBackgroundThreadService node  stopped  error", e);
                }

                //休息10秒钟之后重试
                // 每次轮询后 间隔  10分钟
                try {
                    Thread.sleep(TtsConstant.PERIODIC_INTERVAL_TIME);
                } catch (InterruptedException e) {
                    log.error("IovNodeBackgroundThreadService     空闲等待 异常 !!! ", e);
                }

            }

        });
    }


    /**
     * 搜索分配给当前节点的 ALLOCATED 状态的任务,然后启动任务线程进行运行
     * 并将任务设置为 RUNNING 状态
     */
    private void runningTask() throws Exception {

        //搜索分配给本机的 处于ALLOCATED 状态的任务
        List<TtsIovSubscribeTask> taskList = ttsIovSubscribeTaskService.queryIovSubscribeTaskInAllocatedState(localHostIp);

        for (TtsIovSubscribeTask ttsIovSubscribeTask : taskList) {
            TtsIovConfig ttsIovConfig = this.ttsIovConfigService.getById(ttsIovSubscribeTask.getIovConfigId());

            //将任务状态设置为 RUNNING ,这个是机器后台线程自动刷新的,所以 操作类型为 自动
            this.ttsIovSubscribeTaskService.runningTask(ttsIovSubscribeTask, IovTaskOperTypeEnum.AUTO);
            //启动任务
            IovSubscribeTaskRuntime.start(ttsIovConfig, ttsIovSubscribeTask, ttsIovSubscribeTaskService, coordinatePointService);

            log.info("iov task [" + ttsIovSubscribeTask.getId() + "] 启动成功!");
        }

    }

    /**
     * 搜索分配给当前机器的 STOPPING 状态的任务,并且当前进程中已经没有其任务运行时实例,将任务设置为 STOPPED 状态
     */
    private void stoppedTask() {

        //搜索分配给本机的 处于ALLOCATED 状态的任务
        List<TtsIovSubscribeTask> taskList = ttsIovSubscribeTaskService.queryIovSubscribeTaskInStoppingState(localHostIp);

        for (TtsIovSubscribeTask task : taskList) {
            if (IovSubscribeTaskRuntime.getTaskRuntimeMap().get(task.getId()) == null) {
                this.ttsIovSubscribeTaskService.stoppedSubscribeIovTask(task, IovTaskEventTypeEnum.ABNORMAL);
            }
        }

    }

    /**
     * 搜索分配给当前机器的 RUNNING 状态的任务,并且当前进程中已经没有其任务运行时实例,将任务设置为 ALLOCATING 状态
     */
    private void reAllocatingTask() {
        //搜索分配给本机的 处于ALLOCATED 状态的任务
        List<TtsIovSubscribeTask> taskList = ttsIovSubscribeTaskService.queryIovSubscribeTaskInRunningState(localHostIp);

        for (TtsIovSubscribeTask task : taskList) {
            if (IovSubscribeTaskRuntime.getTaskRuntimeMap().get(task.getId()) == null) {
                this.ttsIovSubscribeTaskService.reAllocatingTask(task);
            }
        }
    }


    /**
     * 检查心跳超时的节点
     */
    @Transactional(rollbackFor = Exception.class)
    void checkNodeTimeout() {

        //1. 查询当前有任务运行的节点信息,如果没有运行中的任务则直接返回
        List<String> serverIpList = ttsIovSubscribeTaskService.queryServerIpInRunningState();

        //2.查询相关节点的最新心跳时间
        List<BaseNodeHeartbeat> baseNodeHeartbeatList = baseNodeHeartbeatService.queryInServerList(serverIpList);

        //3.判断节点是否超时
        for (BaseNodeHeartbeat baseNodeHeartbeat : baseNodeHeartbeatList) {

            //如果 baseNodeHeartbeat 不为空 且 心跳不超时,则移除 server ip
            if (baseNodeHeartbeat != null
                    && !baseNodeHeartbeatService.isTimeout(baseNodeHeartbeat)) {

                serverIpList.remove(baseNodeHeartbeat.getServerIp());
            }

        }

        //目前serverIp里面的是 认为有运行任务但是实际心跳超时的 服务器节点
        //4 此时将其上的任务都设置为 allocating 且 记录任务状态变化记录
        for (String serverIp : serverIpList) {
            //通过 serverIp 查询任务列表
            List<TtsIovSubscribeTask> taskList = ttsIovSubscribeTaskService.queryIovSubscribeTaskByServerIp(serverIp);

            //循环重新启动任务
            for (TtsIovSubscribeTask ttsIovSubscribeTask : taskList) {
                //在执行前再确认下任务的状态是不是仍然为 已分配或者运行中, 如果不是的话则 直接 continue
                TtsIovSubscribeTask ttsIovSubscribeTaskNew = ttsIovSubscribeTaskService.getIovSubscribeTaskById(ttsIovSubscribeTask.getId());
                if (ttsIovSubscribeTaskNew != null
                        && (IovTaskStateEnum.ALLOCATED.equals(ttsIovSubscribeTaskNew.getState())
                        || IovTaskStateEnum.RUNNING.equals(ttsIovSubscribeTaskNew.getState()))) {
                    ttsIovSubscribeTaskService.startTaskInner(ttsIovSubscribeTask, "节点[" + serverIp + "] 心跳超时, 重启任务", IovTaskEventTypeEnum.ABNORMAL, IovTaskOperTypeEnum.AUTO);
                }
            }
        }

    }

    /**
     * 分配处于allocating 状态的任务
     * todo: 后面需要 优化分配算法 为负载均衡的
     */
    private void allocatingTask() {

        //获取目前可用的serverIp列表
        List<String> serverIpList = BaseWorkServer.getServerIpList();

        Random random = new Random();

        //搜索处于 allocating 阶段的 任务列表
        List<TtsIovSubscribeTask> taskList = ttsIovSubscribeTaskService.queryIovSubscribeTaskInAllocatingState();

        for (TtsIovSubscribeTask ttsIovSubscribeTask : taskList) {
            //将这个任务分配给随机一个ip
            String serverIp = serverIpList.get(random.nextInt(serverIpList.size()));
            ttsIovSubscribeTaskService.allocatedTask(ttsIovSubscribeTask, serverIp, IovTaskOperTypeEnum.AUTO);
        }

    }

    /**
     * 启动任务
     */
    private void start() {
        innerThread.setDaemon(true);
        innerThread.setName("IovLeaderBackgroundThreadService");
        innerThread.start();
    }

}
