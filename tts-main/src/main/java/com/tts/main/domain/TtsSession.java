package com.tts.main.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.tts.framework.common.domain.SimpleBaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;
import java.util.Date;

/**
 * app上传的会话id
 *
 * @author FangYuan
 * @since 2022-12-22 20:00:36
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class TtsSession extends SimpleBaseEntity {

    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = -3260365560386230565L;


      /*  ****************
           业务标识信息
           不一定每个都有值
    **************************  */

    /**
     * 主键id
     */
    @TableId(value = "session_id", type = IdType.AUTO)
    private Long sessionId;

    /**
     * 租户id,陕煤这种本地化的就是一个常量
     */
    private Long tenantId;

    /**
     * 手机型号
     */
    private String phoneModel;

    /**
     * 手机操作系统
     */
    private String phoneSystem;

    /**
     * 手机运营商 ,移动,联通...
     */
    private String telecomCarrier;

    /**
     * 手机网络 ,4g,5g...
     */
    private String network;

    /**
     * app类型
     */
    private String appName;

    /**
     * app版本
     */
    private String appVersion;

    /**
     * 设备唯一标识
     */
    private String deviceUuid;

    /**
     * 司机id
     */
    private Long driverId;


    /**
     * 订单id
     */
    private Long orderId;

    /**
     * 车辆id
     */
    private String vehicleId;

    /**
     * 装车单id 列表
     */
    private String shipmentIdArray;

    /**
     * 令牌
     */
    private String token;

    /**
     * 坐标上传手机时间
     */
    private Date uploadTime;

    /**
     * 服务器当前时间
     */
    private LocalDateTime createTime;
}
