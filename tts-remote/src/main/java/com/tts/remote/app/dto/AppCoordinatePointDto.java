package com.tts.remote.app.dto;


import lombok.Data;

import java.io.Serializable;

/**
 * app上传的单个坐标的报文
 * 这个是包含在
 *
 * @author FangYuan
 * @since 2022-12-22 17:32:06
 */
@Data
public class AppCoordinatePointDto implements Serializable {

    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = -3250365560386230565L;


      /*  ****************
           业务标识信息
           不一定每个都有值
    ************************************  */


    /**
     * 租户id,陕煤这种本地化的就是一个常量
     * 用于多租户情况
     */
    private String tenantId;


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
    private String driverId;


    /**
     * 订单id
     */
    private String orderId;

    /**
     * 车辆id 列表,多个车辆用逗号分隔
     */
    private String vehicleId;

    /**
     * 车牌号
     */
    private String vehicleNo;

    /**
     * 装车单id ,一个车可能同时会有多个装车单id, 每个装车单id 之间用逗号分隔,例如 1  1,2  1,2,3
     */
    private String shipmentIdArray;

    /**
     * 会话id
     */
    private String sessionId;




      /*  ****************
       定位的基本信息
       一般所有定位点都会包含(除了异常点之外)
    ************************************  */


    /**
     * 定位地图坐标系类别
     * 地图坐标系类型,目前存储仅支持 gcj02 , 需要坐标上传方直接提供基于 gcj02 的坐标
     */
    private String mapCoordinateSystem;


    /**
     * 定位结果来源类别:
     * 包含 GPS,基站,WIFI,蓝牙,传感器.....
     */
    private String locationType;


    /**
     * 经度
     */
    private String longitude;

    /**
     * 纬度
     */
    private String latitude;

    /**
     * 速度
     */
    private String speed;

    /**
     * 方向
     */
    private String direction;

    /**
     * 海拔高度
     */
    private String altitude;

    /**
     * 精度
     */
    private String accuracy;

    /**
     * 详细地址
     */
    private String address;

    //省
    private String province;
    //市
    private String city;
    //县
    private String country;

    /**
     * 坐标定位时间 传过来的long 时间戳
     */
    private String positionTime;

    /**
     * 坐标上传手机时间 传过来的long 时间戳
     */
    private String uploadTime;

    /**
     * 服务器接收到坐标时候的时间
     */
    private String createtime;

    /**
     * 　坐标过滤标志
     */
    private boolean accurateFlag = true;




    /*  **************************************
     * 定位的异常信息
     * 一般只有异常点会包含这部分信息
     *************************************  */

    /**
     * 定位异常编码(这个需要和app协商文档)
     */
    private String errorCode;

    /**
     * 定位异常描述
     */
    private String errorInfo;




    /*  **************************************
     * 定位的业务信息
     * 一般根据业务需求会有部分点有值
     * 举例比如部分点的所属业务操作等
     * 这个部分可能随着业务变化有增加
     *************************************  */

    /**
     * 业务动作
     */
    private String action;

    /**
     * 业务参数
     */
    private String param;


    /**
     * 业务备注
     */
    private String remark;


    /**
     * 令牌
     */
    private String token;


    /*  **************************************
        其他的备用信息,主要是gps设备会提供
     *************************************  */

    /**
     * 温度 1
     */
    private String temperature1;
    /**
     * 温度 2
     */
    private String temperature2;
    /**
     * 温度 3
     */
    private String temperature3;
    /**
     * 温度 4
     */
    private String temperature4;

    /**
     * 湿度
     */
    private String humidity;
    private String humidity2;
    private String humidity3;
    private String humidity4;

    //传感器采集时间,如果是多个传感器默认使用第一个温度传感器的时间
    private String sensorTime;
}
