package com.tts.main.service.impl;

import com.google.gson.Gson;
import com.tts.base.constant.IovTaskStateEnum;
import com.tts.framework.common.constant.IovTaskEventTypeEnum;
import com.tts.framework.common.constant.IovTaskOperTypeEnum;
import com.tts.framework.common.constant.LogOperationEnum;
import com.tts.framework.common.utils.IpUtils;
import com.tts.iov.dto.IovVehicleInputDto;
import com.tts.iov.dto.IovVehiclePointResultDto;
import com.tts.iov.facade.IovFacade;
import com.tts.iov.factory.IovFactory;
import com.tts.main.dao.*;
import com.tts.main.domain.*;
import com.tts.main.service.TtsIovConfigService;
import com.tts.main.service.TtsIovSubscribeTaskService;
import com.tts.main.utils.ConvertTool;
import com.tts.remote.dto.CoordinatePointResultDto;
import com.tts.remote.dto.TtsIovVehicleDirectQueryDto;
import com.tts.remote.dto.TtsIovVehicleDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;


/**
 * iov订阅任务的servcie
 *
 * @author FangYuan
 * @since 2022-12-22 16:24:08
 */
@Service
public class TtsIovSubscribeTaskServiceImpl implements TtsIovSubscribeTaskService {

    @Autowired
    private TtsIovConfigService ttsIovConfigService;

    @Resource
    private TtsIovSubscribeTaskMapper ttsIovSubscribeTaskMapper;

    @Resource
    private TtsIovSubscribeTaskLogMapper ttsIovSubscribeTaskLogMapper;

    @Resource
    private TtsIovSubscribeTaskVehicleMapper ttsIovSubscribeTaskVehicleMapper;

    @Resource
    private TtsIovSubscribeTaskVehicleLogMapper ttsIovSubscribeTaskVehicleLogMapper;

