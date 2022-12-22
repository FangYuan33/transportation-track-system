package com.tts.main.service;

import com.tts.framework.common.constant.IovTaskEventTypeEnum;
import com.tts.framework.common.constant.IovTaskOperTypeEnum;
import com.tts.framework.common.constant.LogOperationEnum;
import com.tts.iov.dto.IovVehicleInputDto;
import com.tts.iov.dto.IovVehiclePointResultDto;
import com.tts.main.domain.TtsIovConfig;
import com.tts.main.domain.TtsIovSubscribeTask;
import com.tts.main.domain.TtsIovSubscribeTaskLog;
import com.tts.main.domain.TtsIovSubscribeTaskVehicle;
import com.tts.remote.dto.CoordinatePointResultDto;
import com.tts.remote.dto.TtsIovVehicleDirectQueryDto;
import com.tts.remote.dto.TtsIovVehicleDto;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * iov 信息订阅任务 service
 *
 * @author FangYuan
 * @since 2022-12-22 15:47:20
 */
public interface TtsIovSubscribeTaskService {

    /**
     * 根据 承运商 code 和iov 类型 启动订阅任务
     * 如果对应的配置不存在则抛出异常
     * 如果对应的任务已经存在,则停止任务,重新启动
     */
    boolean startSubscribeIovTask(String carrierCode, String iov) throws Exception;

    /**
     * 根据承运商code 和iov 停止订阅任务
     * 如果配置不存在则 抛出异常
     * 如果任务没有处于运行中,则返回false
     * 如果任务在运行中,则停止任务并返回true
     */
    boolean stopSubscribeIovTask(String carrierCode, String iov) throws Exception;

    /**
     * 添加订阅车辆信息
     */
    boolean addSubscribeVehicle(TtsIovVehicleDto ttsIovVehicleDto) throws Exception;

    /**
     * 移除订阅车辆信息
     */
    boolean removeSubscribeVehicle(TtsIovVehicleDto ttsIovVehicleDto) throws Exception;


    /**
     * 根据iov 配置id 查询iov 订阅任务信息
     * 一个iov 配置 最多只会有一个对应的iov订阅任务
     */
    TtsIovSubscribeTask queryIovSubscribeTaskByIovConfigId(Long iovConfigId);

    /**
     * 根据订阅任务id 和车牌号查询车辆信息
     */
    TtsIovSubscribeTaskVehicle queryIovSubscribeTaskVehicle(Long iovSubscribeTaskId, String vehicleNo);

    /**
     * 追加车辆信息
     */
    void appendVehicleLog(TtsIovSubscribeTaskVehicle ttsIovSubscribeTaskVehicle, LogOperationEnum operationEnum);

    /**
     * 基于 iov 配置信息 创建 任务
     * 如果任务已经存在则忽略
     * 新创建的任务默认 状态 为 3: 正常停止
     */
    TtsIovSubscribeTask createTaskInner(TtsIovConfig ttsIovConfig, IovTaskOperTypeEnum manual);

    /**
     * 启动任务,如果任务已经处于  ALLOCATING 或者 RUNNING 状态 则忽略
     */
    boolean startTaskInner(TtsIovSubscribeTask ttsIovSubscribeTask);

    @Transactional(rollbackFor = Exception.class)
    boolean startTaskInner(TtsIovSubscribeTask ttsIovSubscribeTask, String logContent,
                           IovTaskEventTypeEnum iovTaskEventTypeEnum, IovTaskOperTypeEnum iovTaskOperTypeEnum);

    /**
     * 停止任务,对于 STOPPING 和 STOPPED 的任务忽略
     */
    boolean stopTaskInner(TtsIovSubscribeTask ttsIovSubscribeTask, IovTaskOperTypeEnum manual);

    @Transactional(rollbackFor = Exception.class)
    void stoppedSubscribeIovTask(TtsIovSubscribeTask ttsIovSubscribeTask, IovTaskEventTypeEnum iovTaskEventTypeEnum);

    /**
     * 追加任务日志
     */
    void appendTaskLog(TtsIovSubscribeTask iovSubscribeTask, String content, Integer preState, Integer nextState,
                       IovTaskEventTypeEnum eventType, IovTaskOperTypeEnum iovTaskOperTypeEnum);

    /**
     * 查询当前处于 RUNNING 状态的 iov 任务
     */
    List<TtsIovSubscribeTask> queryIovSubscribeTaskInRunningState();

    /**
     * 查询处于运行状态的任务的服务器列表
     */
    List<String> queryServerIpInRunningState();

    /**
     * 通过 server ip 查询 任务信息
     */
    List<TtsIovSubscribeTask> queryIovSubscribeTaskByServerIp(String serverIp);

    TtsIovSubscribeTask getIovSubscribeTaskById(Long id);

    List<TtsIovSubscribeTask> queryIovSubscribeTaskInAllocatingState();

    void allocatedTask(TtsIovSubscribeTask ttsIovSubscribeTask, String serverIp, IovTaskOperTypeEnum auto);

    List<TtsIovSubscribeTask> queryIovSubscribeTaskInAllocatedState(String serverIp);

    /**
     * 将任务从 ALLOCATED 状态更改为 RUNNING 状态
     */
    void runningTask(TtsIovSubscribeTask ttsIovSubscribeTask, IovTaskOperTypeEnum iovTaskOperTypeEnum);

    /**
     * 查询 task 的任务日志 中 操作类型为 手动的, id排序 倒序排第一,即最新的任务日志消息
     */
    TtsIovSubscribeTaskLog queryIovSubscribeTaskLogLatestManual(Long taskId);

    /**
     * 根据车辆id 查询车辆列表
     */
    List<TtsIovSubscribeTaskVehicle> queryIovSubscribeTaskVehicle(Long taskId);


    /**
     * 搜索分配给本机的处于stopping 状态的任务
     */
    List<TtsIovSubscribeTask> queryIovSubscribeTaskInStoppingState(String localHostIp);

    /**
     * 搜索分配给本机的处于running 状态的任务
     */
    List<TtsIovSubscribeTask> queryIovSubscribeTaskInRunningState(String localHostIp);

    /**
     * 重新分配任务
     * 一般用于 任务处于 running 但是对应服务器节点没有其运行时的状态
     */
    void reAllocatingTask(TtsIovSubscribeTask task);

    /**
     * 记录iov 请求日志
     */
    void appendIovLog(TtsIovSubscribeTask ttsIovSubscribeTask, IovVehicleInputDto iovVehicleInputDto,
                      List<IovVehiclePointResultDto> iovVehiclePointResultDtoList, Integer status,
                      long timeConsuming, boolean isDebug);

    /**
     * 直接调用iov 平台接口查询车辆的最新信息
     */
    List<CoordinatePointResultDto> queryIovVehicleLastLocationDirectly(TtsIovVehicleDirectQueryDto iovVehicleDirectQueryDto) throws Exception;

    /**
     * 直接调用iov 平台接口查询车辆的轨迹信息
     */
    List<CoordinatePointResultDto> queryIovVehicleTrackDirectly(TtsIovVehicleDirectQueryDto iovVehicleDirectQueryDto) throws Exception;
}
