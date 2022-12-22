package com.tts.base.domain;

import com.tts.framework.common.domain.SimpleBaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * 节点心跳最新时间
 *
 * @author FangYuan
 * @since 2022-12-22 11:35:04
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class BaseNodeHeartbeat extends SimpleBaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 主键,也是服务器ip
     */
    private String serverIp;

    /**
     * 最新 心跳时间
     */
    private LocalDateTime latestHeartbeatTime;

}

