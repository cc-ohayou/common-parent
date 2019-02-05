package com.ccspace.facade.domain.common.enums.redis;

/**
 * @AUTHOR CF
 * @DATE Created on 2018/12/17 15:26.
 */
public enum BizType {


    USER("user", "用户相关"),
    BONUS("bonus", "红包"),
    LOGIN("login", "登录相关"),
    VERIFY_CODE("verifyCode", "验证码相关"),
    OPERATION_AUTH("operation-auth","操作权限相关")
    ;


    private String value;
    private String label;


    BizType(String value, String label) {
        this.value = value;
        this.label = label;
    }

}
