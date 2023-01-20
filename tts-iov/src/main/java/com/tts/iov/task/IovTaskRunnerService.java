package com.tts.iov.task;

import com.alibaba.fastjson.JSONObject;
import com.tts.base.domain.BaseNodeHeartbeat;
import com.tts.base.service.BaseNodeHeartbeatService;
import com.tts.base.zookeeper.TtsZkNode;
import com.tts.common.context.TtsContext;
import com.tts.common.utils.spring.BeanUtils;
import com.tts.gps.dto.GpsCoordinatePointResultDto;
import com.tts.gps.dto.GpsVehicleQueryDto;
import com.tts.gps.enums.IovTypeEnums;
import com.tts.iov.domain.IovSubscribeTask;
import com.tts.iov.domain.IovSubscribeTaskVehicle;
import com.tts.iov.domain.IovTrackPoint;
import com.tts.gps.GpsService;
import com.tts.iov.service.IovSubscribeTaskService;
import com.tts.iov.service.IovSubscribeTaskVehicleService;
import com.tts.iov.service.IovTrackPointService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
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
    private IovSubscribeTaskVehicleService iovSubscribeTaskVehicleService;
    @Autowired
    private BaseNodeHeartbeatService nodeHeartbeatService;
    @Autowired
    private GpsService gpsService;
    @Autowired
    private IovTrackPointService iovTrackPointService;

    @Value("${zookeeper.node.taskInterval}")
    private long TASK_INTERVAL;
    @Value("${zookeeper.node.followerNodeTaskInterval}")
    private long FOLLOWER_NODE_TASK_INTERVAL;
    @Value("${zookeeper.node.trackInterval}")
    private long TRACK_INTERVAL;

    /**
     * 普通节点任务线程
     */
    private Thread followerNodeThread = null;
    /**
     * 普通节点任务的起止控制标志位
     */
    private volatile boolean followerNodeThreadFlag = false;

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
     * 初始化节点任务
     */
    private void initialTask() {
        while (true) {
            // leader节点负责检查心跳和分配任务
            if (node.getIsLeader().get()) {
                // 若之前为子节点，则需要把该子任务线程关掉
                closeFollowerTaskThread();
                // 处理leader节点的任务，心跳检测和分配任务
                processLeaderTask();
            } else {
                // 普通节点将被分配的任务设置为运行状态，并启动任务线程执行
                runningTask();
            }

            try {
                Thread.sleep(TASK_INTERVAL * 1000);
            } catch (Exception e) {
                log.error("************* Node Task sleep error *************", e);
            }
        }
    }

    /**
     * 如果之前是普通节点，但是现在成了leader节点，则需要关闭普通节点任务线程
     */
    private void closeFollowerTaskThread() {
        try {
            if (followerNodeThread != null && followerNodeThread.isAlive()) {
                followerNodeThreadFlag = false;
                followerNodeThread.stop();

                followerNodeThread = null;
            }
        } catch (Exception e) {
            log.error("Leader Node Close Itself Follower Task Thread Error", e);
        }
    }

    /**
     * leader节点的任务 心跳检测和分配任务
     */
    private void processLeaderTask() {
        try {
            // 心跳检测
            checkHeartbeat();
            // 将自己的任务改成待分配
            transferLeaderTask();
            // 分配任务
            allocatingTask();
        } catch (Exception e) {
            log.error("************* Process Leader Task Error *************", e);
        }
    }

    /**
     * 检查心跳，将超时的节点上的未执行完的任务修改为待分配状态
     */
    private void checkHeartbeat() {
        // 查询当前有任务运行的节点信息
        List<IovSubscribeTask> runningTasks = iovSubscribeTaskService.listAllocatedAndRunningTask();

        if (CollectionUtils.isNotEmpty(runningTasks)) {
            // 获取所有正在运行任务上挂的服务
            List<String> serverNames = runningTasks.stream().map(IovSubscribeTask::getServerName).collect(Collectors.toList());
            log.info("Service {} Has Task", JSONObject.toJSONString(serverNames));
            // 获取所有服务的心跳时间
            List<BaseNodeHeartbeat> nodeHeartbeats = nodeHeartbeatService.listByServerNames(serverNames);

            // 将没有超时的服务节点移除
            for (BaseNodeHeartbeat nodeHeartbeat : nodeHeartbeats) {
                if (!nodeHeartbeatService.isTimeOut(nodeHeartbeat)) {
                    log.info("Service {} 's Heartbeat Is Not!!!!! Time Out", JSONObject.toJSONString(serverNames));
                    serverNames.remove(nodeHeartbeat.getServerName());
                }
            }

            // 剩下心跳超时的节点，将其上的未完成的任务全部标记为待分配
            if (CollectionUtils.isNotEmpty(serverNames)) {
                log.info("Service {} 's Heartbeat Is Time Out!!!!!", JSONObject.toJSONString(serverNames));
                List<IovSubscribeTask> timeOutTask = iovSubscribeTaskService.listByServerNames(serverNames);
                for (IovSubscribeTask task : timeOutTask) {
                    if (ALLOCATED.getValue().equals(task.getState()) || RUNNING.getValue().equals(task.getState())) {
                        iovSubscribeTaskService.allocatingTask(task);
                        log.info("Task {} Is Time Out, Wait To Allocating", JSONObject.toJSONString(task));
                    }
                }
            }
        }
    }

    /**
     * 若有之前的leader宕机，新晋的leader需要把手头的任务转换成待分配状态
     * 供其他follower节点执行
     */
    private void transferLeaderTask() {
        List<IovSubscribeTask> taskList = iovSubscribeTaskService.listCurrentNodeAllocatedAndRunningTask();

        for (IovSubscribeTask task : taskList) {
            iovSubscribeTaskService.allocatingTask(task);
        }
    }

    /**
     * 分配处于allocating 状态的任务
     * todo: 后面需要 优化分配算法 为负载均衡的
     */
    private void allocatingTask() {
        // 获取目前可用的serverIp列表
        List<String> serverNameList = node.getServerNameList();

        // 有子节点才进行任务分配
        if (CollectionUtils.isNotEmpty(serverNameList)) {
            Random random = new Random();

            // 拿出来所有待分配的任务
            List<IovSubscribeTask> allocatingTasks = iovSubscribeTaskService.listAllocatingTask();

            for (IovSubscribeTask task : allocatingTasks) {
                // 将这个任务分配给随机一个服务
                String serverName = serverNameList.get(random.nextInt(serverNameList.size()));
                iovSubscribeTaskService.allocatedTask(task, serverName);
            }
        }
    }

    /**
     * 将分配给当前节点的任务改成运行中
     */
    private void runningTask() {
        if (followerNodeThread == null) {
            followerNodeThread = new Thread(this::initialFollowerNodeThreadTask);
            followerNodeThread.setDaemon(true);
            followerNodeThread.setName("Follower Node Thread");
            followerNodeThreadFlag = true;

            followerNodeThread.start();
        }
    }

    /**
     * 初始化普通节点线程任务
     */
    private void initialFollowerNodeThreadTask() {
        while (followerNodeThreadFlag) {
            try {
                // 将已分配的任务更新为运行中
                updateRunningState();

                // 执行运行中的任务
                doRunningTask();
            } catch (Exception e) {
                log.error("Follower " + TtsContext.getNodeServerName() + " running task error", e);
            }

            try {
                Thread.sleep(FOLLOWER_NODE_TASK_INTERVAL * 1000 * 60);
            } catch (Exception e) {
                log.error("************* Follower Node Thread Sleep Error *************", e);
            }
        }
    }

    /**
     * 更新为运行状态
     */
    private void updateRunningState() {
        List<IovSubscribeTask> allocatedTasks = iovSubscribeTaskService.listCurrentNodeAllocatedTask();
        List<Long> idList = allocatedTasks.stream().map(IovSubscribeTask::getId).collect(Collectors.toList());

        iovSubscribeTaskService.runningTask(idList);
    }

    /**
     * 节点执行点位拉取任务
     */
    private void doRunningTask() {
        List<IovSubscribeTask> runningTask = iovSubscribeTaskService.listCurrentNodeRunningTask();
        // 获取运行中任务的ID
        List<Long> taskIdList = runningTask.stream().map(IovSubscribeTask::getId).collect(Collectors.toList());
        // 所有车辆任务
        List<IovSubscribeTaskVehicle> vehicleTasks = iovSubscribeTaskVehicleService.listInTaskIdList(taskIdList);

        // 执行车辆任务
        processVehicleTask(vehicleTasks);
    }

    /**
     * 执行车辆任务
     */
    private void processVehicleTask(List<IovSubscribeTaskVehicle> vehicleTasks) {
        for (IovSubscribeTaskVehicle vehicleTask : vehicleTasks) {
            try {
                // 查询点位
                GpsVehicleQueryDto queryDto = initialTrackPointListQueryParam(vehicleTask);
                List<GpsCoordinatePointResultDto> pointList = gpsService.queryIovVehicleTrackDirectly(queryDto);

                // 点位入库
                List<IovTrackPoint> points = BeanUtils.copyList(pointList, IovTrackPoint.class);
                iovTrackPointService.saveOrUpdateBatch(points);

                // 更新下次任务开始的时间
                vehicleTask.setStartTime(queryDto.getTimeEnd());
                iovSubscribeTaskVehicleService.updateByEntity(vehicleTask);
            } catch (Exception e) {
                log.error("Task: " + JSONObject.toJSONString(vehicleTask) + " save point list error", e);
            }
        }
    }

    /**
     * 初始化轨迹查询参数
     */
    private GpsVehicleQueryDto initialTrackPointListQueryParam(IovSubscribeTaskVehicle vehicleTask) {
        GpsVehicleQueryDto queryDto = new GpsVehicleQueryDto();
        // iov设备类型
        String iovType = iovSubscribeTaskVehicleService.getIovTypeById(vehicleTask.getId());
        queryDto.setIovTypeEnum(IovTypeEnums.parse(iovType));
        queryDto.setVehicleNo(vehicleTask.getVehicleNo());
        // 指定轨迹查询的起止时间，endTime不能超过当前时间
        queryDto.setTimeStart(vehicleTask.getStartTime());
        LocalDateTime endTime = vehicleTask.getStartTime().plusHours(TRACK_INTERVAL);
        if (LocalDateTime.now().isBefore(endTime)) {
            endTime = LocalDateTime.now();
        }
        queryDto.setTimeEnd(endTime);

        return queryDto;
    }
}
