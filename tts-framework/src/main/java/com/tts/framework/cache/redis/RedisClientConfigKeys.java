package com.tts.framework.cache.redis;

/**
 * redis  client 配置信息的key
 *
 * @author FangYuan
 * @since 2021-01-19
 */
public interface RedisClientConfigKeys {


    /**
     * 开关,是否开启redisclient
     */
    String redisSwitch = "redisSwitch";

    /**
     * 默认消息的有效期,单位是分钟.
     * 当前为默认 为 一周 的时间
     * 如果设置为-1则表示没有有效期
     */
    String defaultTTL = "defaultTTL";

    /**
     * Redis Cluster Mode
     * <p>
     * single -> single redis server
     * sentinel -> master-slaves servers
     * cluster -> cluster servers (数据库配置无效，使用 database = 0）
     * masterslave --> masterslave
     */
    String redisMode = "redisMode";

    /* codec
     * 编码
     * Redisson的对象编码类是用于将对象进行序列化和反序列化，以实现对该对象在Redis里的读取和存储。
     * Default is FST codec
     * 默认值: org.redisson.codec.FstCodec
     * */
    String codec = "codec";

    /* threads 线程池数量
     * 这个线程池数量被所有RTopic对象监听器，RRemoteService调用者和RExecutorService任务共同共享。
     * 默认值: 16
     * 如果设置为0,则为当前处理核数量 * 2
     */
    String threads = "threads";

    /* nettyThreads
     * Netty线程池数量
     * 这个线程池数量是在一个Redisson实例内，被其创建的所有分布式数据类型和服务，
     * 以及底层客户端所一同共享的线程池里保存的线程数量。
     * 默认值: 32
     * 如果设置为0则为当前处理核数量 * 2
     * */
    String nettyThreads = "nettyThreads";


    /* lockWatchdogTimeout
					 监控锁的看门狗超时，单位：毫秒
					 监控锁的看门狗超时时间单位为毫秒。
					 该参数只适用于分布式锁的加锁请求中未明确使用leaseTimeout参数的情况。
					 如果该看门口未使用lockWatchdogTimeout去重新调整一个分布式锁的lockWatchdogTimeout超时，那么这个锁将变为失效状态。
					 这个参数可以用来避免由Redisson客户端节点宕机或其他原因造成死锁的情况。
					 默认值：30000
				 */
    String lockWatchdogTimeout = "lockWatchdogTimeout";


    /*
					keepPubSubOrder
					保持订阅发布顺序
					通过该参数来修改是否按订阅发布消息的接收顺序出来消息，如果选否将对消息实行并行处理，该参数只适用于订阅发布消息的情况。
					默认值：true
			 */
    String keepPubSubOrder = "keepPubSubOrder";


    /* sslEnableEndpointIdentification
     * 启用SSL终端识别
     * 开启SSL终端识别能力。
     * 默认值：true
     * */
    String sslEnableEndpointIdentification = "sslEnableEndpointIdentification";


    /* sslProvider
     * SSL实现方式
     * 确定采用哪种方式（JDK或OPENSSL）来实现SSL连接。
     * 默认值：JDK
     */
    String sslProvider = "sslProvider";


    /* sslTruststore
     * SSL信任证书库路径
     * 指定SSL信任证书库的路径。
     * 默认值：null
     * */
    String sslTruststore = "sslTruststore";


    /* sslTruststorePassword
     * SSL信任证书库密码
     * 指定SSL信任证书库的密码。
     * 默认值：null
     * */
    String sslTruststorePassword = "sslTruststorePassword";


    /* sslKeystore
     * SSL钥匙库路径
     * 指定SSL钥匙库的路径。
     * 默认值：null
     * */
    String sslKeystore = "sslKeystore";


    /* sslKeystorePassword（SSL钥匙库密码）
			 指定SSL钥匙库的密码。
		*/
    String sslKeystorePassword = "sslKeystorePassword";


    /* address（节点地址)
     * 可以通过host:port的格式来指定节点地址
     * */
    String address = "address";


