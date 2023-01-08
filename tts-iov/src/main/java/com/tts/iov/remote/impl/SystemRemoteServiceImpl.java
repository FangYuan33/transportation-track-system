package com.tts.iov.remote.impl;

import com.tts.common.exception.ServiceException;
import com.tts.common.utils.spring.SpringUtils;
import com.tts.iov.domain.IovConfig;
import com.tts.iov.facade.IovFacade;
import com.tts.iov.service.IovConfigService;
import com.tts.iov.service.IovSubscribeTaskService;
import com.tts.iov.service.IovSubscribeTaskVehicleService;
import com.tts.remote.dto.CoordinatePointResultDto;
import com.tts.remote.dto.IovConfigDto;
import com.tts.remote.dto.IovSubscribeTaskVehicleDto;
import com.tts.remote.dto.IovVehicleQueryDto;
import com.tts.remote.enums.IovTypeEnums;
import com.tts.remote.service.SystemRemoteService;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
@DubboService
public class SystemRemoteServiceImpl implements SystemRemoteService, InitializingBean {

    @Autowired
    private IovConfigService iovConfigService;
    @Autowired
    private IovSubscribeTaskService iovSubscribeTaskService;
    @Autowired
    private IovSubscribeTaskVehicleService iovSubscribeTaskVehicleService;

    /**
     * key: beanName value: service
     */
    private Map<String, IovFacade> facadeMap;

    @Override
    public void afterPropertiesSet() {
        facadeMap = SpringUtils.getBeansOfType(IovFacade.class);
    }

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
        return getSpecificService(vehicleQueryDto.getIovTypeEnum()).queryIovVehicleLastLocationDirectly(vehicleQueryDto);
    }

    @Override
    public List<CoordinatePointResultDto> queryIovVehicleTrackDirectly(IovVehicleQueryDto vehicleQueryDto) {
        return getSpecificService(vehicleQueryDto.getIovTypeEnum()).queryIovVehicleTrackDirectly(vehicleQueryDto);
    }

    /**
     * 获取具体业务类型的服务对象
     */
    private IovFacade getSpecificService(IovTypeEnums iovTypeEnum) {
        Set<String> keySet = facadeMap.keySet();
        for (String beanName : keySet) {
            if (beanName.contains(iovTypeEnum.getValue())) {
                return facadeMap.get(beanName);
            }
        }

        throw new ServiceException("不支持" + iovTypeEnum.getValue() + "类型查询");
    }
}
