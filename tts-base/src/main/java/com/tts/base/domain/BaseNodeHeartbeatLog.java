package com.tts.base.domain;

import com.tts.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 服务节点心跳日志实体类
 *
 * @author FangYuan
 * @since 2023-01-03 19:44:10
 */
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Data
public class BaseNodeHeartbeatLog extends BaseEntity {

    /**
     * 服务名称
     */
    private String serviceName;

    public BaseNodeHeartbeatLog(String serviceName) {
        this.serviceName = serviceName;
    }
}
