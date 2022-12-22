package com.tts.remote.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.Properties;

/**
 * iov-承运商 配置信息
 *
 * @author FangYuan
 * @since 2022-12-22 15:00:31
 */
@Data
public class TtsIovConfigDto implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 承运商编号,唯一
     */
    private String carrierCode;

    /**
     * iov 类型 , 例如 G7, SINOIOV, 大写
     * 这里 承运商+iov类型 组成了联合 唯一索引
     */
    private String iovType;

    /**
     * 配置信息,不同类型的iov 一般配置信息不同
     */
    private Properties config;

    /**
     * 车辆网数据订阅周期,单位是秒,表示间隔这么长时间 获取 车辆的车辆网数据,目前支持最小值为 30秒
     */
    private Long subscribeInterval;

    /**
     * 查询最新坐标的时间范围,单位根据 不同的iovType而不同
     */
    private Integer timeNearBy;

    /**
     * 是否开启日志标记位 0:关闭 1: 开启
     */
    private Integer logFlag;
}