    @Resource
    private TtsIovLogMapper ttsIovLogMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean startSubscribeIovTask(String carrierCode, String iovType) throws Exception {

        //查询对应的iov配置
        TtsIovConfig ttsIovConfig = ttsIovConfigService.queryByCarrierCodeAndIovType(carrierCode, iovType);

        //如果配置不存在则抛出异常
        if (ttsIovConfig == null) {
            throw new Exception("tts iov配置[" + carrierCode + "," + iovType + "] 不存在 ! ");
        }

        //查询该任务状态
        TtsIovSubscribeTask ttsIovSubscribeTask = this.queryIovSubscribeTaskByIovConfigId(ttsIovConfig.getId());


        //如果任务是空,则创建任务 并将任务状态设置为 ALLOCATING
        if (ttsIovSubscribeTask == null) {
            ttsIovSubscribeTask = this.createTaskInner(ttsIovConfig, IovTaskOperTypeEnum.MANUAL);
            this.startTaskInner(ttsIovSubscribeTask);
            return true;
        }


        //如果任务在启动中则忽略 ,直接返回
        Integer state = ttsIovSubscribeTask.getState();

        //如果任务是 ALLOCATING 或者 RUNNING 状态,表示任务在正常运行中,则不对其进行处理
        if (state.equals(IovTaskStateEnum.ALLOCATING.getValue())
                || state.equals(IovTaskStateEnum.RUNNING.getValue())) {
            return false;
        }
        //如果任务是 ABNORMAL STOPPING,或者  STOPPED 状态   ,则将其更改状态 为 ALLOCATING
        else if (state.equals(IovTaskStateEnum.STOPPING.getValue())
                || state.equals(IovTaskStateEnum.STOPPED.getValue())) {

            //启动任务,即将任务状态更改为 ALLOCATING
            this.startTaskInner(ttsIovSubscribeTask);

            return true;
        }
        //如果任务的状态不符合以上条件则直接返回不处理
        else {
            return false;
        }


    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean stopSubscribeIovTask(String carrierCode, String iovType) throws Exception {

        //查询对应的iov配置
        TtsIovConfig ttsIovConfig = ttsIovConfigService.queryByCarrierCodeAndIovType(carrierCode, iovType);

        //如果配置不存在则抛出异常
        if (ttsIovConfig == null) {
            throw new Exception("tts iov config [" + carrierCode + "," + iovType + "] not exists ! ");
        }

        //查询是否有对应的任务在运行
        TtsIovSubscribeTask ttsIovSubscribeTask = this.queryIovSubscribeTaskByIovConfigId(ttsIovConfig.getId());

        //如果任务没有在运行则返回false
        if (ttsIovSubscribeTask == null) {
            return false;
        }


        this.stopTaskInner(ttsIovSubscribeTask, IovTaskOperTypeEnum.MANUAL);

        return true;

    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean addSubscribeVehicle(TtsIovVehicleDto ttsIovVehicleDto) throws Exception {
        String carrierCode = ttsIovVehicleDto.getCarrierCode();
        String iovType = ttsIovVehicleDto.getIovType();

        //查询对应的iov配置
        TtsIovConfig ttsIovConfig = ttsIovConfigService.queryByCarrierCodeAndIovType(carrierCode, iovType);

        //如果配置不存在则抛出异常
        if (ttsIovConfig == null) {
            throw new Exception("tts iov config[" + carrierCode + "," + iovType + "] not exists ! ");
        }

        //查询对应的iov 订阅任务
        TtsIovSubscribeTask ttsIovSubscribeTask = this.queryIovSubscribeTaskByIovConfigId(ttsIovConfig.getId());

        //如果任务不存在则抛出异常
        if (ttsIovSubscribeTask == null) {
            throw new Exception("tts iov task[" + ttsIovConfig.getId() + "," + carrierCode + "," + iovType + "] not exists ! ");
        }

        //查询 tts_iov_subscribe_task_vehicle 表中是否已经有相同 (iovConfigId,vehicleNo) 的记录
        TtsIovSubscribeTaskVehicle ttsIovSubscribeTaskVehicleOld = this.queryIovSubscribeTaskVehicle(ttsIovSubscribeTask.getId(), ttsIovVehicleDto.getVehicleNo());

        //如果有对应的车辆 则需要先判断两者是否完全相同,
        // 如果相同则不处理
        // 如果不相同则需要先删除再插入
        if (ttsIovSubscribeTaskVehicleOld != null) {
            //新老车辆记录完全相同,则不处理直接返回
            if (ttsIovSubscribeTask.getId().equals(ttsIovSubscribeTaskVehicleOld.getTaskId())
                    && ttsIovSubscribeTaskVehicleOld.getVehicleId().equals(ttsIovVehicleDto.getVehicleId())
                    && ttsIovSubscribeTaskVehicleOld.getVehicleNo().equals(ttsIovVehicleDto.getVehicleNo())
                    && ttsIovSubscribeTaskVehicleOld.getShipmentId().equals(ttsIovVehicleDto.getShipmentId())
                    && ttsIovSubscribeTaskVehicleOld.getVehicleColorType().equals(ttsIovVehicleDto.getVehicleColorType())) {
                return true;
            }
            //新老车辆不相同,则删除老车辆信息
            else {
                //删除车辆
                ttsIovSubscribeTaskVehicleMapper.deleteById(ttsIovSubscribeTaskVehicleOld.getId());
                //追加车辆日志
                this.appendVehicleLog(ttsIovSubscribeTaskVehicleOld, LogOperationEnum.DELETE);
            }
        }
        //如果没有对应的记录则直接插入新记录
        TtsIovSubscribeTaskVehicle ttsIovSubscribeTaskVehicleNew = ConvertTool.toTtsIovSubscribeTaskVehicle(ttsIovVehicleDto);
        ttsIovSubscribeTaskVehicleNew.setTaskId(ttsIovSubscribeTask.getId());
        ttsIovSubscribeTaskVehicleNew.setCreateTime(LocalDateTime.now());
        ttsIovSubscribeTaskVehicleMapper.insert(ttsIovSubscribeTaskVehicleNew);
        //追加车辆日志
        this.appendVehicleLog(ttsIovSubscribeTaskVehicleNew, LogOperationEnum.INSERT);
        return true;

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean removeSubscribeVehicle(TtsIovVehicleDto ttsIovVehicleDto) throws Exception {
        String carrierCode = ttsIovVehicleDto.getCarrierCode();
        String iovType = ttsIovVehicleDto.getIovType();

        //查询对应的iov配置
        TtsIovConfig ttsIovConfig = ttsIovConfigService.queryByCarrierCodeAndIovType(carrierCode, iovType);

        //如果配置不存在则抛出异常
        if (ttsIovConfig == null) {
            throw new Exception("tts iov config[" + carrierCode + "," + iovType + "] not exists ! ");
        }

        //查询对应的iov 订阅任务
        TtsIovSubscribeTask ttsIovSubscribeTask = this.queryIovSubscribeTaskByIovConfigId(ttsIovConfig.getId());

        //如果任务不存在则抛出异常
        if (ttsIovSubscribeTask == null) {
            throw new Exception("tts iov task[" + ttsIovConfig.getId() + "," + carrierCode + "," + iovType + "] not exists ! ");
        }

        //查询有没有对应的车辆信息
        TtsIovSubscribeTaskVehicle ttsIovSubscribeTaskVehicle = this.queryIovSubscribeTaskVehicle(ttsIovSubscribeTask.getId(), ttsIovVehicleDto.getVehicleNo());

        //如果没有对应的车辆 则直接返回false
        if (ttsIovSubscribeTaskVehicle == null) {
            return false;
        }
        //如果有对应的车辆,则 删除并追加车辆日志
        else {

            //删除车辆
            ttsIovSubscribeTaskVehicleMapper.deleteById(ttsIovSubscribeTaskVehicle.getId());
            //追加车辆日志
            this.appendVehicleLog(ttsIovSubscribeTaskVehicle, LogOperationEnum.DELETE);

            return true;
        }

    }

    @Override
    public TtsIovSubscribeTask queryIovSubscribeTaskByIovConfigId(Long iovConfigId) {

        TtsIovSubscribeTask ttsIovSubscribeTask = new TtsIovSubscribeTask();

        ttsIovSubscribeTask.setIovConfigId(iovConfigId);

        List<TtsIovSubscribeTask> iovSubscribeTaskList = this.ttsIovSubscribeTaskMapper.selectByQueryCondition(ttsIovSubscribeTask);

        if (iovSubscribeTaskList.size() == 0) {
            return null;
        } else {
            return iovSubscribeTaskList.get(0);
        }
    }

    @Override
    public TtsIovSubscribeTaskVehicle queryIovSubscribeTaskVehicle(Long iovSubscribeTaskId, String vehicleNo) {

        TtsIovSubscribeTaskVehicle queryCondition = new TtsIovSubscribeTaskVehicle();

        queryCondition.setTaskId(iovSubscribeTaskId);
        queryCondition.setVehicleNo(vehicleNo);

        List<TtsIovSubscribeTaskVehicle> iovSubscribeTaskVehicleList = this.ttsIovSubscribeTaskVehicleMapper.selectByQueryCondition(queryCondition);

        if (iovSubscribeTaskVehicleList.size() == 0) {
            return null;
        } else {
            return iovSubscribeTaskVehicleList.get(0);
        }
    }


    @Override
    public List<TtsIovSubscribeTaskVehicle> queryIovSubscribeTaskVehicle(Long taskId) {

        TtsIovSubscribeTaskVehicle queryCondition = new TtsIovSubscribeTaskVehicle();

        queryCondition.setTaskId(taskId);

        List<TtsIovSubscribeTaskVehicle> iovSubscribeTaskVehicleList = this.ttsIovSubscribeTaskVehicleMapper.selectByQueryCondition(queryCondition);

        return iovSubscribeTaskVehicleList;
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void appendVehicleLog(TtsIovSubscribeTaskVehicle ttsIovSubscribeTaskVehicle, LogOperationEnum operationEnum) {
        TtsIovSubscribeTaskVehicleLog ttsIovSubscribeTaskVehicleLog =
                ConvertTool.toTtsIovSubscribeTaskVehicleLog(ttsIovSubscribeTaskVehicle);

        ttsIovSubscribeTaskVehicleLog.setOperation(operationEnum.name());
        ttsIovSubscribeTaskVehicleLog.setServerIp(IpUtils.getHostIp());
        ttsIovSubscribeTaskVehicleLog.setCreateTime(LocalDateTime.now());

        this.ttsIovSubscribeTaskVehicleLogMapper.insert(ttsIovSubscribeTaskVehicleLog);

    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public TtsIovSubscribeTask createTaskInner(TtsIovConfig ttsIovConfig, IovTaskOperTypeEnum iovTaskOperTypeEnum) {
        LocalDateTime currentDate = LocalDateTime.now();
        TtsIovSubscribeTask iovSubscribeTask = new TtsIovSubscribeTask();
        iovSubscribeTask.setIovConfigId(ttsIovConfig.getId());
        iovSubscribeTask.setState(IovTaskStateEnum.STOPPED.getValue());
        iovSubscribeTask.setCreateTime(currentDate);
        iovSubscribeTask.setUpdateTime(currentDate);

        this.ttsIovSubscribeTaskMapper.insert(iovSubscribeTask);

        //追加任务日志
        this.appendTaskLog(iovSubscribeTask, "创建任务", IovTaskStateEnum.NONE.getValue(), IovTaskStateEnum.STOPPED.getValue(), IovTaskEventTypeEnum.NORMAL, iovTaskOperTypeEnum);

        return iovSubscribeTask;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean startTaskInner(TtsIovSubscribeTask ttsIovSubscribeTask) {

        return startTaskInner(ttsIovSubscribeTask, "启动任务", IovTaskEventTypeEnum.NORMAL, IovTaskOperTypeEnum.MANUAL);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean startTaskInner(TtsIovSubscribeTask ttsIovSubscribeTask, String logContent, IovTaskEventTypeEnum iovTaskEventTypeEnum, IovTaskOperTypeEnum iovTaskOperTypeEnum) {

        Integer preState = ttsIovSubscribeTask.getState();

        //将任务设置为 ALLOCATING 状态
        ttsIovSubscribeTask.setState(IovTaskStateEnum.ALLOCATING.getValue());
        ttsIovSubscribeTask.setUpdateTime(LocalDateTime.now());

        //更新任务状态
        this.ttsIovSubscribeTaskMapper.updateById(ttsIovSubscribeTask);

        //追加任务日志
        this.appendTaskLog(ttsIovSubscribeTask, logContent, preState, IovTaskStateEnum.ALLOCATING.getValue(), iovTaskEventTypeEnum, iovTaskOperTypeEnum);

        return true;
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean stopTaskInner(TtsIovSubscribeTask ttsIovSubscribeTask, IovTaskOperTypeEnum iovTaskOperTypeEnum) {

        Integer preState = ttsIovSubscribeTask.getState();

        //将任务设置为 STOPPING 状态
        ttsIovSubscribeTask.setState(IovTaskStateEnum.STOPPING.getValue());
        ttsIovSubscribeTask.setUpdateTime(LocalDateTime.now());

        //更新任务状态
        this.ttsIovSubscribeTaskMapper.updateById(ttsIovSubscribeTask);

        //追加任务日志
        this.appendTaskLog(ttsIovSubscribeTask, "停止任务", preState, IovTaskStateEnum.STOPPING.getValue(), IovTaskEventTypeEnum.NORMAL, iovTaskOperTypeEnum);

        return true;

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void stoppedSubscribeIovTask(TtsIovSubscribeTask ttsIovSubscribeTask, IovTaskEventTypeEnum iovTaskEventTypeEnum) {

        Integer preState = ttsIovSubscribeTask.getState();

        //将任务设置为 STOPPING 状态
        ttsIovSubscribeTask.setState(IovTaskStateEnum.STOPPED.getValue());
        ttsIovSubscribeTask.setUpdateTime(LocalDateTime.now());

        //更新任务状态
        this.ttsIovSubscribeTaskMapper.updateById(ttsIovSubscribeTask);

        //记录任务已经停止的日志
        this.appendTaskLog(ttsIovSubscribeTask, "任务已停止", preState, IovTaskStateEnum.STOPPED.getValue(), iovTaskEventTypeEnum, IovTaskOperTypeEnum.AUTO);

    }


    /**
     * 追加任务日志
     */
    @Override
    public void appendTaskLog(TtsIovSubscribeTask iovSubscribeTask, String content, Integer preState, Integer nextState,
                              IovTaskEventTypeEnum eventType, IovTaskOperTypeEnum iovTaskOperTypeEnum) {
        TtsIovSubscribeTaskLog ttsIovSubscribeTaskLog = new TtsIovSubscribeTaskLog();
        ttsIovSubscribeTaskLog.setTaskId(iovSubscribeTask.getId());
        ttsIovSubscribeTaskLog.setContent(content);
        ttsIovSubscribeTaskLog.setPreState(preState);
        ttsIovSubscribeTaskLog.setNextState(nextState);
        ttsIovSubscribeTaskLog.setEventType(eventType.getValue());
        ttsIovSubscribeTaskLog.setOperType(iovTaskOperTypeEnum.getValue());
        ttsIovSubscribeTaskLog.setServerIp(IpUtils.getHostIp());
        ttsIovSubscribeTaskLog.setCreateTime(LocalDateTime.now());

        this.ttsIovSubscribeTaskLogMapper.insert(ttsIovSubscribeTaskLog);
    }

    @Override
    public List<TtsIovSubscribeTask> queryIovSubscribeTaskInAllocatingState() {
        TtsIovSubscribeTask queryCondition = new TtsIovSubscribeTask();
        queryCondition.setState(IovTaskStateEnum.ALLOCATING.getValue());
        return this.ttsIovSubscribeTaskMapper.selectByQueryCondition(queryCondition);
    }


    @Override
    public List<TtsIovSubscribeTask> queryIovSubscribeTaskInRunningState() {
        TtsIovSubscribeTask queryCondition = new TtsIovSubscribeTask();
        queryCondition.setState(IovTaskStateEnum.RUNNING.getValue());
        return this.ttsIovSubscribeTaskMapper.selectByQueryCondition(queryCondition);
    }

    @Override
    public List<String> queryServerIpInRunningState() {
        List<String> serverIpList = ttsIovSubscribeTaskMapper.selectServerIpInRunningState();
        return serverIpList;
    }

    @Override
    public TtsIovSubscribeTask getIovSubscribeTaskById(Long id) {
        return ttsIovSubscribeTaskMapper.selectById(id);
    }


    @Override
    public List<TtsIovSubscribeTask> queryIovSubscribeTaskByServerIp(String serverIp) {

        TtsIovSubscribeTask queryCondition = new TtsIovSubscribeTask();
        queryCondition.setServerIp(serverIp);
        return ttsIovSubscribeTaskMapper.selectByQueryCondition(queryCondition);
    }

    @Override
    public List<TtsIovSubscribeTask> queryIovSubscribeTaskInAllocatedState(String serverIp) {
        TtsIovSubscribeTask queryCondition = new TtsIovSubscribeTask();
        queryCondition.setState(IovTaskStateEnum.ALLOCATED.getValue());
        queryCondition.setServerIp(serverIp);
        return ttsIovSubscribeTaskMapper.selectByQueryCondition(queryCondition);
    }

    @Override
    public List<TtsIovSubscribeTask> queryIovSubscribeTaskInStoppingState(String serverIp) {

        TtsIovSubscribeTask queryCondition = new TtsIovSubscribeTask();
        queryCondition.setState(IovTaskStateEnum.STOPPING.getValue());
        queryCondition.setServerIp(serverIp);
        return ttsIovSubscribeTaskMapper.selectByQueryCondition(queryCondition);
    }

    @Override
    public List<TtsIovSubscribeTask> queryIovSubscribeTaskInRunningState(String serverIp) {

        TtsIovSubscribeTask queryCondition = new TtsIovSubscribeTask();
        queryCondition.setState(IovTaskStateEnum.RUNNING.getValue());
        queryCondition.setServerIp(serverIp);
        return ttsIovSubscribeTaskMapper.selectByQueryCondition(queryCondition);
    }


    @Override
    @Transactional
    public void allocatedTask(TtsIovSubscribeTask ttsIovSubscribeTask, String serverIp, IovTaskOperTypeEnum iovTaskOperTypeEnum) {

        Integer preState = ttsIovSubscribeTask.getState();

        ttsIovSubscribeTask.setServerIp(serverIp);
        ttsIovSubscribeTask.setState(IovTaskStateEnum.ALLOCATED.getValue());
        ttsIovSubscribeTask.setUpdateTime(LocalDateTime.now());
        ttsIovSubscribeTaskMapper.updateById(ttsIovSubscribeTask);

        //追加任务日志
        this.appendTaskLog(ttsIovSubscribeTask, "分配任务", preState, IovTaskStateEnum.ALLOCATED.getValue(), IovTaskEventTypeEnum.NORMAL, iovTaskOperTypeEnum);
    }


    /**
     * 重新分配任务
     * 一般用于 任务处于 running 但是对应服务器节点没有其运行时的状态
     */
    @Override
    @Transactional
    public void reAllocatingTask(TtsIovSubscribeTask ttsIovSubscribeTask) {
        Integer preState = ttsIovSubscribeTask.getState();

        ttsIovSubscribeTask.setState(IovTaskStateEnum.ALLOCATING.getValue());
        ttsIovSubscribeTask.setUpdateTime(LocalDateTime.now());
        ttsIovSubscribeTaskMapper.updateById(ttsIovSubscribeTask);

        //追加任务日志
        this.appendTaskLog(ttsIovSubscribeTask, "重新分配中任务[" + ttsIovSubscribeTask.getServerIp() + "]", preState, IovTaskStateEnum.ALLOCATING.getValue(), IovTaskEventTypeEnum.ABNORMAL, IovTaskOperTypeEnum.AUTO);
    }


    @Override
    @Transactional
    public void runningTask(TtsIovSubscribeTask ttsIovSubscribeTask, IovTaskOperTypeEnum iovTaskOperTypeEnum) {
        Integer preState = ttsIovSubscribeTask.getState();
        ttsIovSubscribeTask.setState(IovTaskStateEnum.RUNNING.getValue());
        ttsIovSubscribeTask.setUpdateTime(LocalDateTime.now());
        ttsIovSubscribeTaskMapper.updateById(ttsIovSubscribeTask);

        //追加任务日志
        this.appendTaskLog(ttsIovSubscribeTask, "运行任务", preState, IovTaskStateEnum.RUNNING.getValue(), IovTaskEventTypeEnum.NORMAL, iovTaskOperTypeEnum);
    }


    @Override
    public TtsIovSubscribeTaskLog queryIovSubscribeTaskLogLatestManual(Long taskId) {

        TtsIovSubscribeTaskLog queryCondition = new TtsIovSubscribeTaskLog();

        queryCondition.setTaskId(taskId);
        queryCondition.setOperType(IovTaskOperTypeEnum.MANUAL.getValue());


        List<TtsIovSubscribeTaskLog> ttsIovSubscribeTaskLogList = this.ttsIovSubscribeTaskLogMapper.selectIovSubscribeTaskLogLatestManual();

        if (ttsIovSubscribeTaskLogList.size() > 0) {
            return ttsIovSubscribeTaskLogList.get(0);
        } else {
            return null;
        }

    }

    @Override
    public void appendIovLog(TtsIovSubscribeTask ttsIovSubscribeTask, IovVehicleInputDto iovVehicleInputDto
            , List<IovVehiclePointResultDto> iovVehiclePointResultDtoList
            , Integer status, long timeConsuming, boolean isDebug) {

        TtsIovLog ttsIovLog = new TtsIovLog();

        Gson gson = new Gson();

        ttsIovLog.setTaskId(ttsIovSubscribeTask.getId());

        if (iovVehicleInputDto != null
                && iovVehicleInputDto.getVehicleNoList() != null) {

            ttsIovLog.setRequestSize(iovVehicleInputDto.getVehicleNoList().size());

            if (isDebug) {
                ttsIovLog.setRequest(gson.toJson(iovVehicleInputDto.getVehicleNoList()));
            }
        } else {
            ttsIovLog.setRequestSize(0);
        }

        if (iovVehiclePointResultDtoList != null) {

            ttsIovLog.setResponseSize(iovVehiclePointResultDtoList.size());

            if (isDebug) {

                List<String> vehicleNoList = new ArrayList<>();

                for (IovVehiclePointResultDto iovVehiclePointResultDto : iovVehiclePointResultDtoList) {
                    vehicleNoList.add(iovVehiclePointResultDto.getVehicleNo());
                }
                ttsIovLog.setResponse(gson.toJson(vehicleNoList));
            }
        }


        ttsIovLog.setStatus(status);
        ttsIovLog.setTimeConsuming(timeConsuming);
        ttsIovLog.setCreateTime(LocalDateTime.now());


        this.ttsIovLogMapper.insert(ttsIovLog);
    }


    /**
     * 直接调用iov 平台接口查询车辆的最新信息
     */
    public List<CoordinatePointResultDto> queryIovVehicleLastLocationDirectly(TtsIovVehicleDirectQueryDto iovVehicleDirectQueryDto) throws Exception {
        String carrierCode = iovVehicleDirectQueryDto.getCarrierCode();
        String iovType = iovVehicleDirectQueryDto.getIovTypeEnum().name();


        //根据 iov type 获取配置信息,如果没有对应配置信息则抛出异常
        TtsIovConfig ttsIovConfig = ttsIovConfigService.queryByCarrierCodeAndIovType(carrierCode, iovType);

        //如果配置不存在则抛出异常
        if (ttsIovConfig == null) {
            throw new Exception("queryIovVehicleLastLocationDirectly tts iov配置[" + carrierCode + "," + iovType + "] 不存在 ! ");
        }

        Gson gson = new Gson();
        Properties properties = gson.fromJson(ttsIovConfig.getConfig(), Properties.class);
        IovFacade iovFacade = IovFactory.createIovFacade(iovType, properties);

        IovVehicleInputDto iovVehicleInputDto = ConvertTool.toIovVehicleInputDto(iovVehicleDirectQueryDto);

        List<IovVehiclePointResultDto> iovVehiclePointResultDtoList = iovFacade.queryVehicleLastLocation(iovVehicleInputDto);

        List<CoordinatePointResultDto> result = new ArrayList<>();

        if (iovVehiclePointResultDtoList != null) {
            for (IovVehiclePointResultDto iovVehiclePointResultDto : iovVehiclePointResultDtoList) {
                result.add(ConvertTool.toCoordinatePointResultDto(iovVehiclePointResultDto));
            }
        }

        return result;
    }

    /**
     * 直接调用iov 平台接口查询车辆的轨迹信息
     */
    public List<CoordinatePointResultDto> queryIovVehicleTrackDirectly(TtsIovVehicleDirectQueryDto iovVehicleDirectQueryDto) throws Exception {

        String carrierCode = iovVehicleDirectQueryDto.getCarrierCode();
        String iovType = iovVehicleDirectQueryDto.getIovTypeEnum().name();


        //根据 iov type 获取配置信息,如果没有对应配置信息则抛出异常
        TtsIovConfig ttsIovConfig = ttsIovConfigService.queryByCarrierCodeAndIovType(carrierCode, iovType);

        //如果配置不存在则抛出异常
        if (ttsIovConfig == null) {
            throw new Exception("queryIovVehicleLastLocationDirectly tts iov配置[" + carrierCode + "," + iovType + "] 不存在 ! ");
        }

        Gson gson = new Gson();
        Properties properties = gson.fromJson(ttsIovConfig.getConfig(), Properties.class);
        IovFacade iovFacade = IovFactory.createIovFacade(iovType, properties);
        IovVehicleInputDto iovVehicleInputDto = ConvertTool.toIovVehicleInputDto(iovVehicleDirectQueryDto);

        List<IovVehiclePointResultDto> iovVehiclePointResultDtoList = iovFacade.queryVehicleTrack(iovVehicleInputDto);

        List<CoordinatePointResultDto> result = new ArrayList<>();

        if (iovVehiclePointResultDtoList != null) {

            for (IovVehiclePointResultDto iovVehiclePointResultDto : iovVehiclePointResultDtoList) {
                result.add(ConvertTool.toCoordinatePointResultDto(iovVehiclePointResultDto));
            }
        }

        return result;
    }

}

