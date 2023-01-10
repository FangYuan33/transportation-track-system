package com.tts.iov.gps;

import com.tts.common.exception.ServiceException;
import com.tts.common.utils.spring.SpringUtils;
import com.tts.gps.dto.GpsCoordinatePointResultDto;
import com.tts.gps.dto.GpsVehicleQueryDto;
import com.tts.gps.enums.IovTypeEnums;
import com.tts.gps.service.IovGps;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
public class GpsService implements InitializingBean {

    /**
     * key: beanName value: service
     */
    private Map<String, IovGps> gpsServiceMap;

    @Override
    public void afterPropertiesSet() {
        // 将所有的GPS类型bean封装起来，根据策略模式按需取
        gpsServiceMap = SpringUtils.getBeansOfType(IovGps.class);
    }

    public List<GpsCoordinatePointResultDto> queryIovVehicleLastLocationDirectly(GpsVehicleQueryDto vehicleQueryDto) {
        return getSpecificService(vehicleQueryDto.getIovTypeEnum()).queryIovVehicleLastLocationDirectly(vehicleQueryDto);
    }

    public List<GpsCoordinatePointResultDto> queryIovVehicleTrackDirectly(GpsVehicleQueryDto vehicleQueryDto) {
        return getSpecificService(vehicleQueryDto.getIovTypeEnum()).queryIovVehicleTrackDirectly(vehicleQueryDto);
    }

    /**
     * 获取具体业务类型的服务对象
     */
    public IovGps getSpecificService(IovTypeEnums iovTypeEnum) {
        if (iovTypeEnum != null) {
            Set<String> keySet = gpsServiceMap.keySet();
            for (String beanName : keySet) {
                if (beanName.contains(iovTypeEnum.getValue())) {
                    return gpsServiceMap.get(beanName);
                }
            }

            throw new ServiceException("不支持" + iovTypeEnum.getValue() + "类型查询");
        }

        throw new ServiceException("参数异常");
    }
}
