package com.ccspace.facade.domain.common.getui;

import lombok.Data;

/**
 * @AUTHOR Eric
 * @DATE Created on 2017/12/15 15:36.
 */
@Data
public class PushReturnData {
    private String msgType;
    private String title;
    private String msg;
    private String content;
    private String strategyId;
    private String relationId;



    public static void main(String[] args) {
        PushReturnData data=new PushReturnData();
        data.setMsgType("10");
        data.setTitle("策略动态");
        data.setContent("test");
        data.setStrategyId("201807251626190000000001553014");
        data.setMsg("");

    }
}
