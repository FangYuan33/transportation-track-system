/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package com.tts.common.utils.http;

import com.tts.common.core.domain.Request;
import com.tts.common.core.domain.Response;
import lombok.extern.slf4j.Slf4j;

/**
 * Client
 */
@Slf4j
public class Client {
    /**
     * 发送请求
     *
     * @param request request对象
     * @return Response
     */
    public static Response execute(Request request) throws Exception {
        log.info(HttpPoolUtil.getTotalStats());
        switch (request.getMethod()) {
            case GET:
                return HttpPoolUtil.httpGet(request.getHost(), request.getPath(),
                		request.getTimeout(), 
                		request.getHeaders(), 
                		request.getQueries(),
                		request.getAccessId(), request.getSecretKey());

			case POST_JSON:
				return HttpPoolUtil.httpPost(request.getHost(), request.getPath(),
						request.getTimeout(),
						request.getHeaders(),
						request.getQueries(),
						request.getJsonStrBody(),
						request.getAccessId(), request.getSecretKey());

            default:
                throw new IllegalArgumentException(String.format("unsupported method:%s", request.getMethod()));
        }
    }
}
