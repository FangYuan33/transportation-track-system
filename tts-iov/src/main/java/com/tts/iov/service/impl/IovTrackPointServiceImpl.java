package com.tts.iov.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tts.iov.dao.IovTrackPointMapper;
import com.tts.iov.domain.IovTrackPoint;
import com.tts.iov.service.IovTrackPointService;
import com.tts.remote.dto.IovTrackPointQueryDto;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class IovTrackPointServiceImpl extends ServiceImpl<IovTrackPointMapper, IovTrackPoint>
        implements IovTrackPointService {
    @Resource
    private IovTrackPointMapper iovTrackPointMapper;

    @Override
    public List<IovTrackPoint> queryIovTrackPointByCondition(IovTrackPointQueryDto iovTrackPointQueryDto) {
        return iovTrackPointMapper.queryIovTrackPointByCondition(iovTrackPointQueryDto);
    }
}
