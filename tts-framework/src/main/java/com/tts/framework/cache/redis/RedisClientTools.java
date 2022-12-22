package com.tts.framework.cache.redis;

import com.tts.framework.common.utils.YamlUtil;
import org.redisson.client.codec.Codec;
import org.redisson.config.*;
import org.redisson.connection.balancer.LoadBalancer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileNotFoundException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.Map;

import static com.tts.framework.cache.redis.RedisClientConfigKeys.*;


/**
 * redis client 内部工具类
 *
 * @author FangYuan
 * @since 2021-01-18
 */
public class RedisClientTools {

    private final static Logger log = LoggerFactory.getLogger(RedisClientTools.class);

    /**
     * 生成redis配置信息对象
     *
     * @param confFile
     * @return
     */
    protected static RedisClientConfig generateRedisClientConfig(String confFile, String redisConfigHeaderKey) {

        RedisClientConfig redisClientConfig = null;

        try {
            Map configMap = YamlUtil.loadYaml(confFile);

            if (configMap == null) {
                throw new RedisClientException("configMap(" + confFile + ") is null");
            }

            configMap = (Map) configMap.get(redisConfigHeaderKey);

            //打印 配置信息
            log.info("redis client  的 配置文件内容:" + Arrays.toString(configMap.entrySet().toArray()));

            redisClientConfig = new RedisClientConfig();

            redisClientConfig.setRedisSwitch((Boolean) configMap.get(RedisClientConfigKeys.redisSwitch));

            //判断redis开关是否打开,如果是关闭的则直接返回
            if (redisClientConfig.isRedisSwitch()) {

                redisClientConfig.setRedisMode((String) configMap.get(RedisClientConfigKeys.redisMode));

                //消息默认有效期
                String defaultTTLValue = toString(configMap.get(defaultTTL));

                if (defaultTTLValue == null) {
                    redisClientConfig.setDefaultTTL(Integer.parseInt(defaultTTLValue));
                }

                Config redissonConfig = generateRedissonConfig(redisClientConfig, configMap);

                redisClientConfig.setRedissonConfig(redissonConfig);

                redisClientConfig.setSuccess(Boolean.TRUE);
            }

        } catch (FileNotFoundException e) {
            log.error("redis client 配置文件(" + confFile + ")没有找到!", e);
        } catch (IllegalAccessException | InstantiationException | ClassNotFoundException | MalformedURLException e) {
            log.error("redis client 配置文件解析失败!", e);
        }

        return redisClientConfig;
    }


    /**
     * 初始化 redission 客户端
     *
     * @param redisClientConfig
     * @param configMap
     * @return
     */
    private static Config generateRedissonConfig(RedisClientConfig redisClientConfig, Map configMap) throws ClassNotFoundException, InstantiationException, IllegalAccessException, MalformedURLException {

        Config config = new Config();

        generateRedissonConfig4Common(config, redisClientConfig, configMap);

        //获取redis的模式
        String redisMode = redisClientConfig.getRedisMode();

        switch (redisMode) {

            //单机模式 single
            case "single":
                generateRedissonConfig4Single(config, redisClientConfig, configMap);
                break;
            //哨兵模式 sentinel
            case "sentinel":
                generateRedissonConfig4Sentinel(config, redisClientConfig, configMap);
                break;
            //主从模式 masterSlave
            case "masterSlave":
                generateRedissonConfig4MasterSlave(config, redisClientConfig, configMap);
                break;
            //集群模式 cluster
            case "cluster":
                generateRedissonConfig4Cluster(config, redisClientConfig, configMap);
                break;
        }

        return config;
    }

