package com.tts.main.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tts.main.domain.TtsCoordinateLatest;
import com.tts.main.dto.GeofenceVehicleQueryDto;

import java.util.List;

/**
 * 运输轨迹坐标存储 数据层
 *
 * @author FangYuan
 * @since 2020-12-10
 */
public interface TtsCoordinateLatestMapper extends BaseMapper<TtsCoordinateLatest> {

    /**
     * 根据租户id 和 车辆id 查询坐标点
     *
     * @param ttsCoordinateLatest
     * @return
     */
    TtsCoordinateLatest queryLatestPointByTenantIdAndVehicleId(TtsCoordinateLatest ttsCoordinateLatest);

    /**
     * 根据电子围栏信息筛选车辆最新坐标点
     *
     * @param geofenceVehicleQueryDto
     * @return
     */
    List<TtsCoordinateLatest> selectCoordinateLatestPointByGeofence(GeofenceVehicleQueryDto geofenceVehicleQueryDto);

    /**
     * 查询列表
     *
     * @param ttsCoordinateLatest
     * @return
     */
    List<TtsCoordinateLatest> selectTtsCoordinateLatestList(TtsCoordinateLatest ttsCoordinateLatest);

    /**
     * 根据租户id 和 车辆id 更新记录
     *
     * @param ttsCoordinateLatest
     */
    void replaceByTenantIdAndVehicleId(TtsCoordinateLatest ttsCoordinateLatest);

    /**
     * 根据 租户id 和车辆id 删除
     *
     * @param ttsCoordinateLatest
     */
    void deleteByTenantIdAndVehicleId(TtsCoordinateLatest ttsCoordinateLatest);

    /**
     * 保存 最新坐标
     *
     * @param ttsCoordinateLatest
     */
    void saveTtsCoordinateLatest(TtsCoordinateLatest ttsCoordinateLatest);

    //通过车辆和租户修改
    void updateLatestByVehicleId(TtsCoordinateLatest ttsCoordinateLatest);
}
