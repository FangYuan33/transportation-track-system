package com.tts.iov.enums;

public enum IovSubscribeTaskStateEnums {
    /**
     * 待分配
     */
    ALLOCATING(10, "待分配"),
    /**
     * 已分配
     */
    ALLOCATED(20, "已分配"),
    /**
     * 任务运行中
     */
    RUNNING(30, "任务运行中"),
    /**
     * 异常停止
     */
    ERROR(40, "异常停止"),
    /**
     * 正常停止
     */
    STOPPED(50, "正常停止");

    private final Integer value;

    private final String name;

    IovSubscribeTaskStateEnums(Integer value, String name) {
        this.value = value;
        this.name = name;
    }

    public Integer getValue() {
        return value;
    }

    public String getName() {
        return name;
    }
}
