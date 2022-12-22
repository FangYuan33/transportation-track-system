package com.tts.remote.constant;


/**
 * 坐标类型
 * 手机端目前传过来的坐标类型 为小写字母,没有横线
 * 目前跟手机端沟通,他们预计以后统一使用 火星坐标gcj02 , 不会有变化
 * <p>
 * 但是为了计算精确,我们在运算时会 先把 gcj02 转会为 wgs84 再进行运算
 * <p>
 * 从设备获取经纬度（GPS）坐标
 * <p>
 * 如果使用的是百度sdk那么可以获得百度坐标（bd09）或者火星坐标（GCJ02),默认是bd09
 * 如果使用的是ios的原生定位库，那么获得的坐标是WGS84
 * 如果使用的是腾讯地图, 高德地图sdk,那么获取的坐标是GCJ02
 * <p>
 * 最准确的距离计算应当采用WGS84的坐标，采用火星坐标系和百度坐标从原理上就可能存在偏差。
 *
 * @author FangYuan
 * @since 2022-12-22 16:05:29
 */
public enum CoordinateTypeEnum {

    /**
     * 地球坐标 (WGS84)
     * <p>
     * 国际标准，从 GPS 设备中取出的数据的坐标系
     * 国际地图提供商使用的坐标系
     */
    WGS84("wgs84"),

    /**
     * 火星坐标 (GCJ-02)也叫国测局坐标系
     * <p>
     * 中国标准，从国行移动设备中定位获取的坐标数据使用这个坐标系
     * 国家规定： 国内出版的各种地图系统（包括电子形式），必须至少采用GCJ-02对地理位置进行首次加密。
     */
    GCJ02("gcj02"),

    /**
     * 百度坐标 (BD-09)
     * <p>
     * 百度标准，百度 SDK，百度地图，Geocoding 使用(本来就乱了，百度又在火星坐标上来个二次加密)
     */
    BD09("bd09");


    private final String value;

    CoordinateTypeEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
