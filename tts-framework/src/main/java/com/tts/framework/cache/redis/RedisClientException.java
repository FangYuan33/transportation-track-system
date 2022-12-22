package com.tts.framework.cache.redis;

import com.tts.framework.common.exception.BusinessException;

/**
 * redis 客户端异常
 *
 * @author FangYuan
 * @since 2021-01-15
 */
public class RedisClientException extends BusinessException {

    public RedisClientException(String message) {
        super(message);
    }
}
