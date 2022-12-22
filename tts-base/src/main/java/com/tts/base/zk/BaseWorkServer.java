package com.tts.base.zk;

import com.tts.base.constant.BaseServerStatus;
import com.tts.base.constant.BaseServerType;
import com.tts.base.dto.BaseServerDto;
import com.tts.base.service.BaseServerService;
import com.tts.base.utils.BaseUtilTool;
import lombok.extern.slf4j.Slf4j;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.TreeCache;
import org.apache.curator.framework.recipes.cache.TreeCacheListener;
import org.apache.curator.framework.recipes.leader.LeaderSelector;
import org.apache.curator.framework.recipes.leader.LeaderSelectorListenerAdapter;
import org.apache.curator.retry.RetryForever;
import org.apache.zookeeper.CreateMode;
import org.springframework.core.env.Environment;

import java.io.Closeable;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * 集群中的服务器，用于多个节点进行 master 和 slave 的 选举
 * 在项目启动后开始自动运行
 * 注: 继承LeaderSelectorListenerAdapter类的目的是: 重写takeLeadership方法，需要时还可重写stateChasnged方法
 * 注: 实现Closeable的目的是: 更优雅地关闭释放资源
 *
 * @author FangYuan
 * @since 2022-12-21 20:53:57
 */
@Slf4j
public class BaseWorkServer extends LeaderSelectorListenerAdapter implements Closeable {
    private static final BaseWorkServer WORK_SERVER = new BaseWorkServer();

    private static final String IP_PORT_KEY = "zookeeper.address";
    private static final String SESSION_TIMEOUT_KEY = "zookeeper.session_timeout";
    private static final String CONNECT_TIMEOUT_KEY = "zookeeper.connect_timeout";
    private static final String RETRY_COUNT_INTERVAL_KEY = "zookeeper.retry_count_interval";
    private static final String PATH_MASTER_SLAVE_KEY = "zookeeper.path.master_slave";
    private static final String SERVER_IP_KEY = "zookeeper.path.server_ip";

    private String ipPort;
    private int sessionTimeout;
    private int connectTimeout;
    private int retryInterval;
    private String masterPath;

    private CuratorFramework client;

    private String serverIpPath;

    /**
     * 服务器的基本属性，主要用来区分是不同的服务器
     */
    private String serverName;
    /**
     * curator提供的监听器
     */
    private LeaderSelector leaderSelector;

    private volatile AtomicBoolean isLeader;
    /**
     * 成为leader的时间戳
     */
    private volatile Long isLeaderTime;
    /**
     * spring 环境变量
     */
    private Environment env;
    /**
     * base server service
     */
    private BaseServerService baseServerService;
    /**
     * zk节点的监听器
     */
    private TreeCache treeCache;
    /**
     * 用于master的 路径节点监听器
     */
    private volatile TreeCacheListener treeCacheListener;

    /**
     * 无参构造函数
     */
    public BaseWorkServer() {
    }

    public static void init4Test(String ipPort, int sessionTimeout, int connectTimeout,
                                 int retryInterval, String masterPath, String serverIpPath, String serverName) {

        WORK_SERVER.ipPort = ipPort;
        WORK_SERVER.sessionTimeout = sessionTimeout;
        WORK_SERVER.connectTimeout = connectTimeout;
        WORK_SERVER.retryInterval = retryInterval;
        WORK_SERVER.masterPath = masterPath;
        WORK_SERVER.serverIpPath = serverIpPath;
        WORK_SERVER.serverName = serverName;
        WORK_SERVER.initCurator();
    }

    /**
     * 初始化
     */
    public static void init(Environment env, BaseServerService baseServerService) {
        WORK_SERVER.env = env;
        WORK_SERVER.ipPort = env.getProperty(IP_PORT_KEY);
        WORK_SERVER.sessionTimeout = Integer.parseInt(env.getProperty(SESSION_TIMEOUT_KEY));
        WORK_SERVER.connectTimeout = Integer.parseInt(env.getProperty(CONNECT_TIMEOUT_KEY));
        WORK_SERVER.retryInterval = Integer.parseInt(env.getProperty(RETRY_COUNT_INTERVAL_KEY));
        WORK_SERVER.masterPath = env.getProperty(PATH_MASTER_SLAVE_KEY);
        WORK_SERVER.serverIpPath = env.getProperty(SERVER_IP_KEY);
        WORK_SERVER.serverName = BaseUtilTool.getLocalIpByNetcard();

        WORK_SERVER.baseServerService = baseServerService;
        WORK_SERVER.initCurator();
    }

