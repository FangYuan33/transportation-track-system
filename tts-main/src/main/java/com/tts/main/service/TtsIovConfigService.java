package com.tts.main.service;


import com.tts.main.domain.TtsIovConfig;
import com.tts.remote.dto.TtsIovConfigDto;

/**
 * iov-承运商 配置信息 service
 *
 * @author FangYuan
 * @since 2022-12-22 14:40:03
 */
public interface TtsIovConfigService {

    /**
     * 初始化 IOV config 解密
     */
    void initIovConfig();

    /**
     * 保存 iov 的配置信息
     * 这里需要注意 每个 承运商 carrier 下只会有一个  iov 类型(sinoiov 或者 g7)
     */
    boolean saveOrUpdateIovConfig(TtsIovConfigDto ttsIovConfigDto) throws Exception;

    /**
     * 通经承运商编码和 iov 类型
     */
    TtsIovConfig queryByCarrierCodeAndIovType(String carrierCode, String iovType) throws Exception;

    /**
     * 保存iov 配置信息
     */
    void save(TtsIovConfig ttsIovConfig) throws Exception;

    /**
     * 更新 iov 配置信息
     */
    void update(TtsIovConfig ttsIovConfig) throws Exception;

    TtsIovConfig getById(Long iovConfigId) throws Exception;
}
