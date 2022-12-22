package com.tts.main.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tts.main.domain.TtsIovConfig;

import java.util.List;

public interface TtsIovConfigMapper extends BaseMapper<TtsIovConfig> {

    List<TtsIovConfig> selectByQueryCondition(TtsIovConfig queryCondition);
}