package com.tts.iov.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tts.iov.dao.IovSubscribeTaskMapper;
import com.tts.iov.domain.IovSubscribeTask;
import com.tts.iov.service.IovSubscribeTaskService;
import org.springframework.stereotype.Service;

@Service
public class IovSubscribeTaskServiceImpl extends ServiceImpl<IovSubscribeTaskMapper, IovSubscribeTask>
        implements IovSubscribeTaskService {

}
