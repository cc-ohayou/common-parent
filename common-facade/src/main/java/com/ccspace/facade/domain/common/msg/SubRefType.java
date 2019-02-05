package com.ccspace.facade.domain.common.msg;

/** 订阅对象的类型
 * @AUTHOR CF
 * @DATE Created on 2018/5/24 13:52.
 */
public enum SubRefType {
    POST("post", "帖子"),
    PERSON("person", "订阅人员"),
    SYS("sys", "订阅系统");


    private String value;
    private String label;

    SubRefType(String value, String label) {
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
