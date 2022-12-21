package com.tts.base.domain;

import com.tts.framework.common.domain.SimpleBaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;
import java.util.Date;


/**
 * 服务器变化日志
 *
 * @author FangYuan
 * @since 2022-12-21 20:14:47
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class BaseServerLog extends SimpleBaseEntity {

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
     * 服务器状态 ,包括 register,unregister,master,unmaster
     */
    private String status;

    /**
     * 发生时间
     */
    private LocalDateTime createTime;

}
