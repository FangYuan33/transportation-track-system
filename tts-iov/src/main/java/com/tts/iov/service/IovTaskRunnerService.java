package com.tts.iov.service;

import com.tts.base.domain.BaseNodeHeartbeat;
import com.tts.base.service.BaseNodeHeartbeatService;
import com.tts.base.zookeeper.TtsZkNode;
import com.tts.common.context.TtsContext;
import com.tts.iov.domain.IovConfig;
import com.tts.iov.domain.IovSubscribeTask;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import static com.tts.iov.enums.IovSubscribeTaskStateEnums.*;

/**
 * 节点任务执行服务
 * 主要职责包括:
 * 1. leader 节点
 * 1.1 分配 ALLOCATING 的任务
 * 1.2 检查任务的心跳,记录心跳超时的任务,并将其重新设置为ALLOCATING
 * <p>
 * 2. 普通节点: 查询处于  ALLOCATED 状态的任务, 启动任务线程,将任务设置为 RUNNING 状态
 *
 * @author FangYuan
 * @since 2023-01-07 19:50:33
 */
@Slf4j
@Order
@Service
public class IovTaskRunnerService {

    @Autowired
    private TtsZkNode node;
    @Autowired
    private IovSubscribeTaskService iovSubscribeTaskService;
    @Autowired
    private BaseNodeHeartbeatService nodeHeartbeatService;
    @Autowired
    private IovConfigService iovConfigService;

    @Value("${zookeeper.node.taskInterval}")
    private long TASK_INTERVAL;

    /**
     * 启动节点任务执行
     */
    public void start() {
        // 初始化任务线程
        Thread taskThread = new Thread(this::initialTask);
        taskThread.setDaemon(true);
        taskThread.setName("Node Task Thread");

        // 启动
        taskThread.start();
    }

    /**
     * 初始化任务
     */
    private void initialTask() {
        while (true) {
            // leader节点负责检查心跳和分配任务
            if (node.getIsLeader().get()) {
                processLeaderTask();
            } else {
                // 普通节点将被分配的任务设置为运行状态，启动任务线程
                runningTask();
            }

            try {
                Thread.sleep(TASK_INTERVAL);
            } catch (Exception e) {
                log.error("Node Task sleep error", e);
            }
        }
    }

    /**
     * leader节点的任务 心跳检测和分配任务
     */
    private void processLeaderTask() {
        try {
            // 心跳检测
            checkHeartbeat();
            // 分配任务
            allocatingTask();
        } catch (Exception e) {
            log.error("Process Leader Task Error", e);
        }
    }

    /**
     * 检查心跳，将超时的节点上的未执行完的任务修改为待分配状态
     */
    private void checkHeartbeat() {
        // 查询当前有任务运行的节点信息
        List<IovSubscribeTask> runningTasks = iovSubscribeTaskService.listRunningTask();

        if (!runningTasks.isEmpty()) {
            // 获取所有正在运行任务上挂的服务
            List<String> serverNames = runningTasks.stream().map(IovSubscribeTask::getServerName).collect(Collectors.toList());
            // 获取所有服务的心跳时间
            List<BaseNodeHeartbeat> nodeHeartbeats = nodeHeartbeatService.listByServerNames(serverNames);

            // 将没有超时的服务节点移除
            for (BaseNodeHeartbeat nodeHeartbeat : nodeHeartbeats) {
                if (!nodeHeartbeatService.isTimeOut(nodeHeartbeat)) {
                    serverNames.remove(nodeHeartbeat.getServerName());
                }
            }

            // 剩下心跳超时的节点，将其上的未完成的任务全部标记为待分配
            List<IovSubscribeTask> timeOutTask = iovSubscribeTaskService.listByServerNames(serverNames);
            for (IovSubscribeTask task : timeOutTask) {
                if (ALLOCATED.getValue().equals(task.getState()) || RUNNING.getValue().equals(task.getState())) {
                    iovSubscribeTaskService.allocatingTask(task);
                }
            }
        }
    }

    /**
     * 分配处于allocating 状态的任务
     * todo: 后面需要 优化分配算法 为负载均衡的
     */
    private void allocatingTask() {
        // 获取目前可用的serverIp列表
        List<String> serverNameList = node.getServerNameList();
        Random random = new Random();

        // 拿出来所有待分配的任务
        List<IovSubscribeTask> allocatingTasks = iovSubscribeTaskService.listAllocatingTask();

        for (IovSubscribeTask task : allocatingTasks) {
            // 将这个任务分配给随机一个服务
            String serverName = serverNameList.get(random.nextInt(serverNameList.size()));
            iovSubscribeTaskService.allocatedTask(task, serverName);
        }
    }

    /**
     * 将分配给当前节点的任务改成运行中
     */
    private void runningTask() {
        try {
            List<IovSubscribeTask> allocatedTasks = iovSubscribeTaskService.listCurrentNodeAllocatedTask();

            for (IovSubscribeTask allocatedTask : allocatedTasks) {
                IovConfig iovConfig = iovConfigService.getById(allocatedTask.getIovConfigId());
                // 启动任务

                iovSubscribeTaskService.runningTask(allocatedTask);
            }
        } catch (Exception e) {
            log.error("Follower " + TtsContext.getNodeServerName() + " running task error", e);
        }
    }
}
