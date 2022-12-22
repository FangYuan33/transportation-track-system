package com.tts.framework.common.constant;

public enum StatusEnum {

    /**
     * 停用
     */
    DISABLE(0),

    /**
     * 可用
     */
    ENABLE(1);

    private final int value;

    /**
     * 构造方法
     *
     * @param value 响应枚举值
     */
    StatusEnum(int value) {
        this.value = value;
    }


    /**
     * 返回value值
     */
    public int getValue() {
        return this.value;
    }

}
