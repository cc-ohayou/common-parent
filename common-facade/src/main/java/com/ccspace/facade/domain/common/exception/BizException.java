package com.ccspace.facade.domain.common.exception;

/**
 * @AUTHOR CF
 * @DATE Created on 2017/10/1 16:01.
 */
public class BizException extends Exception {


    private String errorCode;
    private String errorMessage;

    public String getErrorCode() {
        return errorCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public BizException(String errorCode, String errorMessage) {
        super(errorMessage);
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

    public BizException( String errorMessage) {
        super(errorMessage);
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }


}
