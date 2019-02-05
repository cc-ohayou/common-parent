package com.ccspace.facade.domain.common.enums;

public enum ErrorEnum {
    // 系统
    SYSTEM_ERROR(999999, "系统异常"),
    VISIT_TOO_MUCH(3014, "您的访问太过频繁,喝杯茶休息会吧"),

    PARAM_ERROR(900006, "参数错误"),
    /**
     * 用户相关
     */
    AUTH_FAILED(3001, "Token Auth Failed"),

    ;

    private int code;
    private String message;
    private Object[] params;

    public Object[] getParams() {
        return params;
    }

    public void setParams(Object[] params) {
        this.params = params;
    }

    ErrorEnum(int code, String message) {
        this.code = code;
        this.message = message;
    }

    ErrorEnum(int code, String message, Object[] params) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return message;
    }

    public void setMsg(String message) {
        this.message = message;
    }

}