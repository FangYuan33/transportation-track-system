package com.tts.base.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tts.base.domain.BaseServerLog;

import java.util.List;

public interface BaseServerLogMapper extends BaseMapper<BaseServerLog> {

    /**
     * 查询服务器日志列表
     */
    List<BaseServerLog> selectBaseServerLogList(BaseServerLog baseServerLog);
}
