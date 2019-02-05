package com.ccspace.server.web.aop;


import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

@ControllerAdvice(basePackages = "com.ccspace.server.web.controller")
public class ApiResponseAdvice implements ResponseBodyAdvice<Object> {


    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
       /* if(converterType.equals(MappingJackson2HttpMessageConverter.class)){
            return true;
        }
        return false;*/
        return true;
    }

    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType,
                                  MediaType selectedContentType, Class<? extends HttpMessageConverter<?>> selectedConverterType,
                                  ServerHttpRequest request, ServerHttpResponse response) {
        // 连连回调返回的不在包装
        if(request.getURI().getPath().contains("receiveNotify")){
            return body;
        }
        //先做版本判断，然后加密处理
        ApiResponse succ=ApiResponse.success().setData(body);
        return succ;
    }
}
