package com.tts.framework.cache.redis;

import com.tts.framework.cache.redis.operator.*;
import com.tts.framework.cache.redis.operator.impl.*;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.client.codec.Codec;
import org.redisson.config.Config;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.atomic.AtomicBoolean;


/**
 * redis 客户端
 * 提供给业务系统使用redis的统一对外接口
 * 底层基于 redisson 实现
 *
 * @author FangYuan
 * @since 2021-01-15
 */
public class RedisClient  {

    public static final String EMPTY_NAMESAPCE = null;
    private final static Logger log = LoggerFactory.getLogger(RedisClient.class);

    private final static String REDIS_CONFIG_HEADER_KEY = "redisclient";
    private final static String CONF_FILE = "redisclient.yml";

    private RedisClient(){
    }

    private volatile RedisClientConfig redisClientConfig;
    private static volatile AtomicBoolean isInited = new AtomicBoolean(false);

    /** redis 开关, false:关闭redis  true: 开启redis   */
    private static volatile Boolean isRedisSwitch ;

    private volatile static RedisClient instance;

    protected RedissonClient redissonClient;



    /**
     * 初始化redisClient并获取其实例的方法
     * redisClien应该是个单例的
     */
    public  static RedisClient getInstance(){

        if(instance == null  || !isInited.get()) {
            synchronized (RedisClient.class) {
                if (instance == null) {
                    log.info("Redis Client 开始初始化");
                    instance = new RedisClient();
                }

                // 未初始化
                if (!isInited.get()) {
                    //设置redisClient属性
                    //redis属性存储于application.redisClient 前缀的内容下

                    //生成redis配置信息对象
                    RedisClientConfig redisClientConfig =  RedisClientTools.generateRedisClientConfig(CONF_FILE,REDIS_CONFIG_HEADER_KEY);

                    //判断redis配置是否加载成功
                    //如果没有加载成功则抛出异常
                    if(redisClientConfig == null || !redisClientConfig.isSuccess()){
                        throw new RedisClientException("redis client 配置文件信息加载失败!");
                    }

                    log.info("Redis Client 配置信息加载成功: ["+redisClientConfig.toString()+"]");

                    //执行 RedisClient的初始化工作
                    instance.init(redisClientConfig);

                    //设置当前为已经初始化
                    isInited.set(Boolean.TRUE);

                    log.info("Redis Client 初始化完成");
                }
            }
        }
        return instance;
    }

    public static Boolean getIsRedisSwitch(){

        return RedisClient.getInstance().isRedisSwitch;
    }


    /**
     *  进行redis client 的初始化 工作
     * @param redisClientConfig redis client 配置信息
     */
    private void init(RedisClientConfig redisClientConfig)   {

        this.redisClientConfig = redisClientConfig;

        isRedisSwitch = this.redisClientConfig.isRedisSwitch();

        //如果 redis 缓存开关为 false ,则什么也不执行并退出
        if(!this.redisClientConfig.isRedisSwitch()){
            log.info("redis client 开关为 关闭,停止初始化redis client");
            return;
        }

        //  redission 客户端的配置对象
        Config redissonConfig = redisClientConfig.getRedissonConfig();

        this.redissonClient = Redisson.create(redissonConfig);

    }

    /**
     * 关闭redis 客户端,建议程序退出时执行
     */
    public synchronized void shutdown(){
        if(this.redissonClient != null && !this.redissonClient.isShuttingDown()){
            this.redissonClient.shutdown();
        }
    }

    public RedissonClient getRedissonClient() {
        return redissonClient;
    }

    public RedisClientStringOperator getStringOperator(){
        return new RedisClientStringOperatorImpl(this.redisClientConfig);
    }

    public RedisClientScheduledExecutorOperator getScheduledExecutorOperator(){
        return new RedisClientScheduledExecutorOperatorImpl(this.redisClientConfig);
    }

    public RedisClientTopicOperator getTopicOperator(){
        return new RedisClientTopicOperatorImpl(this.redisClientConfig);
    }

    public RedisClientSetOperator getSetOperator(){
        return new RedisClientSetOperatorImpl(this.redisClientConfig);
    }

    public  <T> RedisClientObjectOperator<T> getObjectOperator(Codec codec){
        return new RedisClientObjectOperatorImpl(this.redisClientConfig,codec);
    }

    public  <T>  RedisClientObjectOperator<T> getObjectOperator(){
        return new RedisClientObjectOperatorImpl(this.redisClientConfig);
    }

    public RedisClientQueueOperator getQueueOperator(){
        return new RedisClientQueueOperatorImpl(this.redisClientConfig);
    }

    public RedisClientMapOperator getMapOperator(){
        return new RedisClientMapOperatorImpl(this.redisClientConfig);
    }

    public RedisClientLockOperator getLockOperator(){
        return new RedisClientLockOperatorImpl(this.redisClientConfig);
    }

    public RedisClientListOperator getListOperator(){
        return new RedisClientListOperatorImpl(this.redisClientConfig);
    }

    public RedisClientDelayedQueueOperator getDelayedQueueOperator(){
        return new RedisClientDelayedQueueOperatorImpl(this.redisClientConfig);
    }

    public RedisClientBitSetOperator getBitSetOperator(){
        return new RedisClientBitSetOperatorImpl(this.redisClientConfig);
    }

    public RedisClientAtomicLongOperator getAtomicLongOperator(){
        return new RedisClientAtomicLongOperatorImpl(this.redisClientConfig);
    }

    public RedisClientAtomicDoubleOperator getAtomicDoubleOperator(){
        return new RedisClientAtomicDoubleOperatorImpl(this.redisClientConfig);
    }




}
