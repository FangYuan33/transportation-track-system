package com.tts.facade.enums;

public enum IovTypeEnums {

    G7("G7");

    private final String value;

    IovTypeEnums(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static IovTypeEnums parse(String value) {
        for (IovTypeEnums iovType : values()) {
            if (iovType.value.equals(value)) {
                return iovType;
            }
        }

        return null;
    }
}
