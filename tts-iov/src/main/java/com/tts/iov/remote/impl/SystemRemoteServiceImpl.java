package com.tts.iov.remote.impl;

import com.tts.iov.domain.IovConfig;
import com.tts.iov.service.IovConfigService;
import com.tts.iov.service.IovSubscribeTaskService;
import com.tts.iov.service.IovSubscribeTaskVehicleService;
import com.tts.remote.dto.CoordinatePointResultDto;
import com.tts.remote.dto.IovConfigDto;
import com.tts.remote.dto.IovSubscribeTaskVehicleDto;
import com.tts.remote.dto.IovVehicleQueryDto;
import com.tts.remote.service.SystemRemoteService;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@DubboService
public class SystemRemoteServiceImpl implements SystemRemoteService {

    @Autowired
    private IovConfigService iovConfigService;
    @Autowired
    private IovSubscribeTaskService iovSubscribeTaskService;
    @Autowired
    private IovSubscribeTaskVehicleService iovSubscribeTaskVehicleService;

    @Override
    public boolean saveOrUpdateIovConfig(IovConfigDto iovConfig) {
        return iovConfigService.saveOrUpdateIovConfig(new IovConfig(iovConfig.getIovType(), iovConfig.getConfigInfo()));
    }

    @Override
    public boolean startSubscribeTask(String carrierCode, String iovType) {
        return iovSubscribeTaskService.startSubscribeTask(carrierCode, iovType);
    }

    @Override
    public boolean stopSubscribeTask(String carrierCode, String iovType) {
        return iovSubscribeTaskService.stopSubscribeTask(carrierCode, iovType);
    }

    @Override
    public boolean addVehicleTask(IovSubscribeTaskVehicleDto taskVehicleDto) {
        return iovSubscribeTaskVehicleService.addVehicleTask(taskVehicleDto);
    }

    @Override
    public boolean removeVehicleTask(IovSubscribeTaskVehicleDto taskVehicleDto) {
        return iovSubscribeTaskVehicleService.removeVehicleTask(taskVehicleDto);
    }

    @Override
    public List<CoordinatePointResultDto> queryIovVehicleLastLocationDirectly(IovVehicleQueryDto vehicleQueryDto) {
        return null;
    }

    @Override
    public List<CoordinatePointResultDto> queryIovVehicleTrackDirectly(IovVehicleQueryDto vehicleQueryDto) {
        return null;
    }
}
