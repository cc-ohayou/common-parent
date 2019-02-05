package com.ccspace.facade.domain.common.enums.redis;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @AUTHOR CF
 * @DATE Created on 2018/5/10 15:37.
 */
public enum RedisKeyBizType {
    LOGIN("login", "登录相关"),
    IP_LIMIT("ipLimit", "ip限制相关"),
    UA_LIMIT("uaLimit", "ua设备限制相关"),
    URL_LIMIT("urlLimit", "url限制相关"),
    SYS_CONFIG("sysConfig", "各种全局系统配置相关"),
    SWITCH("switch", "开关相关key");

    private String value;
    private String label;

    RedisKeyBizType(String value, String label) {
        this.value = value;
        this.label = label;
    }

    public static String switchTypeToName(String value) {
        String labelStr = "";
        for (RedisKeyBizType status : RedisKeyBizType.values()) {
            if (status.value.equals(value)) {
                labelStr = status.label;
                break;
            }
        }
        return labelStr;
    }

    public static Set<String> getTypes() {
        Set<String> types=new HashSet<>(8);
        for (RedisKeyBizType type : RedisKeyBizType.values()) {
            types.add(type.getValue());
        }
        return types;
    }
    public static Map<String,String> getBizTypesMap() {
        Map<String,String> types=new HashMap<>(8);
        for (RedisKeyBizType type : RedisKeyBizType.values()) {
            types.put(type.getValue(),type.getLabel());
        }
        return types;
    }

    public String getLabel() {
        return label;
    }


    public String getValue() {
        return value;
    }
}