    /**
     * 生成 redisson的常用配置
     * 适用于所有Redis组态模式（单机，集群和哨兵）
     *
     * @param config
     * @param redisClientConfig
     * @param configMap
     */
    private static void generateRedissonConfig4Common(Config config, RedisClientConfig redisClientConfig, Map configMap) throws ClassNotFoundException, IllegalAccessException, InstantiationException {

        /* codec
         * 编码
         * Redisson的对象编码类是用于将对象进行序列化和反序列化，以实现对该对象在Redis里的读取和存储。
         * Default is FST codec
         * 默认值: org.redisson.codec.FstCodec
         * */
        String codecValue = (String) configMap.get(codec);
        if (codecValue != null) {
            config.setCodec((Codec) Class.forName(codecValue).newInstance());
        }

        /* threads 线程池数量
         * 这个线程池数量被所有RTopic对象监听器，RRemoteService调用者和RExecutorService任务共同共享。
         * 默认值: 16
         * 如果设置为0,则为当前处理核数量 * 2
         */
        String threadsValue = toString(configMap.get(threads));
        if (threadsValue != null) {
            config.setThreads(Integer.parseInt(threadsValue));
        }

        /* nettyThreads
         * Netty线程池数量
         * 这个线程池数量是在一个Redisson实例内，被其创建的所有分布式数据类型和服务，
         * 以及底层客户端所一同共享的线程池里保存的线程数量。
         * 默认值: 32
         * 如果设置为0则为当前处理核数量 * 2
         * */
        String nettyThreadsValue = toString(configMap.get(nettyThreads));
        if (nettyThreadsValue != null) {
            config.setNettyThreads(Integer.parseInt(nettyThreadsValue));
        }


        /* executor
         * 线程池
         * 单独提供一个用来执行所有RTopic对象监听器，RRemoteService调用者和RExecutorService任务的线程池（ExecutorService）实例。
         * */
        //config.setExecutor()

        /* eventLoopGroup
         * 用于特别指定一个EventLoopGroup.
         * EventLoopGroup是用来处理所有通过Netty与Redis服务之间的连接发送和接受的消息。
         * 每一个Redisson都会在默认情况下自己创建管理一个EventLoopGroup实例。
         *  因此，如果在同一个JVM里面可能存在多个Redisson实例的情况下，
         * 采取这个配置实现多个Redisson实例共享一个EventLoopGroup的目的。
         * 只有io.netty.channel.epoll.EpollEventLoopGroup或io.netty.channel.nio.NioEventLoopGroup才是允许的类型。
         * 目前的场景应该不会更改这个配置
         * */
        //config.setEventLoopGroup()


        /* transportMode
         * 传输模式
         * 可选参数：
         *     TransportMode.NIO,
         *     TransportMode.EPOLL - 需要依赖里有netty-transport-native-epoll包（Linux）
         *     TransportMode.KQUEUE - 需要依赖里有 netty-transport-native-kqueue包（macOS）
         * 默认值：TransportMode.NIO
         * 目前的场景应该不会更改这个配置
         */
        //config.setTransportMode(TransportMode.NIO);

             /* lockWatchdogTimeout
                监控锁的看门狗超时，单位：毫秒
                监控锁的看门狗超时时间单位为毫秒。
                该参数只适用于分布式锁的加锁请求中未明确使用leaseTimeout参数的情况。
                如果该看门口未使用lockWatchdogTimeout去重新调整一个分布式锁的lockWatchdogTimeout超时，那么这个锁将变为失效状态。
                这个参数可以用来避免由Redisson客户端节点宕机或其他原因造成死锁的情况。
                默认值：30000
              */
        String lockWatchdogTimeoutValue = toString(configMap.get(lockWatchdogTimeout));
        if (lockWatchdogTimeoutValue != null) {
            config.setLockWatchdogTimeout(Integer.parseInt(lockWatchdogTimeoutValue));
        }

              /*
                  keepPubSubOrder
                  保持订阅发布顺序
                  通过该参数来修改是否按订阅发布消息的接收顺序出来消息，如果选否将对消息实行并行处理，该参数只适用于订阅发布消息的情况。
                  默认值：true
               */
        String keepPubSubOrderValue = toString(configMap.get(keepPubSubOrder));
        if (keepPubSubOrderValue != null) {
            config.setKeepPubSubOrder(Boolean.parseBoolean(keepPubSubOrderValue));
        }

    }

