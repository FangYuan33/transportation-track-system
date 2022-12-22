package com.tts.base.service.impl;

import com.tts.base.constant.BaseConstant;
import com.tts.base.dao.BaseNodeHeartbeatLogMapper;
import com.tts.base.dao.BaseNodeHeartbeatMapper;
import com.tts.base.domain.BaseNodeHeartbeat;
import com.tts.base.domain.BaseNodeHeartbeatLog;
import com.tts.base.service.BaseNodeHeartbeatService;
import com.tts.framework.common.utils.IpUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Slf4j
@Service
public class BaseNodeHeartbeatServiceImpl implements BaseNodeHeartbeatService {

    @Resource
    private BaseNodeHeartbeatMapper baseNodeHeartbeatMapper;

    @Resource
    private BaseNodeHeartbeatLogMapper baseNodeHeartbeatLogMapper;

    // 心跳线程
    private Thread heartBeatThread;
    // 心跳是否启动的标记位 true 为 已经启动
    private volatile boolean heartBeatRunFlag = false;
    // 本机ip
    private String localIp;

    /**
     * 启动心跳线程
     * 每个进程就一个线程
     * 周期性记录心跳记录
     */
    @Override
    public synchronized void startHearbeat() {
        if (heartBeatRunFlag) {
            return;
        }
        localIp = IpUtils.getHostIp();

        // 初始化该节点心跳,即查询是否存在当前ip的心跳记录
        // 如果不存在则插入一条,如果存在则忽略
        this.initHeartbeat();

        heartBeatThread = new Thread(() -> {
            while (true) {
                try {
                    BaseNodeHeartbeatServiceImpl.this.updateHeartbeat();
                } catch (Exception e) {
                    log.error("base heartbeat update error !", e);
                }

                try {
                    // 心跳更新间隔
                    Thread.sleep(BaseConstant.HEARTBEAT_INTERVAL);
                } catch (InterruptedException e) {
                    log.error("base heartbeat sleep error !", e);
                }
            }
        });
        heartBeatThread.setName("baseHeartbeatThread");
        heartBeatThread.setDaemon(true);

        heartBeatThread.start();
        heartBeatRunFlag = true;
    }

    @Override
    public void updateHeartbeat() {
        BaseNodeHeartbeat heartbeat = new BaseNodeHeartbeat();
        heartbeat.setServerIp(localIp);
        heartbeat.setLatestHeartbeatTime(LocalDateTime.now());

        // 记录心跳流水
        appendNodeHeartbeatLog(heartbeat);

        // 更新服务器的最新心跳时间
        baseNodeHeartbeatMapper.updateBaseNodeHeartbeatById(heartbeat);
    }

    @Override
    public void initHeartbeat() {
        BaseNodeHeartbeat baseNodeHeartbeat = baseNodeHeartbeatMapper.selectBaseNodeHeartbeatById(localIp);

        if (baseNodeHeartbeat == null) {
            // 插入心跳记录
            BaseNodeHeartbeat heartbeat = new BaseNodeHeartbeat();
            heartbeat.setServerIp(localIp);
            heartbeat.setLatestHeartbeatTime(LocalDateTime.now());
            baseNodeHeartbeatMapper.insert(heartbeat);

            // 插入心跳流水
            this.appendNodeHeartbeatLog(heartbeat);
        }
    }

    @Override
    public void appendNodeHeartbeatLog(BaseNodeHeartbeat heartbeat) {
        BaseNodeHeartbeatLog baseNodeHeartbeatLog = new BaseNodeHeartbeatLog();

        baseNodeHeartbeatLog.setServerIp(heartbeat.getServerIp());
        baseNodeHeartbeatLog.setHeartbeatTime(new Date());

        this.baseNodeHeartbeatLogMapper.insert(baseNodeHeartbeatLog);
    }

    @Override
    public List<BaseNodeHeartbeat> queryInServerList(List<String> serverIpList) {
        if (serverIpList.size() == 0) {
            return new ArrayList<>();
        }
        return baseNodeHeartbeatMapper.selectInServerList(serverIpList);
    }

    @Override
    public boolean isTimeout(BaseNodeHeartbeat baseNodeHeartbeat) {
        boolean isNotTimeout = false;

        if (baseNodeHeartbeat != null && baseNodeHeartbeat.getLatestHeartbeatTime() != null
                && ((System.currentTimeMillis() - baseNodeHeartbeat.getLatestHeartbeatTime().toInstant(ZoneOffset.of("+8")).toEpochMilli())
                <= (2 * BaseConstant.HEARTBEAT_INTERVAL))) {

            isNotTimeout = true;
        }

        return !isNotTimeout;
    }
}
