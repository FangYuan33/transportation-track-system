package com.tts.main.service.impl;

import com.tts.base.utils.DEStool;
import com.tts.main.dao.TtsIovConfigMapper;
import com.tts.main.domain.TtsIovConfig;
import com.tts.main.service.TtsIovConfigService;
import com.tts.main.utils.ConvertTool;
import com.tts.remote.dto.TtsIovConfigDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


/**
 * iov-承运商  配置信息 servcie 实现类
 *
 * @author FangYuan
 * @since 2022-12-22 15:04:02
 */
@Slf4j
@Service
public class TtsIovConfigServiceImpl implements TtsIovConfigService {

    public static Map<String, TtsIovConfig> ttsIovConfigMap = new ConcurrentHashMap<>();

    @Resource
    private TtsIovConfigMapper iovConfigMapper;

    @Override
    public void initIovConfig() {
        try {
            TtsIovConfig ttsIovConfig = new TtsIovConfig();
            List<TtsIovConfig> ttsIovConfigs = iovConfigMapper.selectByQueryCondition(ttsIovConfig);
            for (TtsIovConfig config : ttsIovConfigs) {
                config.setConfig(DEStool.decrypt(config.getConfig()));
                ttsIovConfigMap.put(config.getCarrierCode() + config.getIovType(), config);
            }
        } catch (Exception e) {
            log.error("TtsIovConfigServiceImpl#initIovConfig init config decrypt error" + e);
        }
    }


    /**
     * 保存 iov 的配置信息
     * 这里需要注意 每个 承运商 carrier 下只会有一个  iov 类型(SINOIOV 或者 G7)
     */
    @Override
    @Transactional
    public boolean saveOrUpdateIovConfig(TtsIovConfigDto ttsIovConfigDto) throws Exception {

        String carrierCode = ttsIovConfigDto.getCarrierCode();
        String iovType = ttsIovConfigDto.getIovType();

        //通过 carrierCode 和 iovType 去数据库查询有没有对应的配置信息
        TtsIovConfig ttsIovConfig = this.queryByCarrierCodeAndIovType(carrierCode, iovType);

        //如果之前没有记录,则插入一个新记录
        if (ttsIovConfig == null) {
            ttsIovConfig = ConvertTool.toTtsIovConfig(ttsIovConfigDto, ttsIovConfig);
            this.save(ttsIovConfig);

        }
        //如果之前有记录,则更新已有记录
        else {
            ttsIovConfig = ConvertTool.toTtsIovConfig(ttsIovConfigDto, ttsIovConfig);
            this.update(ttsIovConfig);
        }

        return true;
    }

    /**
     * 通过承运商编码和iov类型查询其配置
     */
    @Override
    public TtsIovConfig queryByCarrierCodeAndIovType(String carrierCode, String iovType) throws Exception {

        TtsIovConfig queryCondition = new TtsIovConfig();
        queryCondition.setCarrierCode(carrierCode);
        queryCondition.setIovType(iovType);

        List<TtsIovConfig> ttsIovConfigList = iovConfigMapper.selectByQueryCondition(queryCondition);

        if (ttsIovConfigList.size() == 0) {
            return null;
        } else {
            return extractEncryptIovConfig(ttsIovConfigList.get(0));
        }
    }

    @Override
    public void save(TtsIovConfig ttsIovConfig) throws Exception {
        ttsIovConfig.setUpdateTime(LocalDateTime.now());
        ttsIovConfig.setConfig(DEStool.encrypt(ttsIovConfig.getConfig()));
        this.iovConfigMapper.insert(ttsIovConfig);
    }

    @Override
    public void update(TtsIovConfig ttsIovConfig) throws Exception {
        ttsIovConfig.setUpdateTime(LocalDateTime.now());
        ttsIovConfig.setConfig(DEStool.encrypt(ttsIovConfig.getConfig()));
        this.iovConfigMapper.updateById(ttsIovConfig);
    }

    @Override
    public TtsIovConfig getById(Long iovConfigId) throws Exception {
        return extractEncryptIovConfig(this.iovConfigMapper.selectById(iovConfigId));
    }

    private TtsIovConfig extractEncryptIovConfig(TtsIovConfig ttsIovConfig) throws Exception {
        TtsIovConfig voiConfig = ttsIovConfigMap.get(ttsIovConfig.getCarrierCode() + ttsIovConfig.getIovType());
        if (null != voiConfig && ttsIovConfig.getUpdateTime().equals(voiConfig.getUpdateTime())) {
            return voiConfig;
        }
        ttsIovConfig.setConfig(DEStool.decrypt(ttsIovConfig.getConfig()));
        ttsIovConfigMap.put(ttsIovConfig.getCarrierCode() + ttsIovConfig.getIovType(), ttsIovConfig);
        return ttsIovConfig;
    }

}
