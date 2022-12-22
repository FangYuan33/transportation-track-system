package com.tts.main.domain;

import com.tts.framework.common.domain.SimpleBaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;


/**
 * iov 的 订阅任务
 *
 * @author FangYuan
 * @since 2022-12-22 15:51:24
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class TtsIovSubscribeTask extends SimpleBaseEntity {
    private static final long serialVersionUID = 1L;

    private Long id;
    /**
     * iov配置id
     */
    private Long iovConfigId;
    /**
     * 任务当前运行的服务器ip
     */
    private String serverIp;
    /**
     * 任务运行的状态  0:已启动待分配 1: 运行中 2:异常中断 3:正常停止
     */
    private Integer state;
    /**
     * 创建时间
     */
    private LocalDateTime createTime;
    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
}