package com.tts.main.domain;

import com.tts.framework.common.domain.SimpleBaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * app上传的单个坐标的报文
 *
 * @author FangYuan
 * @since 2022-12-22 17:32:06
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class TtsCoordinatePoint extends SimpleBaseEntity {

    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = -3250365560386230565L;

    /**
     * 主键id
     */
    private Long id;

    /**
     * 会话id
     */
    private Long sessionId;

    /**
     * 租户id
     */
    private Long tenantId;

    /**
     * 车辆id
     */
    private Long vehicleId;

    /**
     * 车牌号
     */
    private String vehicleNo;

    /**
     * 装车单id 列表
     */
    private String shipmentIdArray;

    /**
     * 装车单id
     */
    private Long shipmentId;


      /*  ********************************
       定位的基本信息
       一般所有定位点都会包含(除了异常点之外)
    ************************************  */


    /**
     * 定位地图坐标系类别
     */
    private String mapCoordinateSystem;


    /**
     * 定位结果来源类别:
     * 包含 GPS,基站,WIFI,蓝牙,传感器.....
     */
    private String locationType;


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

    //省
    private String province;
    //市
    private String city;
    //县
    private String country;

    /**
     * 坐标定位时间
     */
    private Date positionTime;

    /**
     * 坐标上传手机时间
     */
    private Date uploadTime;

    /**
     * 服务器当前时间
     */
    private Date createTime;

    /**
     * 精确度判断的表示
     * true为精确,false为不精确
     * 默认为精确
     */
    private boolean accurateFlag;

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

}
