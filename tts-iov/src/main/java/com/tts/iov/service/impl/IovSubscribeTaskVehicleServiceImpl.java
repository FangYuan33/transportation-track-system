package com.tts.iov.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tts.common.exception.ServiceException;
import com.tts.iov.dao.IovSubscribeTaskVehicleMapper;
import com.tts.iov.domain.IovConfig;
import com.tts.iov.domain.IovSubscribeTask;
import com.tts.iov.domain.IovSubscribeTaskVehicle;
import com.tts.iov.service.IovConfigService;
import com.tts.iov.service.IovSubscribeTaskService;
import com.tts.iov.service.IovSubscribeTaskVehicleService;
import com.tts.remote.dto.IovSubscribeTaskVehicleDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class IovSubscribeTaskVehicleServiceImpl extends ServiceImpl<IovSubscribeTaskVehicleMapper, IovSubscribeTaskVehicle>
        implements IovSubscribeTaskVehicleService {

    @Autowired
    private IovConfigService iovConfigService;
    @Autowired
    private IovSubscribeTaskService iovSubscribeTaskService;

    @Override
    public boolean addVehicleTask(IovSubscribeTaskVehicleDto taskVehicleDto) {
        return processAddOrDelete(taskVehicleDto, true);
    }

    @Override
    public boolean removeVehicleTask(IovSubscribeTaskVehicleDto taskVehicleDto) {
        return processAddOrDelete(taskVehicleDto, false);
    }

    /**
     * 执行增加或删除逻辑
     *
     * @param isAdd 是否是增加逻辑
     */
    private boolean processAddOrDelete(IovSubscribeTaskVehicleDto taskVehicleDto, boolean isAdd) {
        IovConfig iovConfig = iovConfigService.getByIovType(taskVehicleDto.getIovType());
        if (iovConfig == null) {
            throw new ServiceException("TTS Iov Config [" + taskVehicleDto.getIovType() + "] Not Found!");
        }

        IovSubscribeTask subscribeTask = iovSubscribeTaskService
                .selectByIovConfigIdAndCarrierCode(iovConfig.getId(), taskVehicleDto.getCarrierCode());
        if (subscribeTask == null) {
            throw new ServiceException("TTS Iov Subscribe Task Carrier Code [" + taskVehicleDto.getCarrierCode()
                    + "] [" + taskVehicleDto.getIovType() + "] Not Found!");
        }

        // 处理业务逻辑
        if (isAdd) {
            return processAddVehicleTask(subscribeTask, taskVehicleDto.getVehicleNo());
        } else {
            return processDeleteVehicleTask(subscribeTask, taskVehicleDto.getVehicleNo());
        }
    }

    /**
     * 处理新增车辆任务逻辑
     */
    private boolean processAddVehicleTask(IovSubscribeTask subscribeTask, String vehicleNo) {
        IovSubscribeTaskVehicle taskVehicle = selectByTaskIdAndVehicleNo(subscribeTask.getId(), vehicleNo);
        if (taskVehicle == null) {
            taskVehicle = new IovSubscribeTaskVehicle(subscribeTask.getId(), vehicleNo);
            baseMapper.insert(taskVehicle);

            log.info("Carrier: {} Start Task Vehicle: {}", subscribeTask.getCarrierCode(), JSONObject.toJSONString(taskVehicle));
            return true;
        }

        return false;
    }

    private IovSubscribeTaskVehicle selectByTaskIdAndVehicleNo(Long taskId, String vehicleNo) {
        LambdaQueryWrapper<IovSubscribeTaskVehicle> queryWrapper = new QueryWrapper<IovSubscribeTaskVehicle>()
                .lambda().eq(IovSubscribeTaskVehicle::getTaskId, taskId)
                .eq(IovSubscribeTaskVehicle::getVehicleNo, vehicleNo);

        return baseMapper.selectOne(queryWrapper);
    }

    /**
     * 处理删除车辆任务逻辑
     */
    private boolean processDeleteVehicleTask(IovSubscribeTask subscribeTask, String vehicleNo) {
        LambdaUpdateWrapper<IovSubscribeTaskVehicle> deleteWrapper = new UpdateWrapper<IovSubscribeTaskVehicle>()
                .lambda().eq(IovSubscribeTaskVehicle::getTaskId, subscribeTask.getId())
                .eq(IovSubscribeTaskVehicle::getVehicleNo, vehicleNo);
        int result = baseMapper.delete(deleteWrapper);

        if (result == 1) {
            log.info("Carrier: {} Delete Task Vehicle: {}", subscribeTask.getCarrierCode(), vehicleNo);
            return true;
        } else {
            return false;
        }
    }
}
