package com.tts.main.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tts.main.domain.TtsIovSubscribeTaskLog;

import java.util.List;

/**
 * iov 订阅任务 日志的数据层
 *
 * @author FangYuan
 * @since 2022-12-22 17:32:06
 */
public interface TtsIovSubscribeTaskLogMapper extends BaseMapper<TtsIovSubscribeTaskLog> {

    List<TtsIovSubscribeTaskLog> selectIovSubscribeTaskLogLatestManual();
}
