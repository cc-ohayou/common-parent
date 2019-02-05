package com.ccspace.facade.domain.common.util.sms;

/**
 * @AUTHOR chenhui
 * @DATE Created on 2018/5/18 0018 下午 5:26.
 */
public class Sms {
    private String account;//用户账号
    private String pswd;//账号密码
    private String mobile;//发送手机号码
    private String msg;//发送内容
    private String resptype;//发送结果响应格式，填json返回json格式
    private String needStatus;
    private String userId;
    private String action;
    private String extno;
    private String sendTime;


    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getPswd() {
        return pswd;
    }

    public void setPswd(String pswd) {
        this.pswd = pswd;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getResptype() {
        return resptype;
    }

    public void setResptype(String resptype) {
        this.resptype = resptype;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getExtno() {
        return extno;
    }

    public void setExtno(String extno) {
        this.extno = extno;
    }

    public String getSendTime() {
        return sendTime;
    }

    public void setSendTime(String sendTime) {
        this.sendTime = sendTime;
    }

    public String getNeedStatus() {
        return needStatus;
    }

    public void setNeedStatus(String needStatus) {
        this.needStatus = needStatus;
    }
}
