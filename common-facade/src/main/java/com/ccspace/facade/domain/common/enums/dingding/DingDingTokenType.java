package com.ccspace.facade.domain.common.enums.dingding;

/**
 * @AUTHOR song-jj
 */
public enum DingDingTokenType {
    SETTLE("settle","结算"),
    DISTRIBUTE("distribute","分发"),
    MONITOR("monitor","监控"),
    PUBSUB("pubsub","发布订阅")
    ;


    DingDingTokenType(String value, String label) {
        this.value = value;
        this.label = label;
    }

    private String value;
    private String label;

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
