package com.tts.main.service.impl;

import com.tts.base.constant.TtsGlobalConfigureKeyConstants;
import com.tts.framework.common.constant.StatusEnum;
import com.tts.main.dao.TtsGlobalConfigureMapper;
import com.tts.main.domain.TtsGlobalConfigure;
import com.tts.main.service.ITtsGlobalConfigureService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 全局配置 服务层处理
 *
 * @author ruoyi
 */
@Service
public class TtsGlobalConfigureImpl implements ITtsGlobalConfigureService {

     @Resource
     private TtsGlobalConfigureMapper ttsGlobalConfigureMapper;

     //redis 全局开关
     private boolean globalRedisSwitch = false;

     //iov 全局开关
    private boolean globalIovSwitch = false;

    // remote 全局开关

    private boolean globalRemoteSwitch = false;

    @Override
    public void init() {
        this.globalRedisSwitch = getGlobalRedisSwitchInner();
        this.globalIovSwitch = getGlobalIovSwitchInner();
        this.globalRemoteSwitch = getGlobalRemoteSwitchInner();
    }


    /**
     * 返回全局redis开关
     * @return
     */
    @Override
    public boolean getGlobalRedisSwitch() {

        return globalRedisSwitch;
    }

    /**
     * 返回全局Iov开关
     * @return
     */
    @Override
    public boolean getGlobalIovSwitch() {

        return globalIovSwitch;
    }

    /**
     * 返回全局Iov开关
     * @return
     */
    @Override
    public boolean getGlobalRemoteSwitch() {

        return globalRemoteSwitch;
    }

    /**
     * 返回全局redis开关
     * @return
     */
    public boolean getGlobalRedisSwitchInner() {

        TtsGlobalConfigure globalRedisConfigure = this.selectByConfigKey(TtsGlobalConfigureKeyConstants.GLOBAL_REDIS_SWITCH);

        boolean result = false;

        if(globalRedisConfigure != null
                && globalRedisConfigure.getConfigValue().equals(StatusEnum.ENABLE.getValue())){
            result = true;
        }

        return result;
    }

    /**
     * 返回全局Iov开关
     * @return
     */
    public boolean getGlobalIovSwitchInner() {

        TtsGlobalConfigure globalIovConfigure = this.selectByConfigKey(TtsGlobalConfigureKeyConstants.GLOBAL_IOV_SWITCH);

        boolean result = false;

        if(globalIovConfigure != null
                && globalIovConfigure.getConfigValue().equals(StatusEnum.ENABLE.getValue())){
            result = true;
        }

        return result;
    }

    /**
     * 返回全局日志开关
     * @return
     */
    public boolean getGlobalRemoteSwitchInner() {

        TtsGlobalConfigure globalIovConfigure = this.selectByConfigKey(TtsGlobalConfigureKeyConstants.GLOBAL_REMOTE_SWITCH);

        boolean result = false;

        if(globalIovConfigure != null
                   && globalIovConfigure.getConfigValue().equals(StatusEnum.ENABLE.getValue())){
            result = true;
        }

        return result;
    }




    /**
     * 根据全局configKey查询 全局 dubbo 日志流水的 开关
     *
     * @param configKey 全局配置 key
     * @return 操作全局配置对象
     */
    @Override
    public TtsGlobalConfigure selectByConfigKey(String configKey) {
        return ttsGlobalConfigureMapper.selectByConfigKey(configKey);
    }
}
