package com.tts.main.domain;

import com.tts.framework.common.domain.SimpleBaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * 配置管理记录信息
 *
 * @author FangYuan
 * @since 2022-12-22 17:32:06
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class TtsConfigure extends SimpleBaseEntity {

    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = -3250365560386230566L;

    /**
     * 主键ID
     */
    private Long id;


    /**
     * 租户id
     */
    private Long tenantId;

    /**
     * 坐标定位过滤开关 ,true 为开启过滤 ,  false 为关闭过滤
     */
    private boolean filterSwitch = false;

    /**
     * 坐标定位方式过滤 内容, 即不采用列表中的定位方式,如果列表为空则表示全采用
     */
    private String locationTypeFilter;


    /**
     * 坐标过滤精度阈值 , 当开启坐标过滤的时候,只采用精度范围内的坐标点,如果为0则表示不进行过滤
     */
    private double accuracyFilterThreshold = 0;

    /**
     * 是否开启  redis 缓存坐标 , 默认为 false 不开启
     */
    private boolean redisSwitch = false;

    /**
     * redis 中 缓存坐标的 有效期时间,单位为小时,即如果这个 车ID_装车单ID 连续 n 个小时 没有坐标数据过来,则清空其在redis中的数据
     */
    private int redisExpiryHour = 1;

    /**
     * 运输里程和车速实时计算配置 开关,如果开启的话 会进行 里程实时计算 ,默认为 false 关闭
     */
    private boolean mileageComputeSwitch = false;

    /**
     * 电子围栏 自动监听开关
     */
    private boolean geofenceMonitorSwitch = false;

    /**
     * 电子围栏 监听 主动通知 开关
     */
    private boolean geofenceMonitorNotifySwitch = false;

    /**
     * 更新人名称
     */
    private String updateBy;

    /**
     * 配置项更新时间
     */
    private LocalDateTime updateTime;

}
