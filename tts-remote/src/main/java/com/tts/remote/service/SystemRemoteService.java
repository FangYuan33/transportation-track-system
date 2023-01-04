package com.tts.remote.service;

import com.tts.remote.dto.IovConfigDto;

public interface SystemRemoteService {

    /**
     * 新增or修改Iov Config
     */
    boolean saveOrUpdateIovConfig(IovConfigDto iovConfig);
}
