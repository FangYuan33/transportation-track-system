package com.tts.iov.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tts.common.exception.ServiceException;
import com.tts.common.utils.DESUtil;
import com.tts.gps.enums.IovTypeEnums;
import com.tts.gps.service.IovGps;
import com.tts.iov.dao.IovConfigMapper;
import com.tts.iov.domain.IovConfig;
import com.tts.iov.gps.GpsService;
import com.tts.iov.service.IovConfigService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Enumeration;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Service
public class IovConfigServiceImpl extends ServiceImpl<IovConfigMapper, IovConfig> implements IovConfigService {

    /**
     * iov config 本地缓存 key:iovType, value:configInfo(已解密)
     */
    private final ConcurrentHashMap<String, String> localCache = new ConcurrentHashMap<>(4);

    @Autowired
    private GpsService gpsService;

    @Override
    public void initialIovConfig() {
        // 缓存
        cacheIovConfig();
        // 初始化各个不同GPS Service设备的配置信息
        initialIovTypeConfigInfo();
    }

    /**
     * 将Iov config 缓存
     */
    private void cacheIovConfig() {
        try {
            List<IovConfig> iovConfigs = baseMapper.selectList(new QueryWrapper<>());

            for (IovConfig iovConfig : iovConfigs) {
                iovConfig.setConfigInfo(DESUtil.decrypt(iovConfig.getConfigInfo()));
                localCache.put(iovConfig.getIovType(), JSONObject.toJSONString(iovConfig));
            }
        } catch (Exception e) {
            log.error("Initial Iov Config Error", e);
        }
    }

    /**
     * 初始化各个不同GPS Service设备的配置信息
     */
    @SuppressWarnings("unchecked")
    private void initialIovTypeConfigInfo() {
        Enumeration<String> keys = localCache.keys();

        if (keys.hasMoreElements()) {
            String iovType = keys.nextElement();
            try {
                IovConfig iovConfig = JSONObject.parseObject(localCache.get(iovType), IovConfig.class);

                IovGps specificService = gpsService.getSpecificService(IovTypeEnums.parse(iovType));
                specificService.initialConfigInfo(JSONObject.parseObject(iovConfig.getConfigInfo(), Map.class));
            } catch (Exception e) {
                log.error("Initial IovType [" + iovType + "] Config Info Error", e);
            }
        }
    }

    @Override
    public IovConfig getByIovType(String iovType) {
        String configInfo = localCache.get(iovType);
        if (configInfo != null) {
            return JSONObject.parseObject(configInfo, IovConfig.class);
        } else {
            try {
                IovConfig iovConfig = baseMapper.selectOne(new QueryWrapper<IovConfig>().lambda().eq(IovConfig::getIovType, iovType));
                if (iovConfig != null) {
                    // 解密
                    iovConfig.setConfigInfo(DESUtil.decrypt(iovConfig.getConfigInfo()));
                    // 加入本地缓存
                    localCache.put(iovType, JSONObject.toJSONString(iovConfig));

                    return iovConfig;
                }
            } catch (Exception e) {
                log.error("Get Iov Config Error", e);
            }

            return null;
        }
    }

    @Override
    public boolean saveOrUpdateIovConfig(IovConfig iovConfig) {
        // 校验参数
        checkUpdateIovConfigInfo(iovConfig);

        try {
            iovConfig.setConfigInfo(DESUtil.encrypt(iovConfig.getConfigInfo()));
            String iovConfigInfo = localCache.get(iovConfig.getIovType());
            if (iovConfigInfo != null) {
                // 更新数据库
                LambdaUpdateWrapper<IovConfig> updateWrapper = new UpdateWrapper<IovConfig>().lambda()
                        .set(IovConfig::getConfigInfo, iovConfig.getConfigInfo())
                        .eq(IovConfig::getIovType, iovConfig.getIovType());
                baseMapper.update(null, updateWrapper);
            } else {
                // 存库
                baseMapper.insert(iovConfig);
            }
            // 更新缓存
            localCache.put(iovConfig.getIovType(), JSONObject.toJSONString(iovConfig));
        } catch (Exception e) {
            log.error("Save or Update Iov Config Error", e);
            return false;
        }

        return true;
    }

    /**
     * 校验iov config更新的参数
     */
    private void checkUpdateIovConfigInfo(IovConfig iovConfig) {
        if (iovConfig.getConfigInfo() == null || iovConfig.getIovType() == null) {
            throw new ServiceException("参数异常");
        }
    }
}
