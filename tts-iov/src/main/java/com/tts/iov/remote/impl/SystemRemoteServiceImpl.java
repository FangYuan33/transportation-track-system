package com.tts.iov.remote.impl;

import com.tts.common.utils.spring.BeanUtils;
import com.tts.gps.dto.GpsCoordinatePointResultDto;
import com.tts.gps.dto.GpsVehicleQueryDto;
import com.tts.gps.enums.IovTypeEnums;
import com.tts.iov.domain.IovConfig;
import com.tts.gps.GpsService;
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
    @Autowired
    private GpsService gpsService;

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
    public boolean saveOrUpdateVehicleTask(IovSubscribeTaskVehicleDto taskVehicleDto) {
        return iovSubscribeTaskVehicleService.saveOrUpdateVehicleTask(taskVehicleDto);
    }

    @Override
    public boolean removeVehicleTask(IovSubscribeTaskVehicleDto taskVehicleDto) {
        return iovSubscribeTaskVehicleService.removeVehicleTask(taskVehicleDto);
    }

    @Override
    public List<CoordinatePointResultDto> queryIovVehicleLastLocationDirectly(IovVehicleQueryDto vehicleQueryDto) {
        // 入参类型转换
        GpsVehicleQueryDto gpsVehicleQueryDto = BeanUtils.copyProperties2(vehicleQueryDto, GpsVehicleQueryDto.class);
        gpsVehicleQueryDto.setIovTypeEnum(IovTypeEnums.parse(vehicleQueryDto.getIovTypeEnum().getValue()));

        List<GpsCoordinatePointResultDto> result = gpsService.queryIovVehicleLastLocationDirectly(gpsVehicleQueryDto);

        // 出参类型转换
        return BeanUtils.copyList(result, CoordinatePointResultDto.class);
    }

    @Override
    public List<CoordinatePointResultDto> queryIovVehicleTrackDirectly(IovVehicleQueryDto vehicleQueryDto) {
        // 入参类型转换
        GpsVehicleQueryDto gpsVehicleQueryDto = BeanUtils.copyProperties2(vehicleQueryDto, GpsVehicleQueryDto.class);
        gpsVehicleQueryDto.setIovTypeEnum(IovTypeEnums.parse(vehicleQueryDto.getIovTypeEnum().getValue()));

        List<GpsCoordinatePointResultDto> result = gpsService.queryIovVehicleTrackDirectly(gpsVehicleQueryDto);

        // 出参类型转换
        return BeanUtils.copyList(result, CoordinatePointResultDto.class);
    }
}