    /**
     * 生成单例模式的 redisson 配置
     *
     * @param config
     * @param redisClientConfig
     * @param configMap
     */
    private static void generateRedissonConfig4Single(Config config, RedisClientConfig redisClientConfig, Map configMap) throws MalformedURLException {

        /* 单节点设置
         * */
        SingleServerConfig singleServerConfig = config.useSingleServer();

        /*  Base 配置 */
        generateBaseConfig(singleServerConfig, redisClientConfig, configMap);

        /* address（节点地址)
         * 可以通过host:port的格式来指定节点地址
         * */
        String addressValue = toString(configMap.get(address));
        if (addressValue != null) {
            singleServerConfig.setAddress(addressValue);
        }


        /* connectionPoolSize
         * 连接池大小
         * 连接池最大容量。
         * 连接池的连接数量自动弹性伸缩。
         * 默认值: 64
         * */
        String connectionPoolSizeValue = toString(configMap.get(connectionPoolSize));
        if (connectionPoolSizeValue != null) {
            singleServerConfig.setConnectionPoolSize(Integer.parseInt(connectionPoolSizeValue));
        }


        /* database
         * 数据库编号
         * 尝试连接的数据库编号。
         * 默认值：0
         * */
        String databaseValue = toString(configMap.get(database));
        if (databaseValue != null) {
            singleServerConfig.setDatabase(Integer.valueOf(databaseValue));
        }

        /**
         * dnsMonitoringInterval
         * Interval in milliseconds to check DNS
         */
        String dnsMonitoringIntervalValue = toString(configMap.get(dnsMonitoringInterval));
        if (dnsMonitoringIntervalValue != null) {
            singleServerConfig.setDnsMonitoringInterval(Integer.valueOf(dnsMonitoringIntervalValue));
        }

        /**
         * subscriptionConnectionMinimumIdleSize
         * Minimum idle subscription connection amount
         */
        String subscriptionConnectionMinimumIdleSizeValue = toString(configMap.get(subscriptionConnectionMinimumIdleSize));
        if (subscriptionConnectionMinimumIdleSizeValue != null) {
            singleServerConfig.setSubscriptionConnectionMinimumIdleSize(Integer.valueOf(subscriptionConnectionMinimumIdleSizeValue));
        }

        /**
         * subscriptionConnectionPoolSize
         * Redis subscription connection maximum pool size
         */
        String subscriptionConnectionPoolSizeValue = toString(configMap.get(subscriptionConnectionPoolSize));
        if (subscriptionConnectionPoolSizeValue != null) {
            singleServerConfig.setSubscriptionConnectionPoolSize(Integer.valueOf(subscriptionConnectionPoolSizeValue));
        }


        /**
         * connectionMinimumIdleSize
         * Minimum idle Redis connection amount
         */
        String connectionMinimumIdleSizeValue = toString(configMap.get(connectionMinimumIdleSize));
        if (connectionMinimumIdleSizeValue != null) {
            singleServerConfig.setConnectionMinimumIdleSize(Integer.valueOf(connectionMinimumIdleSizeValue));
        }


    }


