package com.ccspace.facade.domain.common.util;

import com.ccspace.facade.domain.common.anotation.InterceptRequired;
import org.springframework.web.method.HandlerMethod;

/**
 * @AUTHOR CF
 * @DATE Created on 2018/1/24 21:15.
 */
public class HandlerAnnotationUtil {
    public static boolean annotationJudgePassed(HandlerMethod handler) {
        return handler.getMethodAnnotation(InterceptRequired.class) != null &&
                !handler.getMethodAnnotation(InterceptRequired.class).required();
    }
    public static boolean rateLimitInterceptOn(HandlerMethod handler) {
        return handler.getMethodAnnotation(InterceptRequired.class) != null &&
                !"".equals(handler.getMethodAnnotation(InterceptRequired.class).rateLimit());
    }
}
