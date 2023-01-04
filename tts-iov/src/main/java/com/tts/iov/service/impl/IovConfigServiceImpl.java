package com.tts.iov.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tts.iov.dao.IovConfigMapper;
import com.tts.iov.domain.IovConfig;
import com.tts.iov.service.IovConfigService;
import org.springframework.stereotype.Service;

@Service
public class IovConfigServiceImpl extends ServiceImpl<IovConfigMapper, IovConfig> implements IovConfigService {

}
