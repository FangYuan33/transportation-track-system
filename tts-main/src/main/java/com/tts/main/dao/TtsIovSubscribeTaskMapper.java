package com.tts.main.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tts.main.domain.TtsIovSubscribeTask;

import java.util.List;


/**
 * iov 订阅任务 的数据层
 *
 * @author FangYuan
 * @since 2022-12-22 16:34:19
 */
public interface TtsIovSubscribeTaskMapper extends BaseMapper<TtsIovSubscribeTask> {


    List<TtsIovSubscribeTask> selectByQueryCondition(TtsIovSubscribeTask ttsIovSubscribeTask);

    /**
     * 查询运行状态的服务器ip列表
     *
     * @return
     */
    List<String> selectServerIpInRunningState();

}
