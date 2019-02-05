package com.ccspace.facade.domain.dataobject;

import lombok.Data;

import javax.persistence.Table;

/**
 * @AUTHOR CF
 * @DATE Created on 2018/4/25 19:27.
 */
@Data
@Table(name = "user")
public class User {

    /**
     * 城市编号
     */
    private String uid;

    /**
     * 用户真实名
     */
    private String userName;

    /**
     * 描述
     */
    private String description;
    private String nickName;
    private String mail;
    private String phone;
    private String pwd;
    private String headImage;
    private String salty;
    private String createTime;
    private String updateTime;

}

