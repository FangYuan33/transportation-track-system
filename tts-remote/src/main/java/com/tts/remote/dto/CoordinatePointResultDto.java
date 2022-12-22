package com.tts.remote.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * app上传的单个坐标的报文
 *
 * @author FangYuan
 * @since 2022-12-22 16:00:33
 */
@Data
public class CoordinatePointResultDto implements Serializable {

    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = -3250365560386230565L;

    /**
     * 租户id
     */
    private Long tenantId;

    //车牌号
    private String vehicleNo;

    /**
     * 车辆id
     */
    private Long vehicleId;

    /**
     * 装车单id 列表
     */
    private String shipmentIdArray;

    /**
     * 纬度
     */
    private String latitude;

    /**
     * 经度
     */
    private String longitude;

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

    /**
     * 服务器当前时间
     */
    private Date createTime;

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
     * 用来描述车辆的状态等信息.目前只有易流有对应信息
     */
    private String remark;


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

    //湿度1
    private String humidity;
    //湿度2
    private String humidity2;
    //湿度3
    private String humidity3;
    //湿度4
    private String humidity4;

    //采集设备类型
    private String deviceType;

    //上传时间
    private Date uploadTime;
}