    /**
     * 生成哨兵模式的 redisson 配置
     *
     * @param config
     * @param redisClientConfig
     * @param configMap
     */
    private static void generateRedissonConfig4Sentinel(Config config, RedisClientConfig redisClientConfig, Map configMap) throws ClassNotFoundException, IllegalAccessException, InstantiationException, MalformedURLException {

        SentinelServersConfig sentinelServersConfig = config.useSentinelServers();

        /*  Base 配置 */
        generateBaseConfig(sentinelServersConfig, redisClientConfig, configMap);
        generateBaseMasterSlaveConfig(sentinelServersConfig, redisClientConfig, configMap);


        /*
           masterName（主服务器的名称）
           主服务器的名称是哨兵进程中用来监测主从服务切换情况的。
         */
        String masterNameValue = toString(configMap.get(masterName));
        if (masterNameValue != null) {
            sentinelServersConfig.setMasterName(masterNameValue);
        }


        /*
           sentinelAddresses（添加哨兵节点地址）
           可以通过host:port的格式来指定哨兵节点的地址。多个节点可以一次性批量添加。
         */
        String[] sentinelAddressesValue = (String[]) configMap.get(masterName);
        if (sentinelAddressesValue != null) {
            for (String sentinelAddressesValueItem : sentinelAddressesValue) {
                sentinelServersConfig.addSentinelAddress(sentinelAddressesValueItem);
            }
        }



      /* database（数据库编号）
        尝试连接的数据库编号。
        默认值：0 */
        String databaseValue = toString(configMap.get(database));
        if (databaseValue != null) {
            sentinelServersConfig.setDatabase(Integer.parseInt(databaseValue));
        }

        /**
         * scanInterval
         * Sentinel scan interval in milliseconds
         */
        String scanIntervalValue = toString(configMap.get(scanInterval));
        if (scanIntervalValue != null) {
            sentinelServersConfig.setScanInterval(Integer.parseInt(scanIntervalValue));
        }

        /**
         * checkSentinelsList
         * Enables sentinels list check during Redisson startup.
         * <p>
         * Default is <code>true</code>
         */
        String checkSentinelsListValue = toString(configMap.get(checkSentinelsList));
        if (checkSentinelsListValue != null) {
            sentinelServersConfig.setScanInterval(Integer.parseInt(checkSentinelsListValue));
        }


    }

    /**
     * 生成主从模式的redisson配置
     *
     * @param config
     * @param redisClientConfig
     * @param configMap
     */
    private static void generateRedissonConfig4MasterSlave(Config config, RedisClientConfig redisClientConfig, Map configMap) throws ClassNotFoundException, IllegalAccessException, InstantiationException, MalformedURLException {

        MasterSlaveServersConfig masterSlaveServersConfig = config.useMasterSlaveServers();

        /*  Base 配置 */
        generateBaseConfig(masterSlaveServersConfig, redisClientConfig, configMap);
        generateBaseMasterSlaveConfig(masterSlaveServersConfig, redisClientConfig, configMap);


        /*
           masterAddress
           主节点地址
           可以通过host:port的格式来指定主节点地址。
         */
        String masterAddressValue = toString(configMap.get(masterAddress));
        if (masterAddressValue != null) {
            masterSlaveServersConfig.setMasterAddress(masterAddressValue);
        }

        /*
          slaveAddresses
          添加从主节点地址
          可以通过host:port的格式来指定从节点的地址。多个节点可以一次性批量添加。
         */
        String[] slaveAddressesValue = (String[]) configMap.get(slaveAddresses);
        if (slaveAddressesValue != null) {
            for (String slaveAddressesValueItem : slaveAddressesValue) {
                masterSlaveServersConfig.addSlaveAddress(slaveAddressesValueItem);
            }
        }

         /* database（数据库编号）
        尝试连接的数据库编号。
        默认值：0 */
        String databaseValue = toString(configMap.get(database));
        if (databaseValue != null) {
            masterSlaveServersConfig.setDatabase(Integer.parseInt(databaseValue));
        }


    }

