package com.tts.base.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tts.base.dao.BaseNodeHeartbeatMapper;
import com.tts.base.domain.BaseNodeHeartbeat;
import com.tts.base.service.BaseNodeHeartbeatLogService;
import com.tts.base.service.BaseNodeHeartbeatService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;

@Slf4j
@Service
public class BaseNodeHeartbeatServiceImpl extends ServiceImpl<BaseNodeHeartbeatMapper, BaseNodeHeartbeat>
        implements BaseNodeHeartbeatService {

    @Autowired
    private BaseNodeHeartbeatLogService nodeHeartbeatLogService;

    @Value("${zookeeper.node.heartbeatInterval}")
    private long HEARTBEAT_INTERVAL;
    @Value("${zookeeper.node.heartbeatTimeout}")
    private long HEARTBEAT_TIMEOUT;

    private String serviceName;

    /**
     * 心跳开始标志位
     */
    private volatile boolean heartbeatFlag = false;

    /**
     * 心跳进程
     */
    private Thread heartbeatThread;

    @Override
    public void startHeartbeat(String serviceName) {
        this.serviceName = serviceName;

        // 若已经开启心跳记录 则直接返回即可
        if (heartbeatFlag) {
            return;
        }

        // 开启线程来持续更新心跳
        startHeartbeatTask();

        heartbeatFlag = true;
    }

    /**
     * 开始记录心跳的线程任务
     */
    private void startHeartbeatTask() {
        try {
            // 初始化该节点的心跳记录
            initialServerHeartbeat();
            // 初始化心跳线程
            initialHeartbeatThread();

            // 开启线程执行任务
            heartbeatThread.start();
        } catch (Exception e) {
            log.error("Start Heartbeat Task Error", e);
        }
    }

    /**
     * 初始化该节点的心跳记录
     * 无则初始化一条，有的话就什么都不管了
     */
    private void initialServerHeartbeat() {
        // 根据名称先查出来
        LambdaQueryWrapper<BaseNodeHeartbeat> wrapper = new QueryWrapper<BaseNodeHeartbeat>()
                .lambda().eq(BaseNodeHeartbeat::getServerName, serviceName);
        BaseNodeHeartbeat nodeHeartbeat = baseMapper.selectOne(wrapper);

        if (nodeHeartbeat == null) {
            nodeHeartbeat = new BaseNodeHeartbeat(serviceName, LocalDateTime.now());
            baseMapper.insert(nodeHeartbeat);
        }
    }

    /**
     * 初始化心跳线程及其任务
     */
    private void initialHeartbeatThread() {
        // 定义线程任务
        heartbeatThread = new Thread(this::initialHeartbeatTask);

        heartbeatThread.setDaemon(true);
        heartbeatThread.setName("Heartbeat Thread");
    }

    /**
     * 初始化心跳记录的任务
     *
     * 每隔N秒记录一次心跳，并追加流水日志
     */
    private void initialHeartbeatTask() {
        while (true) {
            // 更新服务心跳
            updateServerHeartbeat(serviceName);

            // 记录心跳日志流水
            nodeHeartbeatLogService.appendHeartbeatLog(serviceName);

            try {
                Thread.sleep(HEARTBEAT_INTERVAL);
            } catch (Exception e) {
                log.error("Node heartbeat sleep error", e);
            }
        }
    }

    /**
     * 更新服务心跳
     */
    private void updateServerHeartbeat(String serviceName) {
        LambdaUpdateWrapper<BaseNodeHeartbeat> updateWrapper = new UpdateWrapper<BaseNodeHeartbeat>().lambda()
                .set(BaseNodeHeartbeat::getLatestHeartbeatTime, LocalDateTime.now())
                .eq(BaseNodeHeartbeat::getServerName, serviceName);

        baseMapper.update(null, updateWrapper);
    }

    @Override
    public List<BaseNodeHeartbeat> listByServerNames(List<String> serverNames) {
        Wrapper<BaseNodeHeartbeat> queryWrapper = new QueryWrapper<BaseNodeHeartbeat>()
                .lambda().in(BaseNodeHeartbeat::getServerName, serverNames);

        return baseMapper.selectList(queryWrapper);
    }

    @Override
    public boolean isTimeOut(BaseNodeHeartbeat nodeHeartbeat) {
        return nodeHeartbeat == null || isTimeOut(nodeHeartbeat.getLatestHeartbeatTime());
    }

    /**
     * 当前时间 - 上次心跳时间 > 超时时间间隔 则为超时
     */
    private boolean isTimeOut(LocalDateTime lastHeartbeatTime) {
        return LocalDateTime.now().toEpochSecond(ZoneOffset.UTC) - lastHeartbeatTime.toEpochSecond(ZoneOffset.UTC) > HEARTBEAT_TIMEOUT;
    }
}
