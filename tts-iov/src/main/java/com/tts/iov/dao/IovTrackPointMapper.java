package com.tts.iov.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tts.gps.dto.TrackPointQueryDto;
import com.tts.iov.domain.IovTrackPoint;

import java.util.List;

public interface IovTrackPointMapper extends BaseMapper<IovTrackPoint> {

    /**
     * 查询点位
     */
    List<IovTrackPoint> queryIovTrackPointByCondition(TrackPointQueryDto trackPointQueryDto);
}
