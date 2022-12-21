package com.tts.base.service;

import com.tts.base.domain.BaseServerLog;
import com.tts.base.dto.BaseServerDto;

import java.util.List;

/**
 * base  的 服务器 管理 service
 *
 * @author FangYuan
 * @since 2022-12-21 20:06:17
 */
public interface BaseServerService {

    /**
     * 记录服务器变化日志
     */
    void saveServerLog(String serverIp, String status);

    /**
     * 返回当前注册到zk上的所有服务器列表
     */
    List<BaseServerDto> queryServerList();

    /**
     * 查询服务器日志列表
     */
    List<BaseServerLog> queryServerLogList(BaseServerLog baseServerLog);
}
