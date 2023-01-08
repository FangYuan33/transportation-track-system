package com.tts.iov.facade;

import com.tts.common.exception.ServiceException;
import com.tts.common.utils.spring.SpringUtils;
import com.tts.facade.dto.FacadeCoordinatePointResultDto;
import com.tts.facade.dto.FacadeVehicleQueryDto;
import com.tts.facade.enums.IovTypeEnums;
import com.tts.facade.service.IovFacade;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
public class FacadeService implements InitializingBean {

    /**
     * key: beanName value: service
     */
    private Map<String, IovFacade> facadeMap;

    @Override
    public void afterPropertiesSet() {
        // 将所有的GPS类型bean封装起来，根据策略模式按需取
        facadeMap = SpringUtils.getBeansOfType(IovFacade.class);
    }

    public List<FacadeCoordinatePointResultDto> queryIovVehicleLastLocationDirectly(FacadeVehicleQueryDto vehicleQueryDto) {
        return getSpecificService(vehicleQueryDto.getIovTypeEnum()).queryIovVehicleLastLocationDirectly(vehicleQueryDto);
    }

    public List<FacadeCoordinatePointResultDto> queryIovVehicleTrackDirectly(FacadeVehicleQueryDto vehicleQueryDto) {
        return getSpecificService(vehicleQueryDto.getIovTypeEnum()).queryIovVehicleTrackDirectly(vehicleQueryDto);
    }

    /**
     * 获取具体业务类型的服务对象
     */
    public IovFacade getSpecificService(IovTypeEnums iovTypeEnum) {
        if (iovTypeEnum != null) {
            Set<String> keySet = facadeMap.keySet();
            for (String beanName : keySet) {
                if (beanName.contains(iovTypeEnum.getValue())) {
                    return facadeMap.get(beanName);
                }
            }

            throw new ServiceException("不支持" + iovTypeEnum.getValue() + "类型查询");
        }

        throw new ServiceException("参数异常");
    }
}
