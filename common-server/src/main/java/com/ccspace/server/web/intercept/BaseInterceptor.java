package com.ccspace.server.web.intercept;

import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @AUTHOR CF
 * @DATE Created on 2018/8/7 11:14.
 */
public abstract class BaseInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        return isCrosRequest(request, response);
    }

    /**
     * @description  解决跨域请求的问题
     * 跨域请求科普博客
     * http://www.ruanyifeng.com/blog/2016/04/cors.html
     *
     * @author CF create on 2018/8/7 15:09
     */
    private boolean isCrosRequest(HttpServletRequest request, HttpServletResponse response) {
        boolean flag=false;
        //如果是预检请求 设置下allow的header参数 OPTIONS为预检请求
        if(request.getHeader("Access-Control-Request-Method") != null && "OPTIONS".equals(request.getMethod())){
            response.setHeader("Access-Control-Allow-Origin", "*");
            response.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
            //鉴于header中存放了很多业务相关的信息 直接用*匹配
            response.setHeader("Access-Control-Allow-Headers", "*");
            response.setHeader("Access-Control-Request-Headers", "*");
            response.setHeader("Access-Control-Max-Age", "3600");//30min
            flag=true;
        }else{
            //   允许跨域，允许复杂请求  也可以使用,号隔开的n个域名指定死某些域名允许访问
            response.setHeader("Access-Control-Allow-Origin", "*");
        }
        return flag;
    }
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }


    /**
     * 获取请求主机IP地址,如果通过代理进来，则透过防火墙获取真实IP地址;
     *
     * @param request
     * @return
     * @throws IOException
     */
    public static String getIpAddress(HttpServletRequest request) {

        // 获取请求主机IP地址,如果通过代理进来，则透过防火墙获取真实IP地址

        String ip = request.getHeader("X-Forwarded-For");

        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            if (ip == null || ip.length() == 0
                    || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getHeader("Proxy-Client-IP");
            }
            if (ip == null || ip.length() == 0
                    || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getHeader("WL-Proxy-Client-IP");
            }
            if (ip == null || ip.length() == 0
                    || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getHeader("HTTP_CLIENT_IP");
            }
            if (ip == null || ip.length() == 0
                    || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getHeader("HTTP_X_FORWARDED_FOR");
            }
            if (ip == null || ip.length() == 0
                    || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getRemoteAddr();
            }
        } else if (ip.length() > 15) {
            String[] ips = ip.split(",");
            for (int index = 0; index < ips.length; index++) {
                String strIp = ips[index];
                if (!("unknown".equalsIgnoreCase(strIp))) {
                    ip = strIp;
                    break;
                }
            }
        }
        return ip;
    }
}
