package com.cw.ecommerce.common.enumerate;

/**
 * 状态枚举定义
 * 0: 有效 (ACTIVE)
 * 1: 失效 (INACTIVE)
 */

public enum Status {

    ACTIVE(0, "active"),
    INACTIVE(1, "inactive");

    private final int code;
    private final String description;

    Status(int code, String description) {
        this.code = code;
        this.description = description;
    }


    public int getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }


    public static Status ofCode(int code) {
        for (Status status : Status.values()) {
            if (status.code == code) {
                return status;
            }
        }
        throw new IllegalArgumentException("无效的状态码: " + code);
    }


    public static String getDescriptionByCode(int code) {
        return ofCode(code).getDescription();
    }
}