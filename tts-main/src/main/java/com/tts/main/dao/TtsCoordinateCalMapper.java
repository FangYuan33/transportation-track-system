package com.tts.main.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tts.main.domain.TtsCoordinateCal;

import java.util.List;

/**
 * 运输轨迹坐标存储 数据层
 *
 * @author FangYuan
 * @since 2020-12-10
 */
public interface TtsCoordinateCalMapper extends BaseMapper<TtsCoordinateCal> {
    /**
     * 查询坐标运算结果列表
     *
     * @param ttsCoordinateCal 坐标运算结果
     * @return 坐标运算结果集合
     */
    List<TtsCoordinateCal> selectTtsCoordinateCalList(TtsCoordinateCal ttsCoordinateCal);

}
