package com.tts.framework.common.constant;


/**
 * 任务事件操作的类型 枚举值
 *
 * @author FangYuan
 * @since 2022-12-22 15:57:34
 */
public enum IovTaskOperTypeEnum {

    MANUAL(0),
    AUTO(1);

    private final Integer value;

    IovTaskOperTypeEnum(Integer value) {
        this.value = value;
    }

    public Integer getValue() {
        return value;
    }
}
