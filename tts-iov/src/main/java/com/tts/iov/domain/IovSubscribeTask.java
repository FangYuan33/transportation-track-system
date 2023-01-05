package com.tts.iov.domain;

import com.tts.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * iov 订阅任务实体类
 *
 * @author FangYuan
 * @since 2023-01-05 11:35:41
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class IovSubscribeTask extends BaseEntity {

    /**
     * iov配置ID
     */
    private Long iovConfigId;

    /**
     * 任务所运行的服务名
     */
    private String serverName;

    /**
     * 任务状态: 10: 已启动待分配 20: 运行中 30: 异常中断 40: 正常停止
     */
    private Integer state;

}
