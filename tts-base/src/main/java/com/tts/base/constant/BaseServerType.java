package com.tts.base.constant;

/**
 * 服务器 类型
 */
public enum BaseServerType {

		MASTER("MASTER"),
		SLAVER("SLAVER");

		private final String name;

		BaseServerType(String name){
				this.name = name;
		}

		public String getName() {
				return name;
		}
}
