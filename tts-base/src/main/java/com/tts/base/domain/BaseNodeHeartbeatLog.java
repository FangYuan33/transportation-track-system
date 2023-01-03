package com.tts.base.domain;

import com.tts.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 服务节点心跳日志实体类
 *
 * @author FangYuan
 * @since 2023-01-03 19:44:10
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class BaseNodeHeartbeatLog extends BaseEntity {

    /**
     * 服务名称
     */
    private String serviceName;

}
