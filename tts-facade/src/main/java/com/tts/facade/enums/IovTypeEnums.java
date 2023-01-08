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
}
