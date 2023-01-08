package com.tts.facade.constant;

/**
 * g7 的访问 url地址
 *
 * @author FangYuan
 * @since 2023-01-08 17:09:11
 */
public class G7UrlConstant {

    /**
     * 端对端接口对接: 单个车牌号查询车辆当前位置
     */
    public final static String QUERY_LAST_LOCATION_BY_SINGLE_PLATE_NUM = "/v1/openapi-expline/trace/location";

    /**
     * 端对端接口对接: 单个车牌号查询车辆轨迹
     */
    public final static String QUERY_TRACK_BY_SINGLE_PLATE_NUM = "/v1/openapi-expline/trace/list";
}
