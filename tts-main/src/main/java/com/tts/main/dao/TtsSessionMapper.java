package com.tts.main.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tts.main.domain.TtsSession;
import com.tts.remote.dto.TtsSessionDto;

import java.util.List;

/**
 * 运输轨迹坐标存储 数据层
 *
 * @author FangYuan
 * @since 2022-12-22 20:01:18
 */
public interface TtsSessionMapper extends BaseMapper<TtsSession> {


    /**
     * 首页加载查询数据 并且车辆ID不能为空
     */
    List<TtsSession> selectTtsSessionList(TtsSession ttsSession);


    /**
     * 根据车辆ID 起止时间 查询会话信息
     */
    List<TtsSessionDto> querySessionByVehicleIdAndTime(TtsSessionDto ttsSessionDto);

}
