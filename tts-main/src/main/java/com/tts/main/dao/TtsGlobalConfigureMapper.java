package com.tts.main.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tts.main.domain.TtsGlobalConfigure;


/**
 * 全局配置表 数据层
 *
 * @author FangYuan
 * @since 2021-02-01
 */
public interface TtsGlobalConfigureMapper extends BaseMapper<TtsGlobalConfigure> {

    /**
     * 根据全局configKey查询 全局 dubbo 日志流水的 开关
     *
     * @param configKey 全局配置 key
     * @return 操作全局配置对象
     */
    TtsGlobalConfigure selectByConfigKey(String configKey);

}
