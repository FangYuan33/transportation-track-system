package com.tts.base.domain;

import com.tts.framework.common.domain.SimpleBaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * 节点心跳日志信息
 *
 * @author FangYuan
 * @since 2022-12-22 13:58:08
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class BaseNodeHeartbeatLog extends SimpleBaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    private Long id;

    /**
     * 服务器ip
     */
    private String serverIp;

    /**
     * 心跳时间
     */
    private Date heartbeatTime;

}

