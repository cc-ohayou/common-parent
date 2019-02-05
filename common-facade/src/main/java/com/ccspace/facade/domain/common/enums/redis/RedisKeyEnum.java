package com.ccspace.facade.domain.common.enums.redis;


import com.ccspace.facade.domain.common.constants.CommonConstants;

/**
 * @AUTHOR CF
 * @DATE Created on 2018/12/17 15:14.
 */
public enum RedisKeyEnum {
    HASH_TEST("contract-market-info",
            " ",RedisType.HASH,BizType.USER),

    ;


    private String value;
    private String label;
    private RedisType redisKeyType;
    private BizType type;

    RedisKeyEnum(String value, String label, RedisType redisKeyType, BizType type) {
        this.value = CommonConstants.PROJECT_PREFIX +redisKeyType.getValue()+"-"+ value;
        this.label = label;
        this.redisKeyType = redisKeyType;
        this.type = type;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public RedisType getRedisKeyType() {
        return redisKeyType;
    }

    public void setRedisKeyType(RedisType redisKeyType) {
        this.redisKeyType = redisKeyType;
    }

    public BizType getType() {
        return type;
    }

    public void setType(BizType type) {
        this.type = type;
    }

}
