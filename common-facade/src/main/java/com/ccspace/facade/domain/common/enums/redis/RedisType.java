package com.ccspace.facade.domain.common.enums.redis;

import java.util.HashSet;
import java.util.Set;

/**
 * @AUTHOR CF
 * @DATE Created on 2018/5/10 14:49.
 */
public enum RedisType {

    STRING("str", "字符串类型"),
    HASH("hash", "hash类型"),
    LIST("list", "列表类型"),
    SET("set", "集合类型"),
    ZSET("zset", "有序集合");

    private String value;
    private String label;

    RedisType(String value, String label) {
        this.value = value;
        this.label = label;
    }

    public static String switchTypeToName(String value) {
        String labelStr = "";
        for (RedisType status : RedisType.values()) {
            if (status.value.equals(value)) {
                labelStr = status.label;
                break;
            }
        }
        return labelStr;
    }

    public static Set<String> getTypes() {
      Set<String> types=new HashSet<>(8);
        for (RedisType type : RedisType.values()) {
                types.add(type.getValue());
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
