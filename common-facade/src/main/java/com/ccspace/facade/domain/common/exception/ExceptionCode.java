package com.ccspace.facade.domain.common.exception;

/**
 * @AUTHOR CF
 * @DATE Created on 2017/9/20 13:51.
 */
public class ExceptionCode {
    public static final String PARAM_ERROR = "1001";//参数异常
    public static final String BIZ_ERROR = "1002";//业务异常
    //重要业务异常 此类异常影响正常流程 用户数据 余额数据等等的异常必须处理
    public static final String BIZ_IMPORTANT_ERROR = "1003";
}
