package com.ccspace.facade.domain.common.msg;

/**
 * @AUTHOR CF
 * @DATE Created on 2018/11/6 10:31.
 */
public enum CustomType {
    PULL_TYPE("pullActionTypes","人员需要拉取的消息类型配置名称");

    private String value;
    private String label;

    CustomType(String value, String label) {
        this.value = value;
        this.label = label;
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
}