    public static boolean isLeader() {
        try {
            return WORK_SERVER.isLeader.get();
        } catch (Exception e) {
            log.error("isLeader error ", e);
            return false;
        }
    }

    /**
     * 获取成为Leader的时间戳
     */
    public static Long getLeaderTime() {
        return WORK_SERVER.isLeaderTime;
    }

    public static String getLeaderIp() {
        try {
            return WORK_SERVER.leaderSelector.getLeader().getId();
        } catch (Exception e) {
            log.error("getLeaderIp error ", e);
            return "";
        }
    }

    /**
     * 初始化 curator client
     */
    public void initCurator() {
        RetryPolicy retryPolicy = new RetryForever(retryInterval);

        WORK_SERVER.baseServerService = baseServerService;

        WORK_SERVER.client = CuratorFrameworkFactory.newClient(ipPort, sessionTimeout, connectTimeout, retryPolicy);

        //serverName 默认为当前机器的ip
        WORK_SERVER.serverName = serverName;
        WORK_SERVER.leaderSelector = new LeaderSelector(WORK_SERVER.client, masterPath, WORK_SERVER);
        WORK_SERVER.isLeader = new AtomicBoolean(false);
        WORK_SERVER.serverIpPath = serverIpPath;
        WORK_SERVER.treeCache = new TreeCache(WORK_SERVER.client, serverIpPath);

        /*
         *  当WorkServer从takeLeadership()退出时它就会放弃了Leader身份，
         *  这时Curator会利用Zookeeper再从剩余的WorkServer中选出一个新的Leader。
         *  调用 autoRequeue()方法使放弃Leader身份的WorkServer有机会重新获得Leader身份，
         *  如果不设置的话放弃了的WorkServer是不会再变成Leader的。
         */
        WORK_SERVER.leaderSelector.autoRequeue();
    }

    public static void start() {
        WORK_SERVER.startInner();
    }

    /**
     * LeaderSelector提供好了的 竞争群首的方法，直接调用即可
     */
    public void startInner() {
        log.info("##### BaseWorkServer[" + getServerName() + "] start start !!!");

        client.start();
        leaderSelector.start();
        this.register();

        log.info("### BaseWorkServer[" + getServerName() + "] start end !!!");
    }

    public void register() {
        log.info("BaseWorkServer[" + getServerName() + "] register thread start !!! ");

        while (true) {
            try {
                log.info("BaseWorkServer[" + getServerName() + "] register  start !!! ");
                ZkConnectionStateListener zkConnectionStateListener =
                        new ZkConnectionStateListener(serverIpPath, getServerName(), this, baseServerService);
                client.getConnectionStateListenable().addListener(zkConnectionStateListener);

                // 当前时间字符串
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");

                client.create().creatingParentContainersIfNeeded().withMode(CreateMode.EPHEMERAL)
                        .forPath(serverIpPath + "/" + getServerName(), sdf.format(new Date()).getBytes(StandardCharsets.UTF_8));
                log.info("BaseWorkServer[" + getServerName() + "] register end !!!");
                break;
            } catch (Exception e) {
                log.error("BaseWorkServer[" + getServerName() + "] register error !!!", e);
            }

            try {
                Thread.sleep(1000L);
            } catch (InterruptedException e) {
                log.error("BaseWorkServer register sleep error !!!", e);
            }
        }
    }

    /**
     * 关闭此Server，并从竞争群首组里面移除此Server成员
     */
    @Override
    public void close() {
        this.stopInner();
        log.info("BaseWorkServer[" + getServerName() + "] close ！");
    }

