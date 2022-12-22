package com.tts.main.utils;

import com.alibaba.fastjson.JSONObject;
import com.tts.base.constant.TtsConstant;
import com.tts.iov.dto.IovVehicleInputDto;
import com.tts.iov.dto.IovVehiclePointResultDto;
import com.tts.main.domain.TtsIovConfig;
import com.tts.main.domain.TtsIovSubscribeTaskVehicle;
import com.tts.main.domain.TtsIovSubscribeTaskVehicleLog;
import com.tts.remote.app.dto.AppCoordinatePointDto;
import com.tts.remote.constant.CoordinateTypeEnum;
import com.tts.remote.dto.CoordinatePointResultDto;
import com.tts.remote.dto.TtsIovConfigDto;
import com.tts.remote.dto.TtsIovVehicleDirectQueryDto;
import com.tts.remote.dto.TtsIovVehicleDto;
import lombok.extern.slf4j.Slf4j;

import java.util.Date;

/**
 * 对象转换工具类
 *
 * @author FangYuan
 * @since 2022-12-22 15:24:11
 */
@Slf4j
public class ConvertTool {

    public static TtsIovConfig toTtsIovConfig(TtsIovConfigDto ttsIovConfigDto, TtsIovConfig ttsIovConfig) {
        if (ttsIovConfig == null) {
            ttsIovConfig = new TtsIovConfig();
        }

        ttsIovConfig.setCarrierCode(ttsIovConfigDto.getCarrierCode());
        ttsIovConfig.setIovType(ttsIovConfigDto.getIovType());

        if (ttsIovConfigDto.getConfig().size() > 0) {
            ttsIovConfig.setConfig(JSONObject.toJSONString(ttsIovConfigDto.getConfig()));
        }

        if (ttsIovConfigDto.getSubscribeInterval() != null) {
            ttsIovConfig.setSubscribeInterval(ttsIovConfigDto.getSubscribeInterval());
        }

        if (ttsIovConfigDto.getTimeNearBy() != null) {
            ttsIovConfig.setTimeNearBy(ttsIovConfigDto.getTimeNearBy());
        }

        if (ttsIovConfigDto.getLogFlag() != null) {
            ttsIovConfig.setLogFlag(ttsIovConfigDto.getLogFlag());
        }


        return ttsIovConfig;
    }

    public static TtsIovSubscribeTaskVehicle toTtsIovSubscribeTaskVehicle(TtsIovVehicleDto ttsIovVehicleDto) {
        TtsIovSubscribeTaskVehicle ttsIovSubscribeTaskVehicle = new TtsIovSubscribeTaskVehicle();
        ttsIovSubscribeTaskVehicle.setVehicleId(ttsIovVehicleDto.getVehicleId());
        ttsIovSubscribeTaskVehicle.setVehicleNo(ttsIovVehicleDto.getVehicleNo());
        ttsIovSubscribeTaskVehicle.setShipmentId(ttsIovVehicleDto.getShipmentId());
        ttsIovSubscribeTaskVehicle.setVehicleColorType(ttsIovVehicleDto.getVehicleColorType());

        return ttsIovSubscribeTaskVehicle;
    }

    public static TtsIovSubscribeTaskVehicleLog toTtsIovSubscribeTaskVehicleLog(TtsIovSubscribeTaskVehicle ttsIovSubscribeTaskVehicle) {
        TtsIovSubscribeTaskVehicleLog  ttsIovSubscribeTaskVehicleLog = new TtsIovSubscribeTaskVehicleLog();

        ttsIovSubscribeTaskVehicleLog.setTaskId(ttsIovSubscribeTaskVehicle.getTaskId());
        ttsIovSubscribeTaskVehicleLog.setVehicleId(ttsIovSubscribeTaskVehicle.getVehicleId());
        ttsIovSubscribeTaskVehicleLog.setVehicleNo(ttsIovSubscribeTaskVehicle.getVehicleNo());
        ttsIovSubscribeTaskVehicleLog.setShipmentId(ttsIovSubscribeTaskVehicle.getShipmentId());
        ttsIovSubscribeTaskVehicleLog.setVehicleColorType(ttsIovSubscribeTaskVehicle.getVehicleColorType());

        return  ttsIovSubscribeTaskVehicleLog;
    }

    public static IovVehicleInputDto toIovVehicleInputDto(TtsIovVehicleDirectQueryDto iovVehicleDirectQueryDto) {
        IovVehicleInputDto iovVehicleInputDto = new IovVehicleInputDto();
        iovVehicleInputDto.setVehicleNoList(iovVehicleDirectQueryDto.getVehicleNoList());
        iovVehicleInputDto.setVehicleNoColorList(iovVehicleDirectQueryDto.getVehicleNoColorList());
        iovVehicleInputDto.setTimeNearBy(iovVehicleDirectQueryDto.getTimeNearBy());
        iovVehicleInputDto.setTimeStart(iovVehicleDirectQueryDto.getTimeStart());
        iovVehicleInputDto.setTimeEnd(iovVehicleDirectQueryDto.getTimeEnd());
        iovVehicleInputDto.setTimeInterval(iovVehicleDirectQueryDto.getTimeInterval());
        iovVehicleInputDto.setCoordinateTypeEnum(iovVehicleDirectQueryDto.getCoordinateTypeEnum());
        iovVehicleInputDto.setIsPlaceName(iovVehicleDirectQueryDto.getIsPlaceName());
        return iovVehicleInputDto;
    }