    /**
     * 生成集群模式的redisson配置
     *
     * @param config
     * @param redisClientConfig
     * @param configMap
     */
    private static void generateRedissonConfig4Cluster(Config config, RedisClientConfig redisClientConfig, Map configMap) throws ClassNotFoundException, IllegalAccessException, InstantiationException, MalformedURLException {

        ClusterServersConfig clusterConfig = config.useClusterServers();

        /*  Base 配置 */
        generateBaseConfig(clusterConfig, redisClientConfig, configMap);
        generateBaseMasterSlaveConfig(clusterConfig, redisClientConfig, configMap);


       /*  nodeAddresses（添加节点地址）
         可以通过host:port的格式来添加Redis集群节点的地址。多个节点可以一次性批量添
        */
        String[] nodeAddressesValue = (String[]) configMap.get(nodeAddresses);
        if (nodeAddressesValue != null) {
            for (String nodeAddressesValueItem : nodeAddressesValue) {
                clusterConfig.addNodeAddress(nodeAddressesValueItem);
            }
        }


       /* scanInterval（集群扫描间隔时间）
        对Redis集群节点状态扫描的时间间隔。单位是毫秒。
        默认值： 1000 */
        String scanIntervalValue = toString(configMap.get(scanInterval));
        if (scanIntervalValue != null) {
            clusterConfig.setScanInterval(Integer.parseInt(scanIntervalValue));
        }

        /**
         * checkSlotsCoverage
         * Enables cluster slots check during Redisson startup.
         * <p>
         * Default is <code>true</code>
         */
        String checkSlotsCoverageValue = toString(configMap.get(checkSlotsCoverage));
        if (checkSlotsCoverageValue != null) {
            clusterConfig.setCheckSlotsCoverage(Boolean.parseBoolean(checkSlotsCoverageValue));
        }
    }

