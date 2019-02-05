package com.ccspace.facade.domain.common.msg;

/**
 * @AUTHOR CF
 * @DATE Created on 2018/5/24 13:52.
 */
public enum MsgType {
    ANNOUNCE("announce", "公告"),
    REMIND("remind", "提醒"),
    MESSAGE("message", "消息(私信)");


    private String value;
    private String label;

    MsgType(String value, String label) {
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
