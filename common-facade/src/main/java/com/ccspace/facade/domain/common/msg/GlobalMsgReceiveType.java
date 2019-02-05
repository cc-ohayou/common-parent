package com.ccspace.facade.domain.common.msg;

/**
 * @AUTHOR CF
 * @DATE Created on 2018/5/24 13:52.
 */
public enum GlobalMsgReceiveType {
    ALL("all", "接收来自所有源头的消息（系统的 自己关注的 其他关联的）"),
    ONLY_SUB("onlySub", "只接收自己关注人员的");

    private String value;
    private String label;

    GlobalMsgReceiveType(String value, String label) {
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