    /* subscriptionConnectionMinimumIdleSize
     * 发布和订阅连接的最小空闲连接数
     * 用于发布和订阅连接的最小保持连接数（长连接）。
     * Redisson内部经常通过发布和订阅来实现许多功能。
     * 长期保持一定数量的发布订阅连接是必须的。
     * 默认值: 1
     * */
    String subscriptionConnectionMinimumIdleSize = "subscriptionConnectionMinimumIdleSize";


    /* subscriptionConnectionPoolSize
     * 发布和订阅连接池大小
     * 用于发布和订阅连接的连接池最大容量。
     * 连接池的连接数量自动弹性伸缩。
     * 默认值: 50
     * */
    String subscriptionConnectionPoolSize = "subscriptionConnectionPoolSize";


    /* connectionPoolSize
     * 连接池大小
     * 连接池最大容量。
     * 连接池的连接数量自动弹性伸缩。
     * 默认值: 64
     * */
    String connectionPoolSize = "connectionPoolSize";


    /* dnsMonitoringInterval
     * DNS监测时间间隔，单位：毫秒
     * 监测DNS的变化情况的时间间隔。
     * 默认值：5000
     * */
    String dnsMonitoringInterval = "dnsMonitoringInterval";


    /* idleConnectionTimeout
     * 连接空闲超时，单位：毫秒
     * 如果当前连接池里的连接数量超过了最小空闲连接数，而同时有连接空闲时间超过了该数值，那么这些连接将会自动被关闭，并从连接池里去掉。
     * 时间单位是毫秒。
     * 默认值：10000
     * */
    String idleConnectionTimeout = "idleConnectionTimeout";


    /* connectTimeout
     * 连接超时，单位：毫秒
     * 同节点建立连接时的等待超时。时间单位是毫秒。
     * 默认值：10000
     * */
    String connectTimeout = "connectTimeout";


    /* timeout
     * 命令等待超时，单位：毫秒
     * 等待节点回复命令的时间。该时间从命令发送成功时开始计时。
     * 默认值：3000
     * */
    String timeout = "timeout";


    /* retryAttempts
     * 命令失败重试次数
     * 如果尝试达到 retryAttempts（命令失败重试次数） 仍然不能将命令发送至某个指定的节点时，将抛出错误。
     * 如果尝试在此限制之内发送成功，则开始启用 timeout（命令等待超时） 计时。
     * 默认值：3
     * */
    String retryAttempts = "retryAttempts";

    /* retryInterval
     * 命令重试发送时间间隔，单位：毫秒
     * 在一条命令发送失败以后，等待重试发送的时间间隔。时间单位是毫秒。
     * 默认值：1500*/
    String retryInterval = "retryInterval";

    /* database
     * 数据库编号
     * 尝试连接的数据库编号。
     * 默认值：0
     * */
    String database = "database";


    /* password
     * 密码
     * 用于节点身份验证的密码。
     * 默认值：null
     * */
    String password = "password";


    /* subscriptionsPerConnection
     * 单个连接最大订阅数量
     * 每个连接的最大订阅数量。
     * 默认值：5
     * */
    String subscriptionsPerConnection = "subscriptionsPerConnection";


    /* clientName
     * 客户端名称
     * 在Redis节点里显示的客户端名称。
     * 默认值：null
     * */
    String clientName = "clientName";


    /*
      masterName（主服务器的名称）
      主服务器的名称是哨兵进程中用来监测主从服务切换情况的。
    */
    String masterName = "masterName";

    /*
      sentinelAddresses（添加哨兵节点地址）
      可以通过host:port的格式来指定哨兵节点的地址。多个节点可以一次性批量添加。
    */
    String sentinelAddresses = "sentinelAddresses";

    /*
      readMode（读取操作的负载均衡模式）
      设置读取操作选择节点的模式。
              可用值为：
                       SLAVE - 只在从服务节点里读取。
                       MASTER - 只在主服务节点里读取。
                       MASTER_SLAVE - 在主从服务节点里都可以读取
       注：在从服务节点里读取的数据说明已经至少有两个节点保存了该数据，确保了数据的高可用性。
       默认值： SLAVE（只在从服务节点里读取）
     */
    String readMode = "readMode";


