package com.tts.iov.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.tts.iov.domain.IovConfig;

/**
 * iov 配置服务层
 *
 * @author FangYuan
 * @since 2023-01-04 14:05:41
 */
public interface IovConfigService extends IService<IovConfig> {

    /**
     * 初始化Iov配置信息
     */
    void initialIovConfig();

    /**
     * 根据类型查询iov type
     */
    IovConfig getByIovType(String iovType);

    /**
     * 新增or修改Iov Config
     */
    boolean saveOrUpdateIovConfig(IovConfig iovConfig);
}
