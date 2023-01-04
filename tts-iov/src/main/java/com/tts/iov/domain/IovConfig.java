package com.tts.iov.domain;

import com.tts.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * IOV Config 实体类
 *
 * @author FangYuan
 * @since 2023-01-04 13:54:59
 */
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Data
public class IovConfig extends BaseEntity {

    public IovConfig(String iovType, String configInfo) {
        this.iovType = iovType;
        this.configInfo = configInfo;
    }

    /**
     * 中交兴路、G7
     */
    private String iovType;

    /**
     * 加密后的json字符串格式的iov gps设备配置信息
     */
    private String configInfo;
}
