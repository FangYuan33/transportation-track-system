package com.tts.iov.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.tts.iov.domain.IovTrackPoint;
import com.tts.remote.dto.IovTrackPointQueryDto;

import java.util.List;

public interface IovTrackPointService extends IService<IovTrackPoint> {

    /**
     * 根据条件查询点位信息
     * @param iovTrackPointQueryDto
     * @return
     */
    List<IovTrackPoint> queryIovTrackPointByCondition(IovTrackPointQueryDto iovTrackPointQueryDto);
}
