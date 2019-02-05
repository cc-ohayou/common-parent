package com.ccspace.facade.domain.common.exception;


import com.ccspace.facade.domain.common.enums.ErrorEnum;

/**
 * 业务异常类
 *
 * @author CF create on 2017/10/18 9:51
 * @description
 */
public class BusinessException extends RuntimeException {

    private String msg;
    private Object data;
    private String errorCode;
    private String errorMessage;

    public String getErrorCode() {
        return errorCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }




    public BusinessException(String errorCode, String errorMessage) {
        super(errorMessage);
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }
    public BusinessException(String errorMessage) {
        super(errorMessage);
        this.errorCode = ExceptionCode.BIZ_ERROR;
        this.errorMessage = errorMessage;
    }

    public BusinessException(ErrorEnum errorEnum) {
        super(errorEnum.getMsg());
        this.errorCode = String.valueOf(errorEnum.getCode());
        this.errorMessage = errorEnum.getMsg();
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}