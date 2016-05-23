/*
 * (C) Copyright 2015-2016 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Contributors:
 *   ohun@live.cn (夜色)
 */

package com.mpush.client.gateway;

import com.mpush.api.connection.Connection;
import com.mpush.netty.client.NettyClient;
import io.netty.channel.ChannelHandler;

/**
 * Created by yxx on 2016/5/17.
 *
 * @author ohun@live.cn
 */
public class GatewayClient extends NettyClient {
    private final GatewayClientChannelHandler handler = new GatewayClientChannelHandler();

    public GatewayClient(String host, int port) {
        super(host, port);
    }

    @Override
    public ChannelHandler getChannelHandler() {
        return handler;
    }

    public Connection getConnection() {
        return handler.getConnection();
    }
}