    public void stopInner() {
        log.info("BaseWorkServer[" + getServerName() + "] stop start !!!");
        try {
            treeCache.close();
            treeCache = null;
        } catch (Exception e) {
            log.error("stop  treeCache error ", e);
        }
        try {
            leaderSelector.close();
            leaderSelector = null;
        } catch (Exception e) {
            log.error("stop  leaderSelector error ", e);
        }
        try {
            client.close();
            client = null;
        } catch (Exception e) {
            log.error("stop  client error ", e);
        }
        log.info("BaseWorkServer[" + getServerName() + "] stop end !!!");
    }

    public static List<String> getServerIpList() {
        return WORK_SERVER.getServerIpListInner();
    }

    /**
     * 获取所有注册的server ip
     */
    public List<String> getServerIpListInner() {
        try {
            if (client != null && client.isStarted()) {
                return client.getChildren().forPath(serverIpPath);
            }
            return new ArrayList<>();
        } catch (Exception e) {
            log.error("BaseWorkServer getAllServerIp  error !!!", e);
            return new ArrayList<>();
        }
    }

    public static List<BaseServerDto> getServerDtoList() {
        return WORK_SERVER.getServerDtoListInner();
    }

    /**
     * 获取所有注册的server信息
     */
    public List<BaseServerDto> getServerDtoListInner() {
        List<String> serverIpList = getServerIpListInner();

        List<BaseServerDto> baseServerDtoList = new ArrayList<>();

        String leaderIp = getLeaderIp();

        for (String serverIp : serverIpList) {
            BaseServerDto baseServerDto = new BaseServerDto();
            baseServerDto.setServerIp(serverIp);

            try {
                String time = new String(client.getData().forPath(serverIpPath + "/" + serverIp));
                baseServerDto.setTime(time);
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (serverIp.equals(leaderIp)) {
                baseServerDto.setServerType(BaseServerType.MASTER.getName());
            } else {
                baseServerDto.setServerType(BaseServerType.SLAVER.getName());
            }


            baseServerDtoList.add(baseServerDto);
        }

        return baseServerDtoList;
    }

    /**
     * 当前服务器被选中作为群首Leader时，会调用执行此方法！
     * <p>
     * 注:由于此方法结束后，对应的workerServer就会放弃leader，所以我们不能让此方法马上结束
     *
     * @param client curator客户端
     */
    @Override
    public void takeLeadership(CuratorFramework client) {
        try {
            log.info("BaseWorkServer[" + getServerName() + "] is Leader ！");
            isLeader.set(true);
            isLeaderTime = System.currentTimeMillis();

            // 记录成为 leader
            if (baseServerService != null) {
                baseServerService.saveServerLog(getServerName(), BaseServerStatus.MASTER.getName());
            }
            // 开始监听注册路径下的节点的变化
            registerWatcher(serverIpPath);

            Thread.currentThread().join();
        } catch (Exception e) {
            // 当此方法未运行完就调用了close()方法时，就会触发此异常
            // 记录一下InterruptedException的发生
            log.error("error", e);
            Thread.currentThread().interrupt();
        } finally {
            log.info("BaseWorkServer[" + getServerName() + "] is not Leader ！");
            isLeader.set(false);
            isLeaderTime = null;
            // 取消监听
            unregisterWatcher();
            // 记录没有成为leader
            if (baseServerService != null) {
                baseServerService.saveServerLog(getServerName(), BaseServerStatus.UNMASTER.getName());
            }
        }
    }

    /**
     * 注册路径子节点的监听器
     */
    private void registerWatcher(String path) {
        if (treeCacheListener != null) {
            unregisterWatcher();
        }

        treeCacheListener = new ZKTreeCacheListener(path, baseServerService);
        treeCache.getListenable().addListener(treeCacheListener);
        try {
            treeCache.start();
        } catch (Exception e) {
            log.error("BaseWorkServer treeCache start  error !!!", e);
        }
    }

    /**
     * 反注册路径子节点的监听器
     */
    private void unregisterWatcher() {
        if (treeCacheListener != null) {
            treeCache.getListenable().removeListener(treeCacheListener);
        }
    }

    public String getServerName() {
        return serverName;
    }

    public LeaderSelector getLeaderSelector() {
        return leaderSelector;
    }

}
