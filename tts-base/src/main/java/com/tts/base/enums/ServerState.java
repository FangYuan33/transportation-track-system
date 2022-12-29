package com.tts.base.enums;

public enum ServerState {
    MASTER("MASTER"),
    UN_MASTER("UN_MASTER");

    private final String name;

    ServerState(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
