package com.tts.main.service;


import com.tts.main.domain.TtsGlobalConfigure;

/**
 * tts  全局配置  服务层
 *
 * @author FangYuan
 * @since 2021-8-23
 */
public interface ITtsGlobalConfigureService {

    /**
     * 初始化配置信息
     */
    void init();

    /**
     * 根据全局configKey查询  对应的配置信息
     *
     * @param configKey 全局配置 key
     * @return 操作全局配置对象
     */
    TtsGlobalConfigure selectByConfigKey(String configKey);


    /**
     * 获取全局Redis开关配置
     * 如果返回true 表示开启 redis
     * 如果返回false 表示关闭 redis
     *
     * @return
     */
    boolean getGlobalRedisSwitch();

    /**
     * 获取全局IOV开关配置
     * 如果返回true 表示开启
     * 如果返回false 表示关闭
     *
     * @return
     */
    boolean getGlobalIovSwitch();

    /**
     * 获取全局IOV开关配置
     * 如果返回true 表示开启
     * 如果返回false 表示关闭
     *
     * @return
     */
    boolean getGlobalRemoteSwitch();

}
