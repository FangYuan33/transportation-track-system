package com.tts.base.constant;

/**
 * 服务器状态
 */
public enum BaseServerStatus {

    REGISTER("REGISTER"),
    UNREGISTER("UNREGISTER"),
    MASTER("MASTER"),
    UNMASTER("UNMASTER");

    private final String name;

    BaseServerStatus(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
