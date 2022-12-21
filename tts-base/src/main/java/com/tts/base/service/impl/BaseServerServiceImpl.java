package com.tts.base.service.impl;

import com.tts.base.dao.BaseServerLogMapper;
import com.tts.base.domain.BaseServerLog;
import com.tts.base.dto.BaseServerDto;
import com.tts.base.service.BaseServerService;
import com.tts.base.zk.BaseWorkServer;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 服务器管理 service 实现类
 *
 * @author FangYuan
 * @since 2022-12-21 20:49:24
 */
@Service
public class BaseServerServiceImpl implements BaseServerService {

    @Resource
    private BaseServerLogMapper baseServerLogMapper;

    @Override
    public void saveServerLog(String serverIp, String status) {
        BaseServerLog baseServerLog = new BaseServerLog();
        baseServerLog.setServerIp(serverIp);
        baseServerLog.setStatus(status);
        baseServerLog.setCreateTime(LocalDateTime.now());
        baseServerLogMapper.insert(baseServerLog);
    }

    @Override
    public List<BaseServerDto> queryServerList() {
        return BaseWorkServer.getServerDtoList();
    }

    @Override
    public List<BaseServerLog> queryServerLogList(BaseServerLog baseServerLog) {
        return baseServerLogMapper.selectBaseServerLogList(baseServerLog);
    }
}
