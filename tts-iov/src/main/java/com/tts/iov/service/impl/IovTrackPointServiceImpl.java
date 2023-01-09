package com.tts.iov.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tts.iov.dao.IovTrackPointMapper;
import com.tts.iov.domain.IovTrackPoint;
import com.tts.iov.service.IovTrackPointService;
import org.springframework.stereotype.Service;

@Service
public class IovTrackPointServiceImpl extends ServiceImpl<IovTrackPointMapper, IovTrackPoint>
        implements IovTrackPointService {

}
