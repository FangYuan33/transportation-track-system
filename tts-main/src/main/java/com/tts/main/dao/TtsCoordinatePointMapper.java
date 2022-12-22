package com.tts.main.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tts.main.domain.TtsCoordinatePoint;
import com.tts.main.dto.TrackQueryDto;

import java.util.List;

/**
 * 运输轨迹坐标存储 数据层
 *
 * @author FangYuan
 * @since 2022-12-22 20:05:27
 */
public interface TtsCoordinatePointMapper extends BaseMapper<TtsCoordinatePoint> {

    /**
     * 根据车辆id查询最新的坐标
     *
     * @param trackQueryDto 查询条件
     * @return 这辆车最新的坐标
     */
    TtsCoordinatePoint queryLatestPointByVehicleId(TrackQueryDto trackQueryDto);

    /**
     * 根据车辆id 和 起始与截止时间查询其轨迹
     *
     * @param trackQueryDto 查询条件
     */
    List<TtsCoordinatePoint> queryTrackByVehicleId(TrackQueryDto trackQueryDto);

    /**
     * 首页加载查询数据 并且车辆ID不能为空
     */
    public List<TtsCoordinatePoint> selectQueryCoordinateList(TtsCoordinatePoint ttsCoordinatePoint);
}
