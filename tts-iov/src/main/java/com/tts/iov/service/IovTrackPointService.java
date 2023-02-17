package com.tts.iov.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.tts.gps.dto.TrackPointQueryDto;
import com.tts.iov.domain.IovTrackPoint;

import java.util.List;

public interface IovTrackPointService extends IService<IovTrackPoint> {

    /**
     * 根据条件查询点位信息
     * @param trackPointQueryDto
     * @return
     */
    List<IovTrackPoint> queryIovTrackPointByCondition(TrackPointQueryDto trackPointQueryDto);
}
