package com.ccspace.server.web.intercept;



import com.ccspace.facade.domain.bizobject.HeaderInfo;
import com.ccspace.facade.domain.common.exception.BizException;
import com.ccspace.facade.domain.common.exception.BusinessException;
import com.ccspace.facade.domain.common.exception.ParamException;
import com.ccspace.facade.domain.common.threadlocals.HeaderInfoHolder;
import com.ccspace.facade.domain.common.util.BeanUtil;
import com.ccspace.facade.domain.common.util.IpUtil;
import com.ccspace.server.web.aop.ApiResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

public class ExceptionInterceptor extends BaseInterceptor {

    private static Logger log = LoggerFactory.getLogger(ExceptionInterceptor.class);

    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response, Object handler) throws Exception {

        Map headerInfoMap = new HashMap();
        Enumeration headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String key = (String) headerNames.nextElement();
            String value = request.getHeader(key);
            headerInfoMap.put(key, value);
        }
        headerInfoMap.put("ip", getIpAddress(request));
        HeaderInfo info = new HeaderInfo();
        try {
            info= BeanUtil.mapToObject(headerInfoMap,HeaderInfo.class);
            HeaderInfoHolder.setHeaderInfo(info);
        } catch (Exception e) {
            log.error("Header map transfer to bean error!",e);
        }
        return true;
    }


    @Override
    public void postHandle(HttpServletRequest request,
                           HttpServletResponse response, Object handler,
                           ModelAndView modelAndView) throws Exception {
    }

    @Override
    public void afterCompletion(HttpServletRequest request,
                                HttpServletResponse response, Object handler, Exception ex)
            throws Exception {
        ApiResponse apiResponse = ApiResponse.error();
        if (ex != null) {
            if (null == ex.getMessage()) {
                log.error(ex.toString());
            } else {
                log.error(ex.getMessage());
            }
            if (ex instanceof BusinessException) {
                BusinessException exception = (BusinessException) ex;
                apiResponse.setCode(Integer.valueOf(exception.getErrorCode()))
                        .setMsg(exception.getErrorMessage());
                if (exception.getData() != null) {
                    apiResponse.setData(exception.getData());
                }
            } else if (ex instanceof ParamException) {
                String errorCode = ((ParamException) ex).getErrorCode();
                String errorMsg = ((ParamException) ex).getErrorMessage();
                apiResponse.setCode(Integer.valueOf(errorCode)).setMsg(errorMsg);
            }else if (ex instanceof BizException) {
                String errorCode = ((BizException) ex).getErrorCode();
                String errorMsg = ((BizException) ex).getErrorMessage();
                apiResponse.setCode(Integer.valueOf(errorCode)).setMsg(errorMsg);
            } else {
                apiResponse.setCode(9999).setMsg("系统异常");
            }
            ObjectMapper mapper = new ObjectMapper();
            String json = mapper.writeValueAsString(apiResponse);
            response.reset();
            response.setContentType("application/json;charset=UTF-8");
            response.setHeader("Pragma", "No-cache");
            response.setHeader("Cache-Control", "no-cache");
            response.setDateHeader("Expires", 0);
            PrintWriter out = response.getWriter();
            out.print(json);
            out.flush();
            out.close();
        }
    }




}