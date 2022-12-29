package com.tts.base.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tts.base.dao.BaseServerStateLogMapper;
import com.tts.base.domain.BaseServerStateLog;
import com.tts.base.service.BaseServerStateLogService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class BaseServerStateLogServiceImpl extends ServiceImpl<BaseServerStateLogMapper, BaseServerStateLog>
        implements BaseServerStateLogService {

    @Override
    public void saveNewState(String serviceName, String state) {
        BaseServerStateLog entity = new BaseServerStateLog()
                .setServerName(serviceName).setState(state).setCreateTime(LocalDateTime.now());

        baseMapper.insert(entity);
    }
}
