package com.tts.base.domain;

import com.tts.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;


/**
 * 服务器状态变化日志表实体
 *
 * @author FangYuan
 * @since 2022-12-29 16:38:15
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
public class BaseServerStateLog extends BaseEntity {

    private String serverName;

    private String state;

}