    /**
     * 配置基本的master slave 配置
     *
     * @param baseConfig
     * @param redisClientConfig
     * @param configMap
     */
    private static void generateBaseMasterSlaveConfig(BaseMasterSlaveServersConfig baseConfig, RedisClientConfig redisClientConfig, Map configMap) throws ClassNotFoundException, IllegalAccessException, InstantiationException {
        /*
           loadBalancer（负载均衡算法类的选择）
           在使用多个Redis服务节点的环境里，可以选用以下几种负载均衡方式选择一个节点：
                          org.redisson.connection.balancer.WeightedRoundRobinBalancer - 权重轮询调度算法
                          org.redisson.connection.balancer.RoundRobinLoadBalancer - 轮询调度算法
                          org.redisson.connection.balancer.RandomLoadBalancer - 随机调度算法

            默认值： org.redisson.connection.balancer.RoundRobinLoadBalancer
         */
        String loadBalancerValue = toString(configMap.get(loadBalancer));
        if (loadBalancerValue != null) {
            baseConfig.setLoadBalancer((LoadBalancer) Class.forName(loadBalancerValue).newInstance());
        }

      /*
       slaveConnectionMinimumIdleSize（从节点最小空闲连接数）
       多从节点的环境里，每个 从服务节点里用于普通操作（非 发布和订阅）的最小保持连接数（长连接）。
       长期保持一定数量的连接有利于提高瞬时读取反映速度。
       默认值：24
     */
        String slaveConnectionMinimumIdleSizeValue = toString(configMap.get(slaveConnectionMinimumIdleSize));
        if (slaveConnectionMinimumIdleSizeValue != null) {
            baseConfig.setSlaveConnectionMinimumIdleSize(Integer.parseInt(slaveConnectionMinimumIdleSizeValue));
        }

      /*
       slaveConnectionPoolSize（从节点连接池大小）
       多从节点的环境里，每个 从服务节点里用于普通操作（非 发布和订阅）连接的连接池最大容量。
       连接池的连接数量自动弹性伸缩。
       默认值：64
       */
        String slaveConnectionPoolSizeValue = toString(configMap.get(slaveConnectionPoolSize));
        if (slaveConnectionPoolSizeValue != null) {
            baseConfig.setSlaveConnectionPoolSize(Integer.parseInt(slaveConnectionPoolSizeValue));
        }


        /*
          failedSlaveReconnectionInterval
          redis slave 重连间隔,当其已从可用服务器的内部列表中被排除的时候
          在每个此类超时事件中，Redisson都会尝试连接到已断开连接的Redis服务器。
          默认值 3000 毫秒
         */
        String failedSlaveReconnectionIntervalValue = toString(configMap.get(failedSlaveReconnectionInterval));
        if (failedSlaveReconnectionIntervalValue != null) {
            baseConfig.setFailedSlaveReconnectionInterval(Integer.parseInt(failedSlaveReconnectionIntervalValue));
        }

         /*
          failedSlaveCheckInterval
          失败从节点校验间隔时间
          默认值 180000 毫秒
         */
        String failedSlaveCheckIntervalValue = toString(configMap.get(failedSlaveCheckInterval));
        if (failedSlaveCheckIntervalValue != null) {
            baseConfig.setFailedSlaveCheckInterval(Integer.parseInt(failedSlaveCheckIntervalValue));
        }

         /*
           masterConnectionMinimumIdleSize（主节点最小空闲连接数）
           多从节点的环境里，每个 主节点的最小保持连接数（长连接）。
           长期保持一定数量的连接有利于提高瞬时写入反应速度。
           默认值：32
         */
        String masterConnectionMinimumIdleSizeValue = toString(configMap.get(masterConnectionMinimumIdleSize));
        if (masterConnectionMinimumIdleSizeValue != null) {
            baseConfig.setMasterConnectionMinimumIdleSize(Integer.parseInt(masterConnectionMinimumIdleSizeValue));
        }

      /*
          masterConnectionPoolSize（主节点连接池大小）
          主节点的连接池最大容量。连接池的连接数量自动弹性伸缩。
          默认值：64
         */
        String masterConnectionPoolSizeValue = toString(configMap.get(masterConnectionPoolSize));
        if (masterConnectionPoolSizeValue != null) {
            baseConfig.setMasterConnectionPoolSize(Integer.parseInt(masterConnectionPoolSizeValue));
        }


           /*
            readMode
            读取操作的负载均衡模式
            设置读取操作选择节点的模式。
            可用值为：
                      SLAVE - 只在从服务节点里读取。
                      MASTER - 只在主服务节点里读取。
                      MASTER_SLAVE - 在主从服务节点里都可以读取。
            注：在从服务节点里读取的数据说明已经至少有两个节点保存了该数据，确保了数据的高可用性。
            默认值： SLAVE（只在从服务节点里读取）
         */
        String readModeValue = toString(configMap.get(readMode));
        if (readModeValue != null) {
            baseConfig.setReadMode(Enum.valueOf(ReadMode.class, readModeValue.toUpperCase()));
        }


        /*
           subscriptionMode（订阅操作的负载均衡模式）
           设置订阅操作选择节点的模式。可用值为：SLAVE - 只在从服务节点里订阅。MASTER - 只在主服务节点里订阅。
           默认值：SLAVE（只在从服务节点里订阅）
         */
        String subscriptionModeValue = toString(configMap.get(subscriptionMode));
        if (subscriptionModeValue != null) {
            baseConfig.setSubscriptionMode(Enum.valueOf(SubscriptionMode.class, subscriptionModeValue.toUpperCase()));
        }

        /*
          subscriptionConnectionMinimumIdleSize（从节点发布和订阅连接的最小空闲连接数）
          多从节点的环境里，每个 从服务节点里用于发布和订阅连接的最小保持连接数（长连接）。
          Redisson内部经常通过发布和订阅来实现许多功能。
          长期保持一定数量的发布订阅连接是必须的。
          默认值：1
         */
        String subscriptionConnectionMinimumIdleSizeValue = toString(configMap.get(subscriptionConnectionMinimumIdleSize));
        if (subscriptionConnectionMinimumIdleSizeValue != null) {
            baseConfig.setSubscriptionConnectionMinimumIdleSize(Integer.parseInt(subscriptionConnectionMinimumIdleSizeValue));
        }

       /*
          subscriptionConnectionPoolSize（从节点发布和订阅连接池大小）
          多从节点的环境里，每个 从服务节点里用于发布和订阅连接的连接池最大容量。
          连接池的连接数量自动弹性伸缩。
          默认值：50
         */
        String subscriptionConnectionPoolSizeValue = toString(configMap.get(subscriptionConnectionPoolSize));
        if (subscriptionConnectionPoolSizeValue != null) {
            baseConfig.setSubscriptionConnectionPoolSize(Integer.parseInt(subscriptionConnectionPoolSizeValue));
        }

        /*
           dnsMonitoringInterval
           DNS监控间隔，单位：毫秒
           用来指定检查节点DNS变化的时间间隔。使用的时候应该确保JVM里的DNS数据的缓存时间保持在足够低的范围才有意义。用-1来禁用该功能。
           默认值：5000
         */
        String dnsMonitoringIntervalValue = toString(configMap.get(dnsMonitoringInterval));
        if (dnsMonitoringIntervalValue != null) {
            baseConfig.setDnsMonitoringInterval(Integer.parseInt(dnsMonitoringIntervalValue));
        }
    }


