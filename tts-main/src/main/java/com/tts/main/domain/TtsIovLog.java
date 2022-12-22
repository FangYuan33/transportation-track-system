package com.tts.main.domain;


import com.tts.framework.common.domain.SimpleBaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;
import java.util.Date;

/**
 * tts 的 iov  运行日志
 *
 * @author FangYuan
 * @since 2022-12-22 17:32:06
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class TtsIovLog extends SimpleBaseEntity {
    private static final long serialVersionUID = 1L;

    private Long id;
    private Long taskId;
    private String request;
    private Integer requestSize;
    private String response;
    private Integer responseSize;
    private Integer status;
    private Long timeConsuming;
    private LocalDateTime createTime;

}
