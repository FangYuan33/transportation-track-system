package com.tts.main.domain;


import com.tts.framework.common.domain.SimpleBaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;


/**
 * 操作全局配置表 tts_global_configure
 * <p>
 * tts 的 全局配置
 * 这里需要注意全局配置属于初始化信息,只有在系统启动的时候才会加载使用
 * 所以如果需要 全局配置更改生效的话需要重启机器
 *
 * @author FangYuan
 * @since 2021-08-23
 */

@EqualsAndHashCode(callSuper = true)
@Data
public class TtsGlobalConfigure extends SimpleBaseEntity {
    private static final long serialVersionUID = 1L;
    /***
     * 主键id
     */
    private Long globalId;

    /***
     * configKey 全局配置 key
     */
    private String configKey;

    /***
     * configValue 全局配置 value
     */
    private Integer configValue;

    public Long getGlobalId() {
        return globalId;
    }

    public void setGlobalId(Long globalId) {
        this.globalId = globalId;
    }

    public String getConfigKey() {
        return configKey;
    }

    public void setConfigKey(String configKey) {
        this.configKey = configKey;
    }

    public Integer getConfigValue() {
        return configValue;
    }

    public void setConfigValue(Integer configValue) {
        this.configValue = configValue;
    }

    @Override
    public String toString() {
        return "TtsGlobalConfigure{" +
                "globalId=" + globalId +
                ", configKey='" + configKey + '\'' +
                ", configValue='" + configValue + '\'' +
                '}';
    }
}