    public static CoordinatePointResultDto toCoordinatePointResultDto(IovVehiclePointResultDto iovVehiclePointResultDto) {
        CoordinatePointResultDto coordinatePointResultDto = new CoordinatePointResultDto();

        coordinatePointResultDto.setVehicleNo(iovVehiclePointResultDto.getVehicleNo());
        coordinatePointResultDto.setLatitude(iovVehiclePointResultDto.getLat());
        coordinatePointResultDto.setLongitude(iovVehiclePointResultDto.getLon());
        coordinatePointResultDto.setSpeed(iovVehiclePointResultDto.getSpd());
        coordinatePointResultDto.setDirection(iovVehiclePointResultDto.getDrc());
        coordinatePointResultDto.setAltitude(iovVehiclePointResultDto.getAltitude());
        coordinatePointResultDto.setAddress(iovVehiclePointResultDto.getAdr());
        coordinatePointResultDto.setCreateTime(new Date( Long.parseLong(iovVehiclePointResultDto.getUtc())));
        coordinatePointResultDto.setTemperature1(iovVehiclePointResultDto.getTemperature1());
        coordinatePointResultDto.setTemperature2(iovVehiclePointResultDto.getTemperature2());
        coordinatePointResultDto.setTemperature3(iovVehiclePointResultDto.getTemperature3());
        coordinatePointResultDto.setTemperature4(iovVehiclePointResultDto.getTemperature4());
        coordinatePointResultDto.setHumidity(iovVehiclePointResultDto.getHumidity());
        coordinatePointResultDto.setHumidity2(iovVehiclePointResultDto.getHumidity2());
        coordinatePointResultDto.setHumidity3(iovVehiclePointResultDto.getHumidity3());
        coordinatePointResultDto.setHumidity4(iovVehiclePointResultDto.getHumidity4());
        coordinatePointResultDto.setDeviceType(iovVehiclePointResultDto.getDeviceType());
        coordinatePointResultDto.setUploadTime(new Date(Long.parseLong(iovVehiclePointResultDto.getSensorTime())));

        return coordinatePointResultDto;
    }

    public static AppCoordinatePointDto toAppCoordinatePointDto(IovVehiclePointResultDto iovVehiclePointResultDto,
                                                                TtsIovSubscribeTaskVehicle ttsIovSubscribeTaskVehicle, String iovType) {
        AppCoordinatePointDto appCoordinatePointDto = new AppCoordinatePointDto();

        appCoordinatePointDto.setTenantId(String.valueOf(TtsConstant.DEFAULT_TENANT_ID));
        appCoordinatePointDto.setVehicleId(String.valueOf(ttsIovSubscribeTaskVehicle.getVehicleId()));
        appCoordinatePointDto.setVehicleNo(ttsIovSubscribeTaskVehicle.getVehicleNo());
        appCoordinatePointDto.setShipmentIdArray(ttsIovSubscribeTaskVehicle.getShipmentId());
        //默认使用 火星坐标 (GCJ-02)
        appCoordinatePointDto.setMapCoordinateSystem(CoordinateTypeEnum.GCJ02.getValue());
        appCoordinatePointDto.setLocationType(iovType);
        appCoordinatePointDto.setLongitude(iovVehiclePointResultDto.getLon());
        appCoordinatePointDto.setLatitude(iovVehiclePointResultDto.getLat());
        appCoordinatePointDto.setSpeed(iovVehiclePointResultDto.getSpd());
        appCoordinatePointDto.setDirection(iovVehiclePointResultDto.getSpd());
        appCoordinatePointDto.setAltitude(iovVehiclePointResultDto.getAltitude());
        //iov 数据结果没有给精确度,默认为0
        appCoordinatePointDto.setAccuracy("0");
        //默认异常编码为 正常
        appCoordinatePointDto.setErrorCode(TtsConstant.NORMAL_POINT_ERROR_CODE);
        appCoordinatePointDto.setAddress(iovVehiclePointResultDto.getAdr());
        appCoordinatePointDto.setProvince(iovVehiclePointResultDto.getProvince());
        appCoordinatePointDto.setCity(iovVehiclePointResultDto.getCity());
        appCoordinatePointDto.setCountry(iovVehiclePointResultDto.getCountry());
        appCoordinatePointDto.setTemperature1(iovVehiclePointResultDto.getTemperature1());
        appCoordinatePointDto.setTemperature2(iovVehiclePointResultDto.getTemperature2());
        appCoordinatePointDto.setTemperature3(iovVehiclePointResultDto.getTemperature3());
        appCoordinatePointDto.setTemperature4(iovVehiclePointResultDto.getTemperature4());
        appCoordinatePointDto.setPositionTime(iovVehiclePointResultDto.getUtc());
        appCoordinatePointDto.setSensorTime(iovVehiclePointResultDto.getSensorTime());
        appCoordinatePointDto.setRemark(iovVehiclePointResultDto.getRemark());

        appCoordinatePointDto.setHumidity(iovVehiclePointResultDto.getHumidity());
        appCoordinatePointDto.setHumidity2(iovVehiclePointResultDto.getHumidity2());
        appCoordinatePointDto.setHumidity3(iovVehiclePointResultDto.getHumidity3());
        appCoordinatePointDto.setHumidity4(iovVehiclePointResultDto.getHumidity4());

        return appCoordinatePointDto;
    }
}
