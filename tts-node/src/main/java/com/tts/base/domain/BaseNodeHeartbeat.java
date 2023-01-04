package com.tts.base.domain;

import com.tts.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * 服务节点心跳实体类
 *
 * @author FangYuan
 * @since 2023-01-03 19:44:10
 */
@NoArgsConstructor
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
@Data
public class BaseNodeHeartbeat extends BaseEntity {

    public BaseNodeHeartbeat(String serviceName, LocalDateTime latestHeartBeatTime) {
        this.serverName = serviceName;
        this.latestHeartbeatTime = latestHeartBeatTime;
    }

    /**
     * 服务名称
     */
    private String serverName;

    /**
     * 最新心跳时间
     */
    private LocalDateTime latestHeartbeatTime;
}
