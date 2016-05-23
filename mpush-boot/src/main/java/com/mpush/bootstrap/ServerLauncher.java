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

package com.mpush.bootstrap;


import com.mpush.api.Server;
import com.mpush.bootstrap.job.*;
import com.mpush.core.server.AdminServer;
import com.mpush.core.server.ConnectionServer;
import com.mpush.core.server.GatewayServer;
import com.mpush.tools.config.CC;
import com.mpush.zk.node.ZKServerNode;

/**
 * Created by yxx on 2016/5/14.
 *
 * @author ohun@live.cn
 */
public class ServerLauncher {
    private final ZKServerNode csNode = ZKServerNode.csNode();

    private final ZKServerNode gsNode = ZKServerNode.gsNode();

    private final Server connectServer = new ConnectionServer(csNode.getPort());

    private final Server gatewayServer = new GatewayServer(gsNode.getPort());

    private final Server adminServer = new AdminServer(CC.mp.net.admin_server_port);


    public void start() {
        BootChain chain = BootChain.chain();
        chain.boot()
                .setNext(new ZKBoot())//1.启动ZK节点数据变化监听
                .setNext(new RedisBoot())//2.注册redis sever 到ZK
                .setNext(new ServerBoot(connectServer, csNode))//3.启动长连接服务
                .setNext(new ServerBoot(gatewayServer, gsNode))//4.启动网关服务
                .setNext(new ServerBoot(adminServer, null))//5.启动控制台服务
                .setNext(new HttpProxyBoot())//6.启动http代理服务，解析dns
                .setNext(new MonitorBoot())//7.启动监控
                .setNext(new LastBoot());//8.启动结束
        chain.run();
    }

    public void stop() {
        stopServer(gatewayServer);
        stopServer(gatewayServer);
        stopServer(adminServer);
    }

    private void stopServer(Server server) {
        if (server != null) {
            server.stop(null);
        }
    }
}