    /**
     * 配置基本配置
     *
     * @param baseConfig
     * @param redisClientConfig
     * @param configMap
     */
    private static void generateBaseConfig(BaseConfig baseConfig, RedisClientConfig redisClientConfig, Map configMap) throws MalformedURLException {

        /* sslEnableEndpointIdentification
         * 启用SSL终端识别
         * 开启SSL终端识别能力。
         * 默认值：true
         * */
        String sslEnableEndpointIdentificationValue = toString(configMap.get(sslEnableEndpointIdentification));
        if (sslEnableEndpointIdentificationValue != null) {
            baseConfig.setSslEnableEndpointIdentification(Boolean.parseBoolean(sslEnableEndpointIdentificationValue));
        }

        /* sslProvider
         * SSL实现方式
         * 确定采用哪种方式（JDK或OPENSSL）来实现SSL连接。
         * 默认值：JDK
         * */
        String sslProviderValue = toString(configMap.get(sslProvider));
        if (sslProviderValue != null) {
            baseConfig.setSslProvider(Enum.valueOf(SslProvider.class, sslProviderValue.toUpperCase()));
        }



        /* sslTruststore
         * SSL信任证书库路径
         * 指定SSL信任证书库的路径。
         * 默认值：null
         * */
        String sslTruststoreValue = toString(configMap.get(sslTruststore));
        if (sslTruststoreValue != null) {
            baseConfig.setSslTruststore(new URL(sslTruststoreValue));
        }


        /* sslTruststorePassword
         * SSL信任证书库密码
         * 指定SSL信任证书库的密码。
         * 默认值：null
         * */
        String sslTruststorePasswordValue = toString(configMap.get(sslTruststorePassword));
        if (sslTruststorePasswordValue != null) {
            baseConfig.setSslTruststorePassword(sslTruststorePasswordValue);
        }

        /* sslKeystore
         * SSL钥匙库路径
         * 指定SSL钥匙库的路径。
         * 默认值：null
         * */
        String sslKeystoreValue = toString(configMap.get(sslKeystore));
        if (sslKeystoreValue != null) {
            baseConfig.setSslKeystore(new URL(sslKeystoreValue));
        }

       /* sslKeystorePassword（SSL钥匙库密码）
        指定SSL钥匙库的密码。
        */
        String sslKeystorePasswordValue = toString(configMap.get(sslKeystorePassword));
        if (sslKeystorePasswordValue != null) {
            baseConfig.setSslKeystorePassword(sslKeystorePasswordValue);
        }



     /*
         idleConnectionTimeout（连接空闲超时，单位：毫秒）
         如果当前连接池里的连接数量超过了最小空闲连接数，而同时有连接空闲时间超过了该数值，那么这些连接将会自动被关闭，并从连接池里去掉。时间单位是毫秒。
         默认值：10000
       */
        String idleConnectionTimeoutValue = toString(configMap.get(idleConnectionTimeout));
        if (idleConnectionTimeoutValue != null) {
            baseConfig.setIdleConnectionTimeout(Integer.parseInt(idleConnectionTimeoutValue));
        }


        /*
           connectTimeout（连接超时，单位：毫秒）
           同任何节点建立连接时的等待超时。时间单位是毫秒。
            默认值：10000
         */
        String connectTimeoutValue = toString(configMap.get(connectTimeout));
        if (connectTimeoutValue != null) {
            baseConfig.setConnectTimeout(Integer.parseInt(connectTimeoutValue));
        }


        /*
         timeout（命令等待超时，单位：毫秒）
         等待节点回复命令的时间。该时间从命令发送成功时开始计时。
         默认值：3000
        */
        String timeoutValue = toString(configMap.get(timeout));
        if (timeoutValue != null) {
            baseConfig.setTimeout(Integer.parseInt(timeoutValue));
        }


        /*
          retryAttempts（命令失败重试次数）
          如果尝试达到 retryAttempts（命令失败重试次数） 仍然不能将命令发送至某个指定的节点时，将抛出错误。
          如果尝试在此限制之内发送成功，则开始启用 timeout（命令等待超时） 计时。
          默认值：3
        */
        String retryAttemptsValue = toString(configMap.get(retryAttempts));
        if (retryAttemptsValue != null) {
            baseConfig.setRetryAttempts(Integer.parseInt(retryAttemptsValue));
        }



      /*  retryInterval（命令重试发送时间间隔，单位：毫秒）
        在一条命令发送失败以后，等待重试发送的时间间隔。时间单位是毫秒。
        默认值：1500 */
        String retryIntervalValue = toString(configMap.get(retryInterval));
        if (retryIntervalValue != null) {
            baseConfig.setRetryInterval(Integer.parseInt(retryIntervalValue));
        }


        /* password
         * 密码
         * 用于节点身份验证的密码。
         * 默认值：null
         * */
        String passwordValue = toString(configMap.get(password));
        if (passwordValue != null) {
            baseConfig.setPassword(passwordValue);
        }

        /* username
         * 用戶名
         * 用于节点身份验证。
         * 默认值：null
         * */
        String usernameValue = toString(configMap.get(username));
        if (usernameValue != null) {
            baseConfig.setUsername(usernameValue);
        }


       /* subscriptionsPerConnection（单个连接最大订阅数量）
        每个连接的最大订阅数量。
        默认值：5 */
        String subscriptionsPerConnectionValue = toString(configMap.get(subscriptionsPerConnection));
        if (subscriptionsPerConnectionValue != null) {
            baseConfig.setSubscriptionsPerConnection(Integer.parseInt(subscriptionsPerConnectionValue));
        }



       /* clientName（客户端名称）
        在Redis节点里显示的客户端名称。
        默认值：null */
        String clientNameValue = toString(configMap.get(clientName));
        if (clientNameValue != null) {
            baseConfig.setClientName(clientNameValue);
        }

        /**
         * Defines PING command sending interval per connection to Redis.
         * <code>0</code> means disable.
         * <p>
         * Default is <code>30000</code>
         */
        String pingConnectionIntervalValue = toString(configMap.get(pingConnectionInterval));
        if (pingConnectionIntervalValue != null) {
            baseConfig.setPingConnectionInterval(Integer.parseInt(pingConnectionIntervalValue));
        }


        /**
         * Enables TCP keepAlive for connection
         * <p>
         * Default is <code>false</code>
         *
         */
        String keepAliveValue = toString(configMap.get(keepAlive));
        if (keepAliveValue != null) {
            baseConfig.setKeepAlive(Boolean.parseBoolean(keepAliveValue));
        }


        /**
         * Enables TCP noDelay for connection
         * <p>
         * Default is <code>false</code>
         */
        String tcpNoDelayValue = toString(configMap.get(tcpNoDelay));
        if (tcpNoDelayValue != null) {
            baseConfig.setTcpNoDelay(Boolean.parseBoolean(tcpNoDelayValue));
        }

    }


    /**
     * 将任意对象转换为String, 与String.value 的区别在于 null的时候不会转换为 "null"
     *
     * @param obj
     * @return
     */
    private static String toString(Object obj) {

        if (obj == null) {
            return null;
        } else {
            return obj.toString();
        }
    }


    /***
     * 用于 生成访问redis存储的实际key
     */
    protected final static String NAMESPACE_SPLIT = "_";

    public static String generateRealKey(String namespace, String key) {

        if (namespace == null) {
            return key;
        } else {
            return namespace + NAMESPACE_SPLIT + key;
        }
    }
}
