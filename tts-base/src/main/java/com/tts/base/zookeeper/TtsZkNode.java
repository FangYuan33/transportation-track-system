package com.tts.base.zookeeper;

import com.tts.base.zookeeper.properties.ZkNodeProperties;
import lombok.extern.slf4j.Slf4j;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.TreeCache;
import org.apache.curator.framework.recipes.leader.LeaderSelector;
import org.apache.curator.framework.recipes.leader.LeaderSelectorListenerAdapter;
import org.apache.curator.retry.RetryForever;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.Closeable;
import java.time.LocalDateTime;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * TTS服务节点，分 master 和 follower，借助zookeeper实现集群高可用
 * <p>
 * 实现Closeable为了更优雅的释放资源
 * 实现InitializingBean为了初始化节点字段信息
 *
 * @author FangYuan
 * @since 2022-12-26 15:05:18
 */
@Slf4j
@Component
public class TtsZkNode extends LeaderSelectorListenerAdapter implements Closeable, InitializingBean {

    @Autowired
    private ZkNodeProperties nodeProperties;

    private CuratorFramework curatorFramework;
    /**
     * 监听器
     */
    private LeaderSelector leaderSelector;
    /**
     * 节点监听器
     */
    private TreeCache treeCache;

    /**
     * 是否是Leader的标志位
     */
    private volatile AtomicBoolean isLeader;
    /**
     * 成为Leader的时间点
     */
    private volatile LocalDateTime isLeaderTime;

    @Override
    public void afterPropertiesSet() {
        RetryForever retryForever = new RetryForever(nodeProperties.getRetryCountInterval());
        curatorFramework = CuratorFrameworkFactory.newClient(nodeProperties.getAddress(),
                nodeProperties.getSessionTimeout(), nodeProperties.getConnectTimeout(), retryForever);

        leaderSelector = new LeaderSelector(curatorFramework, nodeProperties.getMasterPath(), this);
        treeCache = new TreeCache(curatorFramework, nodeProperties.getTreePath());

        // 标记为普通节点
        isLeader = new AtomicBoolean(false);

        /*
         当节点执行完takeLeadership()方法时，它会放弃Leader的身份
         此时Curator会从剩余的节点中再选出一个节点作为新的Leader
         调用autoRequeue()方法使放弃Leader身份的节点有机会重新成为Leader
         如果不执行该方法的话，那么该节点就不会再变成leader了
         */
        leaderSelector.autoRequeue();
    }

    /**
     * 当前节点被选中作为群首Leader时，会调用执行此方法
     * 注: 由于此方法结束后，对应的节点就会放弃leader，所以我们不能让此方法马上结束
     *
     * @param curatorFramework curator客户端
     */
    @Override
    public void takeLeadership(CuratorFramework curatorFramework) {
        try {
            log.info("TTS Node {} is Leader!", nodeProperties.getServiceName());

            isLeader.set(true);
            isLeaderTime = LocalDateTime.now();

            // 记录成为Leader的日志
            log.info("记录Leader日志");

            // 注册路径子节点监听器

            Thread.currentThread().join();
        } catch (Exception e) {
            log.error("error", e);

            Thread.currentThread().interrupt();
        } finally {
            log.info("TTS Node {} isn't Leader!", nodeProperties.getServiceName());

            isLeader.set(false);
            isLeaderTime = null;
            // 取消监听器

            // 记录没有成为Leader的日志
        }
    }

    @Override
    public void close() {
        log.info("TTS Node {} close!!!", nodeProperties.getServiceName());

        try {
            treeCache.close();
            treeCache = null;
        } catch (Exception e) {
            log.error("Close TreeCache Error", e);
        }

        try {
            leaderSelector.close();
            leaderSelector = null;
        } catch (Exception e) {
            log.error("Close LeaderSelector Error", e);
        }

        try {
            curatorFramework.close();
            curatorFramework = null;
        } catch (Exception e) {
            log.error("Close CuratorFramework Error", e);
        }

        log.info("TTS Node {} close over!!!", nodeProperties.getServiceName());
    }


}
