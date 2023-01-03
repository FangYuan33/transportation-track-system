package com.tts.base.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tts.base.dao.BaseNodeHeartbeatLogMapper;
import com.tts.base.domain.BaseNodeHeartbeatLog;
import com.tts.base.service.BaseNodeHeartbeatLogService;
import org.springframework.stereotype.Service;

@Service
public class BaseNodeHeartbeatLogImpl extends ServiceImpl<BaseNodeHeartbeatLogMapper, BaseNodeHeartbeatLog>
        implements BaseNodeHeartbeatLogService {

}
