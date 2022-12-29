package com.tts.base.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 服务器状态变化日志表实体
 *
 * @author FangYuan
 * @since 2022-12-29 16:38:15
 */
@Data
public class BaseServerStateLog {

    @TableId(type = IdType.AUTO)
    private Long id;

    private String serverName;

    private String state;

    private LocalDateTime createTime;
}
