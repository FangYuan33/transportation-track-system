package com.tts.main.domain;

import com.tts.framework.common.domain.SimpleBaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * iov 订阅任务 运行日志表
 *
 * @author FangYuan
 * @since 2022-12-22 16:22:09
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class TtsIovSubscribeTaskLog extends SimpleBaseEntity {
    private static final long serialVersionUID = 1L;

    private Long id;
    /**
     * 订阅任务id
     */
    private Long taskId;
    /**
     * 日志内容
     */
    private String content;
    /**
     * 发生事件之前的任务状态
     */
    private Integer preState;
    /**
     * 发生事件之后的任务状态
     */
    private Integer nextState;
    /**
     * 事件类型 :0: 异常事件 1: 正常事件
     */
    private Integer eventType;
    /**
     * 事件操作类型 :0: 手动 1: 系统自动
     */
    private Integer operType;
    /**
     * 发生事件的机器ip
     */
    private String serverIp;
    /**
     * 事件发生时间
     */
    private LocalDateTime createTime;

}
