package com.tts.base.zk;

import com.tts.base.service.BaseServerService;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.state.ConnectionState;
import org.apache.curator.framework.state.ConnectionStateListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * zk 连接状态监听器
 *
 * @author FangYuan
 * @since 2022-12-21 21:03:04
 */
public class ZkConnectionStateListener  implements ConnectionStateListener {

		private static final Logger log = LoggerFactory.getLogger(ZkConnectionStateListener.class);

		private String zkRegPathPrefix;
		private String regContent;
		private BaseServerService baseServerService;
		private BaseWorkServer baseWorkServer;

		public ZkConnectionStateListener(String zkRegPathPrefix, String regContent, BaseWorkServer baseWorkServer, BaseServerService baseServerService) {
				this.zkRegPathPrefix = zkRegPathPrefix;
				this.regContent = regContent;
				this.baseServerService = baseServerService;
				this.baseWorkServer = baseWorkServer;
		}

		@Override
		public void stateChanged(CuratorFramework curatorFramework, ConnectionState connectionState) {

				log.error("##################   zk state change ["+connectionState.toString()+"]");
				//记录节点变化,这里只记录了当前节点自己的变化
				if(baseServerService != null) {
						baseServerService.saveServerLog(regContent, connectionState.toString());
				}

				if (connectionState == ConnectionState.SUSPENDED) {
						log.error("[负载均衡失败]zk session超时");
						baseWorkServer.stopInner();
						baseWorkServer.initCurator();
						baseWorkServer.startInner();
				}


		}
}