    /*
		 subscriptionMode（订阅操作的负载均衡模式）
		 设置订阅操作选择节点的模式。
				可用值为：
									 SLAVE - 只在从服务节点里订阅。
									 MASTER - 只在主服务节点里订阅。
		 默认值：MASTER（只在从服务节点里订阅）
		*/
    String subscriptionMode = "subscriptionMode";


    /*
		 loadBalancer（负载均衡算法类的选择）
		 在使用多个Redis服务节点的环境里，可以选用以下几种负载均衡方式选择一个节点：
									org.redisson.connection.balancer.WeightedRoundRobinBalancer - 权重轮询调度算法
									org.redisson.connection.balancer.RoundRobinLoadBalancer - 轮询调度算法
									org.redisson.connection.balancer.RandomLoadBalancer - 随机调度算法
			默认值： org.redisson.connection.balancer.RoundRobinLoadBalancer
	 */
    String loadBalancer = "loadBalancer";


    /*
       slaveConnectionMinimumIdleSize（从节点最小空闲连接数）
       多从节点的环境里，每个 从服务节点里用于普通操作（非 发布和订阅）的最小保持连接数（长连接）。
       长期保持一定数量的连接有利于提高瞬时读取反映速度。
       默认值：32
     */
    String slaveConnectionMinimumIdleSize = "slaveConnectionMinimumIdleSize";


    /*
       slaveConnectionPoolSize（从节点连接池大小）
       多从节点的环境里，每个 从服务节点里用于普通操作（非 发布和订阅）连接的连接池最大容量。
       连接池的连接数量自动弹性伸缩。
       默认值：64
     */
    String slaveConnectionPoolSize = "slaveConnectionPoolSize";


    /*
		 masterConnectionMinimumIdleSize（主节点最小空闲连接数）
		 多从节点的环境里，每个 主节点的最小保持连接数（长连接）。
		 长期保持一定数量的连接有利于提高瞬时写入反应速度。
		 默认值：32
		*/
    String masterConnectionMinimumIdleSize = "masterConnectionMinimumIdleSize";


    /*
		 masterConnectionPoolSize（主节点连接池大小）
		 主节点的连接池最大容量。连接池的连接数量自动弹性伸缩。
		 默认值：64
		*/
    String masterConnectionPoolSize = "masterConnectionPoolSize";


    /*
		redis slave 重连间隔,当其已从可用服务器的内部列表中被排除的时候
		在每个此类超时事件中，Redisson都会尝试连接到已断开连接的Redis服务器。
		默认值 3000 毫秒
	 */
    String failedSlaveReconnectionInterval = "failedSlaveReconnectionInterval";


    /*  nodeAddresses（添加节点地址）
			可以通过host:port的格式来添加Redis集群节点的地址。多个节点可以一次性批量添加。*/
    String nodeAddresses = "nodeAddresses";


    /* scanInterval（集群扫描间隔时间）
			对Redis集群节点状态扫描的时间间隔。单位是毫秒。
			默认值： 1000 */
    String scanInterval = "scanInterval";


    /*
			masterAddress
			主节点地址
			可以通过host:port的格式来指定主节点地址。
		*/
    String masterAddress = "masterAddress";


    /*
				slaveAddresses
				添加从主节点地址
				可以通过host:port的格式来指定从节点的地址。多个节点可以一次性批量添加。
		*/
    String slaveAddresses = "slaveAddresses";


    /*
			failedSlaveCheckInterval
			失败从节点校验间隔时间
			默认值 180000 毫秒
		 */
    String failedSlaveCheckInterval = "failedSlaveCheckInterval";


    /*
    用戶名
     */
    String username = "username";


    /*
       Defines PING command sending interval per connection to Redis.
     */
    String pingConnectionInterval = "pingConnectionInterval";


    /*
        Enables TCP keepAlive for connection
     */
    String keepAlive = "keepAlive";

    /*
    Enables TCP noDelay for connection
     */
    String tcpNoDelay = "tcpNoDelay";

    /*
    Minimum idle Redis connection amount
     */
    String connectionMinimumIdleSize = "connectionMinimumIdleSize";

    /*
    Enables sentinels list check during Redisson startup.
     */
    String checkSentinelsList = "checkSentinelsList";

    /*
    Enables cluster slots check during Redisson startup.
     */
    String checkSlotsCoverage = "checkSlotsCoverage";


}
