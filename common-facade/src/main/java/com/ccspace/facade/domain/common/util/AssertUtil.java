package com.ccspace.facade.domain.common.util;



import com.ccspace.facade.domain.common.enums.ErrorEnum;
import com.ccspace.facade.domain.common.exception.BusinessException;
import com.ccspace.facade.domain.common.exception.ExceptionCode;
import com.ccspace.facade.domain.common.exception.ParamException;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @AUTHOR CF
 * @DATE Created on 2017/9/20 13:34.
 */
public class AssertUtil {
    /**
     * @description 一般用于判断一些查询返回map必须不为空的情况 譬如人员信息
     * @author CF create on 2017/6/26 16:02
     */
    public static void isNullList(List list, String code, String msg) throws ParamException {

        if (list == null || list.size() == 0) {

            throw new ParamException(code, msg);
        }

    }

    /**
     * @description 此方法多用于判断传入参数非空  属于ParameterException
     * @author CF create on 2017/6/19 15:15
     */
    public static void isNullStr(String str, String code, String msg) throws ParamException {

        if (str == null || "".equals(str.trim()) || "null".equals(str.trim())) {
            throw new ParamException(code, msg);
        }
    }
    public static void isNullStr(String str, String msg) throws ParamException {

        if (str == null || "".equals(str.trim()) || "null".equals(str.trim())) {
            throw new ParamException(ExceptionCode.PARAM_ERROR, msg);
        }
    }

    public static void isNullObj(Object obj, String code, String msg) throws ParamException {
        if (obj == null) {
            throw new ParamException(code, msg);
        }
    }
    public static void isNullObj(Object obj, String msg) throws ParamException {
        if (obj == null) {
            throw new ParamException(ExceptionCode.PARAM_ERROR, msg);
        }
    }

    public static void isNullObj(Object obj,ErrorEnum errorEnum) throws ParamException {
        if (obj == null) {
            throw new ParamException(String.valueOf(errorEnum.getCode()), errorEnum.getMsg());
        }
    }
    /**
     * @description 判断业务引起的异常
     * @author CF create on 2017/6/26 16:04
     */
    public static void isTrueBIZ(Boolean obj, String msg) throws ParamException {
        if (obj) {
            throw new ParamException(ExceptionCode.PARAM_ERROR, msg);
        }

    }
    public static void isTrueParam(Boolean obj, String msg) throws ParamException {
        if (obj) {
            throw new ParamException(ExceptionCode.PARAM_ERROR, msg);
        }

    }

    /**
     * @description 判断是否是数字  非数字则抛出异常
     * @author CF create on 2017/6/26 16:08
     */
    public static void isNumber(String num, String code, String msg) throws ParamException {
        String regEx = "^[0-9]+$";
        Pattern pat = Pattern.compile(regEx);
        Matcher mat = pat.matcher(num);
        if (!mat.find()) {
            throw new ParamException(code, msg);
        }

    }

    /**
     * 空值/空字符串判断
     *
     * @param str
     * @param errorEnum
     */
    public static void isEmpty(String str, ErrorEnum errorEnum) {
        if (!StringUtils.isEmpty(str)) {
            return;
        }
        throw new BusinessException(errorEnum);
    }

    /**
     * 字符串判空，默认返回的错误是参数错误
     *
     * @param str
     */
    public static void isEmpty(String str) {
        if (!StringUtils.isEmpty(str)) {
            return;
        }
        isEmpty(str, ErrorEnum.PARAM_ERROR);
    }

    /**
     * 字符串判空，默认返回的错误是参数错误
     *
     * @param str
     */
    public static boolean isNull(String str) {
        if (!StringUtils.isEmpty(str)) {
            return false;
        }
        return true;
    }

    /**
     * 空值/空字符串判断
     *
     * @param object
     * @param errorEnum
     */
    public static void isNull(Object object, ErrorEnum errorEnum) {
        if (!StringUtils.isEmpty(object)) {
            return;
        }
        throw new BusinessException(errorEnum);
    }

    /**
     * 空值/空字符串判断，默认返回的错误是参数错误
     *
     * @param object
     */
    public static void isNull(Object object) {
        isNull(object, ErrorEnum.PARAM_ERROR);
    }


}
