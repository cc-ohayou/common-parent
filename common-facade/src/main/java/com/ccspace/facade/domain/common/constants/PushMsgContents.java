package com.ccspace.facade.domain.common.constants;

/**
 * @AUTHOR CF
 * @DATE Created on 2018/7/6 16:48.
 */
public class PushMsgContents {


    public static String SYS_DEFER_FAILED="您于%s创建的（%s张）%s期权策略，" +
            "因余额不足延期失败，已被平仓处理。" ;

    public static void main(String[] args) {
        System.out.printf(String.format(SYS_DEFER_FAILED,"2018-11-15 00:00:00","20","1092133.SH"));
    }
}
