package com.tts.iov.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tts.common.context.TtsContext;
import com.tts.common.exception.ServiceException;
import com.tts.iov.dao.IovSubscribeTaskMapper;
import com.tts.iov.domain.IovConfig;
import com.tts.iov.domain.IovSubscribeTask;
import com.tts.iov.service.IovConfigService;
import com.tts.iov.service.IovSubscribeTaskService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

import static com.tts.iov.enums.IovSubscribeTaskStateEnums.*;

@Slf4j
@Service
public class IovSubscribeTaskServiceImpl extends ServiceImpl<IovSubscribeTaskMapper, IovSubscribeTask>
        implements IovSubscribeTaskService {

    @Autowired
    private IovConfigService iovConfigService;

    @Override
    public boolean startSubscribeTask(String carrierCode, String iovType) {
        IovConfig iovConfig = getIovConfigByIovType(iovType);

        // 获取订阅任务
        IovSubscribeTask subscribeTask = selectByIovConfigIdAndCarrierCode(iovConfig.getId(), carrierCode);

        // 如果为空的话 创建一个
        if (subscribeTask == null) {
            createTask(iovConfig.getId(), carrierCode);
            return true;
        }

        // 根据已有的任务状态进行业务处理
        return processByState(subscribeTask);
    }

    /**
     * 创建待分配的任务
     */
    private void createTask(Long iovConfigId, String carrierCode) {
        // 初始化 待分配状态的任务
        IovSubscribeTask subscribeTask = new IovSubscribeTask();
        subscribeTask.setIovConfigId(iovConfigId).setCarrierCode(carrierCode)
                .setState(ALLOCATING.getValue());

        // 入库
        baseMapper.insert(subscribeTask);
        log.info("Carrier: {} Start Task: {}", carrierCode, JSONObject.toJSONString(subscribeTask));
    }

    /**
     * 根据状态处理已经存在的订阅任务
     */
    private boolean processByState(IovSubscribeTask subscribeTask) {
        Integer taskState = subscribeTask.getState();

        if (ALLOCATING.getValue().equals(taskState) || ALLOCATED.getValue().equals(taskState)
                || RUNNING.getValue().equals(taskState)) {
            return false;
        }
        // 重新开启
        if (ERROR.getValue().equals(taskState) || STOPPED.getValue().equals(taskState)) {
            restartTask(subscribeTask);
            log.info("Carrier: {} Restart Task: {}", subscribeTask.getCarrierCode(), JSONObject.toJSONString(subscribeTask));
            return true;
        }

        return false;
    }

    /**
     * 重启任务
     */
    private void restartTask(IovSubscribeTask subscribeTask) {
        IovSubscribeTask updateEntity = subscribeTask.setState(ALLOCATING.getValue());

        baseMapper.updateById(updateEntity);
    }

    @Override
    public boolean stopSubscribeTask(String carrierCode, String iovType) {
        IovConfig iovConfig = getIovConfigByIovType(iovType);

        IovSubscribeTask subscribeTask = selectByIovConfigIdAndCarrierCode(iovConfig.getId(), carrierCode);

        // 任务存在则更改状态
        if (subscribeTask != null) {
            subscribeTask.setState(STOPPED.getValue());
            baseMapper.updateById(subscribeTask);

            log.info("Carrier: {} Stop Task: {}", subscribeTask.getCarrierCode(), JSONObject.toJSONString(subscribeTask));
            return true;
        }

        return false;
    }

    /**
     * 根据iovType获取对应配置信息
     */
    private IovConfig getIovConfigByIovType(String iovType) {
        IovConfig iovConfig = iovConfigService.getByIovType(iovType);

        if (iovConfig == null) {
            throw new ServiceException("TTS Iov Config [" + iovType + "] Not Found!");
        }

        return iovConfig;
    }

    @Override
    public IovSubscribeTask selectByIovConfigIdAndCarrierCode(Long iovConfigId, String carrierCode) {
        LambdaQueryWrapper<IovSubscribeTask> queryWrapper = new QueryWrapper<IovSubscribeTask>()
                .lambda().eq(IovSubscribeTask::getIovConfigId, iovConfigId)
                .eq(IovSubscribeTask::getCarrierCode, carrierCode);

        return baseMapper.selectOne(queryWrapper);
    }

    @Override
    public List<IovSubscribeTask> listRunningTask() {
        return baseMapper.selectList(new QueryWrapper<IovSubscribeTask>().lambda().eq(IovSubscribeTask::getState, RUNNING.getValue()));
    }

    @Override
    public List<IovSubscribeTask> listByServerNames(List<String> serverNames) {
        LambdaQueryWrapper<IovSubscribeTask> queryWrapper = new QueryWrapper<IovSubscribeTask>()
                .lambda().in(IovSubscribeTask::getServerName, serverNames);

        return baseMapper.selectList(queryWrapper);
    }

    @Override
    public void allocatingTask(IovSubscribeTask task) {
        task.setState(ALLOCATING.getValue());
        baseMapper.updateById(task);
    }

    @Override
    public List<IovSubscribeTask> listAllocatingTask() {
        return baseMapper.selectList(new QueryWrapper<IovSubscribeTask>().lambda().eq(IovSubscribeTask::getState, ALLOCATING.getValue()));
    }

    @Override
    public void allocatedTask(IovSubscribeTask task, String serverName) {
        task.setServerName(serverName).setState(ALLOCATED.getValue());
        baseMapper.updateById(task);

        log.info("Task {} has been allocated!", JSONObject.toJSONString(task));
    }

    @Override
    public List<IovSubscribeTask> listCurrentNodeAllocatedTask() {
        LambdaQueryWrapper<IovSubscribeTask> queryWrapper = new QueryWrapper<IovSubscribeTask>().lambda()
                .eq(IovSubscribeTask::getServerName, TtsContext.getNodeServerName())
                .eq(IovSubscribeTask::getState, ALLOCATED.getValue());

        return baseMapper.selectList(queryWrapper);
    }

    @Override
    public void runningTask(List<Long> ids) {
        if (CollectionUtils.isNotEmpty(ids)) {
            LambdaUpdateWrapper<IovSubscribeTask> updateWrapper = new UpdateWrapper<IovSubscribeTask>().lambda()
                    .set(IovSubscribeTask::getState, RUNNING.getValue())
                    .in(IovSubscribeTask::getId, ids);
            baseMapper.update(null, updateWrapper);

            log.info("{} Task Is Running!", JSONObject.toJSONString(ids));
        }
    }

    @Override
    public List<IovSubscribeTask> listCurrentNodeAllocatedAndRunningTask() {
        LambdaQueryWrapper<IovSubscribeTask> queryWrapper = new QueryWrapper<IovSubscribeTask>().lambda()
                .eq(IovSubscribeTask::getServerName, TtsContext.getNodeServerName())
                .in(IovSubscribeTask::getState, Arrays.asList(ALLOCATED.getValue(), RUNNING.getValue()));

        return baseMapper.selectList(queryWrapper);
    }

    @Override
    public List<IovSubscribeTask> listAllocatedAndRunningTask() {
        LambdaQueryWrapper<IovSubscribeTask> queryWrapper = new QueryWrapper<IovSubscribeTask>().lambda()
                .in(IovSubscribeTask::getState, Arrays.asList(ALLOCATED.getValue(), RUNNING.getValue()));

        return baseMapper.selectList(queryWrapper);
    }
}
