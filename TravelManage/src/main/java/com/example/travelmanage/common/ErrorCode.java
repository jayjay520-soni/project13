package com.example.travelmanage.common;

public enum ErrorCode {
    SUCCESS(1, "操作成功"),
    PARAM_ERROR(0, "参数错误"),
    USER_NOT_FOUND(0, "用户不存在"),
    PASSWORD_ERROR(0, "密码错误"),
    ROLE_ERROR(0, "身份选择错误"),
    USER_DISABLED(0, "账号已禁用"),
    USER_EXIST(0, "用户名已存在"),
    SYSTEM_ERROR(0, "系统异常");

    private final int code;
    private final String message;

    ErrorCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
