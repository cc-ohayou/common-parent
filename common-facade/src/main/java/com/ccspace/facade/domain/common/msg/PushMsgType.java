package com.ccspace.facade.domain.common.msg;


/**
 * @AUTHOR CF
 * @DATE Created on 2018/4/4 10:06.
 */
public enum PushMsgType {
    SUB("1", "sub", "隶属新的关注订阅(专指人员订阅)"),
    THUMB("2", "thumb", "点赞"),
    COLLECT("3", "collect", "收藏"),
    SIMU_STRATEGY_TRADE("4", "simu_strategy_trade", "模拟策略交易"),
    BALANCE("5", "balance_modify", "余额变动"),
    PRICE_DROP("6", "price_drop", "股票跌幅推送"),
    COMMENT("7", "comment", "评论"),
    SYS_NOTICE("8","sys_notice","系统消息"),
    SUB_DYNAMIC("9","sub_dynamic","好友动态"),
    STRGY_DYNAMIC("10","strgy_dynamic","我的策略动态"),
    INSUFFICIENT_DEPOSIT("11", "insufficient_deposit", "策略信用金不足推送"),
    RAISING_LIMIT("13","RAISING_LIMIT","涨停修改止盈"),
    OPTIONS_STATUS("14","OPTION_STATUS","期权动态"),
    ;
    private String value;
    private String dbName;
    private String label;
    private String parentType;



    PushMsgType(String value, String dbName, String label) {
        this.value = value;
        this.label = label;
        this.dbName = dbName;
    }
    PushMsgType(String value, String dbName, String label, String parentType) {
        this.value = value;
        this.label = label;
        this.dbName = dbName;
        this.parentType = parentType;
    }
    public String getDbName() {
        return dbName;
    }

    public void setDbName(String dbName) {
        this.dbName = dbName;
    }

    public String getValue() {
        return this.value;
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

    public String getParentType() {
        return parentType;
    }

    public void setParentType(String parentType) {
        this.parentType = parentType;
    }

    public static String switchToDbName(String type) {
        String dbName = "";
        for (PushMsgType sign : PushMsgType.values()) {
            if (sign.value.equals(type)) {
                dbName = sign.dbName;
                break;
            }
        }
        return dbName;
    }
}
