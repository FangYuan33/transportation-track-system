package com.tts.base.constant;

/**
 * 定位方式枚举常量
 * <p>
 * - GPS 定位
 * - 网络定位
 * - WIFI定位
 * - 基站定位
 * <p>
 * 一般手机可以设置如下几种定位策略模式,不同地图sdk之间可能略有区别
 * 1、高精度模式定位策略：这种定位模式下，会同时使用网络定位和GPS定位，优先返回最高精度的定位结果；
 * 2、低功耗模式定位策略：该定位模式下，不会使用GPS，只会使用网络定位（Wi-Fi和基站定位）；
 * 3、仅用设备模式定位策略：这种定位模式下，不需要连接网络，只使用GPS进行定位，这种模式下不支持室内环境的定位。
 *
 * @author FangYuan
 * @since 2022-12-22 17:32:06
 */
public enum LocationModeEnum {

    /**
     * gps 定位,精度10米左右,具体取决于芯片,室内不可用
     * 根据设备GPS芯片和GPS卫星实现定位，GPS定位在室内是不可以使用的。
     * GPS定位精度和芯片本身以及实际使用环境有关，一般情况下，GPS定位精度在10m左右。
     */
    GPS("GPS"),

    /**
     * WIFI定位,经度 30-200m,WIFI覆盖率超过90%
     * 根据设备获取的Wi-Fi的信息进行定位，Wi-Fi定位精度一般不受使用环境影响，主要和Wi-Fi半径，密度有关。
     * Wi-Fi定位精度目前在20m左右。
     */
    WIFI("WIFI"),

    /**
     *
     * 基站定位，精度100-300m,基站覆盖率超过95%
     * 根据设备获取的基站信息实现定位，基站定位精度一般不受使用环境影响，主要和基站的覆盖半径有关。
     * 基站定位服务精度目前在200m左右。
     */
    LBS("LBS");

    private final String value;

    /**
     * 构造方法
     *
     * @param value 定位方式枚举值
     */
    LocationModeEnum(String value) {
        this.value = value;
    }


    /**
     * 返回value值
     */
    public String getValue() {
        return this.value;
    }

}
