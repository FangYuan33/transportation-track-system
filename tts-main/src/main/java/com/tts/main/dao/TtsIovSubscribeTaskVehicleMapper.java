package com.tts.main.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tts.main.domain.TtsIovSubscribeTaskVehicle;

import java.util.List;

/**
 * iov 订阅任务 车辆的数据层
 *
 * @author FangYuan
 * @since 2022-12-22 17:32:06
 */
public interface TtsIovSubscribeTaskVehicleMapper extends BaseMapper<TtsIovSubscribeTaskVehicle> {

    List<TtsIovSubscribeTaskVehicle> selectByQueryCondition(TtsIovSubscribeTaskVehicle queryCondition);
}
