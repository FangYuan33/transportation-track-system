package com.tts.base.constant;

/**
 * 全局配置key 常量类
 * <p>
 * 对应数据表 tts_global_configure
 *
 * @author FangYuan
 * @since 2021-08-23
 */
public class TtsGlobalConfigureKeyConstants {

    /**
     * 全局配置 redis 开关 是否开启
     * 这里要注意,并不是说 关闭后就完全不用redis了
     * 只是说在 存储和查询坐标时不依赖redis了,同时关闭强依赖redis的 坐标计算等功能
     * 换句话说,就是说如果这个功能开启,那么系统会在 存储和查询坐标时强依赖redis,导致redis 的 访问量 飙升
     * 对应 StatusEnum 常量值 0: 关闭 1: 开启
     */
    public static final String GLOBAL_REDIS_SWITCH = "GLOBAL_REDIS_SWITCH";

    /**
     * IOV 开关,如果关闭的话就不会启动 与IOV 相关的后台线程,这样的话即使创建的IOV 配置和任务也不会真正运行起来
     * 对应 StatusEnum 常量值 0: 关闭 1: 开启
     */
    public static final String GLOBAL_IOV_SWITCH = "GLOBAL_IOV_SWITCH";
    /**
     * 接口被调用日志开关
     */
    public static final String GLOBAL_REMOTE_SWITCH = "GLOBAL_REMOTE_SWITCH";

}
