package com.ccspace.facade.domain.common.getui;

import com.alibaba.fastjson.JSON;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PushReq {
    private int code;
    public Map<String, String> msgMap;//消息内容装载体
    public List<String> targetUserIds = new ArrayList<>();//发送目标用户的用户ID

    public List<String> getTargetUserIds() {
        return targetUserIds;
    }


    public void setTargetUserIds(List<String> targetUserIds) {
        this.targetUserIds = targetUserIds;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public Map<String, String> getMsgMap() {
        return msgMap;
    }

    public void setMsgMap(Map<String, String> msgMap) {
        this.msgMap = msgMap;
    }

    public static void main(String[] args) {
        Map<String, String> msgMap = new HashMap<>();
        msgMap.put("merchantId","201709211815270000011005");
        msgMap.put("content","test");
        msgMap.put("type","10");
        msgMap.put("title", "手动推送");
        PushReq req = new PushReq();
        req.getTargetUserIds().add("201712181017220000000005151060");
        req.setMsgMap(msgMap);
        System.out.println(JSON.toJSONString(msgMap));
        System.out.println(JSON.toJSONString(req.getTargetUserIds()));
        System.out.println(JSON.toJSONString(req));
    }
}
