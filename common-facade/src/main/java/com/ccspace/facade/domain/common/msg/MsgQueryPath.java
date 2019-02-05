package com.ccspace.facade.domain.common.msg;

/**
 * @AUTHOR CF
 * @DATE Created on 2018/5/28 12:47.
 */
public enum MsgQueryPath {
    NEW_SUB("newSub", "新的关注"),
    THUMB("like","赞我的"),
    COMMENT("comment","评论我的"),
    SUB_DYNAMIC("subDynamic","好友动态"),
    SYS_MSG("sysNotice","系统消息"),
    STRATEGY_DYNAMIC("strategyDynamic","策略动态");


    private String value;
    private String label;

    MsgQueryPath(String value, String label) {
        this.value = value;
        this.label = label;
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
}
