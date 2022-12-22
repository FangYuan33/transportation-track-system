package com.tts.framework.common.constant;


/**
 * 任务事件的类型 枚举值
 *
 * @author FangYuan
 * @since 2022-12-22 15:58:34
 */
public enum IovTaskEventTypeEnum {

    ABNORMAL(0),
    NORMAL(1);

    private final Integer value;

    IovTaskEventTypeEnum(Integer value) {
        this.value = value;
    }

    public Integer getValue() {
        return value;
    }
}
