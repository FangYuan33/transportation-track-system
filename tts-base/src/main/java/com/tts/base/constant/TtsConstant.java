package com.tts.base.constant;

import java.math.RoundingMode;
import java.util.Arrays;
import java.util.List;

/**
 * TTS系统相关的常量类
 */
public class TtsConstant {

    /**
     * 系统动作:创建session
     */
    public static final String SYS_ACTION_CREATE_SESSION = "sys_csession";


    /**
     * 业务编码: sessionId
     */
    public static final String RESPONSE_CODE_SESSION_ID = "sessionId";

    /**
     * 数字型的默认值,一般用于remote接口 为当没有这个值时作为默认值用
     */
    public static final String DEFAULT_NUM_VALUE = "-1";

    /**
     * 默认租户信息,用于本地化项目这种没有多租户的情况
     */
    public static final Long DEFAULT_TENANT_ID = -1L;

    /**
     * 所有租户信息
     */
    public static final Long ALL_TENANT_ID = -2L;

    /**
     * 唯一
     */
    public final static String FLAG_UNIQUE = "0";
    /**
     * 不唯一
     */
    public final static String FLAG_NOT_UNIQUE = "1";

    /**
     * 状态正常的坐标点的 error code 码 为 0
     */
    public final static String NORMAL_POINT_ERROR_CODE = "0";

    /**
     * 定位方式列表
     */
    public static final List<String> LOCATION_TYPE_LIST = Arrays.asList(LocationModeEnum.GPS.getValue(),
            LocationModeEnum.WIFI.getValue(), LocationModeEnum.LBS.getValue());

    /**
     * 用于redis中缓存数据的 TTS 系统的NAMESPACE
     */
    public static final String REDIS_NAMESPACE = "TTS";

    /**
     * 用于redis中缓存数据的TTS 系统的坐标流水队列的KEY
     */
    public static final String REDIS_KEY_COORDINATE_QUEUE = "COOR-QUEUE-";

    /**
     * 用于redis中 geofence 监听器 的队列
     */
    public static final String REDIS_KEY_GEO_MONITOR_QUEUE = "GEO-MONITOR-QUEUE-";

    /**
     * 用于redis中缓存数据的TTS 系统的 运算用 坐标详情前缀的KEY,用于运算
     */
    public static final String REDIS_KEY_COORDINATE_CAL_PRE = "COOR-CAL-";

    /**
     * 用于redis中缓存数据的TTS 系统的 运算用 坐标详情前缀的KEY
     */
    public static final String REDIS_KEY_COORDINATE_LATEST_PRE = "COOR-LATEST-";

    /**
     * 线程 中周期性循环的间隔时间
     */
    public static final long PERIODIC_INTERVAL_TIME = 10 * 1000;


    /**
     * 刷新redis内容 有效期的 间隔,10分钟
     */
    public static final long REDIS_REFRESH_SLEEP_TIME = 10 * 60 * 1000;

    /**
     * NAMESAPCE和KEY之间默认用了 _ 做分隔符
     * redis 中key的默认分隔符.
     */
    public static final String REDIS_KEY_SPLIT = "-";

    /**
     * 电子围栏监听器 - 全局监听器 前缀
     */
    public static final String REDIS_KEY_GEOFENCE_MONITOR_GLOBAL_PREFIX = "MONITOR_GLOBAL_";

    /**
     * 电子围栏监听器 -  车辆监听器 前缀
     */
    public static final String REDIS_KEY_GEOFENCE_MONITOR_VEHICLE_PREFIX = "MONITOR_VEHICLE_";

    /**
     * 电子围栏监听器 - 电子围栏 详情 key
     */
    public static final String REDIS_KEY_GEOFENCE_MONITOR_GEOFENCE_PREFIX = "MONITOR_GEOFENCE_";

    /**
     * redis 补偿机制 锁 详情 key
     */
    public static final String REDIS_LOCK_REFRESH_REDIS = "REDIS_LOCK_REFRESH_REDIS";

    //车辆和电子围栏的位置
    public static final String GEOFENCE_AND_VEHICLE_IN = "in";
    public static final String GEOFENCE_AND_VEHICLE_OUT = "out";


    public static final int DIVEIDE_SCALE = 10;
    public static final RoundingMode DIVEIDE_ROUNDINGMODE = RoundingMode.HALF_UP;


    //电子围栏类型
    public static final Integer GEOFENCE_TYPE_POLYGON = 0;
    public static final Integer GEOFENCE_TYPE_ROUND = 1;
    public static final Integer GEOFENCE_TYPE_LINE = 2; //用于路线围栏
    /**
     * 批量插入数量,每批次插入的数据量
     */
    public static final Integer MAX_BATCH_SIZE = 1000;


}
