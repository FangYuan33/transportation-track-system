package com.tts.base.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tts.base.dao.BaseNodeHeartbeatMapper;
import com.tts.base.domain.BaseNodeHeartbeat;
import com.tts.base.service.BaseNodeHeartbeatService;
import org.springframework.stereotype.Service;

@Service
public class BaseNodeHeartbeatServiceImpl extends ServiceImpl<BaseNodeHeartbeatMapper, BaseNodeHeartbeat>
        implements BaseNodeHeartbeatService {

}
