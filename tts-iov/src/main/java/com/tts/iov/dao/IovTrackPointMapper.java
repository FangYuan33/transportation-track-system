package com.tts.iov.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tts.iov.domain.IovTrackPoint;
import com.tts.remote.dto.IovTrackPointQueryDto;

import java.util.List;

public interface IovTrackPointMapper extends BaseMapper<IovTrackPoint> {

    /**
     * 查询点位
     * @param iovTrackPointQueryDto
     * @return
     */
    List<IovTrackPoint> queryIovTrackPointByCondition(IovTrackPointQueryDto iovTrackPointQueryDto);
}
