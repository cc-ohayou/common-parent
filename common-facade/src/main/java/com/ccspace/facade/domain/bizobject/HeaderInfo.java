package com.ccspace.facade.domain.bizobject;

import lombok.Data;

/**
 * @Author LJH
 * @DATE Created on 2018/12/15 20:48
 */
@Data
public class HeaderInfo {
    private String clienttype;
    private String userid;
    private String token;
    private String address;
    private String longitude;
    private String latitude;
    private String softversion;
    private String systemversion;
    private String deviceid;
    private String time;
    private String signversion;
    private String sign;
    private String phoneversion;
    private String channel;
    private String merchantid;
    private String sid;
    private String source;
    private String ip;
    private String appidentifier;
    private String pushclientid;
    private String roleid;
    //角色切换时用到，实际传入partnerid 是该合伙人对应的 bg_user_id
    private String partnerid;
}
