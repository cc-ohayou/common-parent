package com.ccspace.server.web.intercept;


import com.alibaba.fastjson.JSON;

import com.ccspace.facade.domain.common.constants.HeaderKeys;
import com.ccspace.facade.domain.common.constants.RedisConstants;
import com.ccspace.facade.domain.common.enums.ErrorEnum;
import com.ccspace.facade.domain.common.util.HandlerAnnotationUtil;
import com.ccspace.manager.mo.RedisManager;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.resource.ResourceHttpRequestHandler;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;


/**
 * 只会拦截spring mvc注解的Controller
 *
 * @AUTHOR CF
 * @DATE Created on 2017/6/15 14:19.
 */
@Component
public class SessionInterceptor extends BaseInterceptor {
    @Resource
    RedisManager redisManager;
    /*@Resource
    UserManager userManager;
*/

    private Logger logger = LoggerFactory.getLogger(SessionInterceptor.class);
    //app启动必然且只访问一次的接口
    private final String APP_START_ONCE_VISIT_PATH = "/queryMerchantInfo";

    /**
     * 在业务处理器处理请求之前被调用
     * 如果返回false
     * 从当前的拦截器往回执行所有拦截器的afterCompletion(),再退出拦截器链
     * 如果返回true
     * 执行下一个拦截器,直到所有的拦截器都执行完毕
     * 再执行被拦截的Controller
     * 然后进入拦截器链,
     * 从最后一个拦截器往回执行所有的postHandle()
     * 接着再从最后一个拦截器往回执行所有的afterCompletion()
     */
    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response, Object handler) throws Exception {
        request.setCharacterEncoding("utf-8");
        response.setCharacterEncoding("utf-8");
        if (super.preHandle(request, response, handler)) {
            return true;
        }

        if (handler instanceof ResourceHttpRequestHandler) {
            return true;
        }
        /*if (ipUaUrlLimitTrue((HandlerMethod) handler, request, response)) {
            return false;
        }*/
        if (HandlerAnnotationUtil.annotationJudgePassed((HandlerMethod) handler)) {
            return true;
        }
        String requestUri = request.getRequestURI();
        String lastPath = requestUri.substring(requestUri.lastIndexOf("/"));
        logger.info(String.format("----收到请求:%s,请求方式:%s", requestUri, request.getMethod()));
        if (StringUtils.isEmpty(request.getHeader(HeaderKeys.SID))) {
            returnErrorMessage(response, ErrorEnum.AUTH_FAILED.getCode(), ErrorEnum.AUTH_FAILED.getMsg());
            return false;
        }
       /* boolean sessionValid = checkSessionId(request);
        if (!sessionValid) {
            logger.info("session expired sid={}", request.getHeader(HeaderKeys.SID));
            returnErrorMessage(response, ErrorEnum.AUTH_FAILED.getCode(), ErrorEnum.AUTH_FAILED.getMsg());
            return false;
        }
        if (APP_START_ONCE_VISIT_PATH.equals(lastPath)) {
            //app启动后根据会话是否过期更新会话更新时间
            updateSessionExpireTime(request);
        }*/

        return true;
    }


    private boolean ipUaUrlLimitTrue(HandlerMethod method, HttpServletRequest request, HttpServletResponse response) throws IOException {
        //判断开关是否打开
        if (!StringUtils.isEmpty(redisManager.get(RedisConstants.IP_SWITCH))) {
            //判断方法是否被拦截
            if ((HandlerAnnotationUtil.rateLimitInterceptOn(method) || specialUrlListContainsReqUrl(request))) {
                //黑白名单判断以及判断是否到达限制
                if (ipUaInfoReachLimit(request)) {
                    returnErrorMessage(response, ErrorEnum.VISIT_TOO_MUCH.getCode(), ErrorEnum.VISIT_TOO_MUCH.getMsg());
                    return true;
                }
            }
        }

        return false;
    }

    private boolean specialUrlListContainsReqUrl(HttpServletRequest request) {
        String urls = redisManager.get(RedisConstants.IP_UA_LIMIT_URLS);
        return !StringUtils.isEmpty(urls) && urls.contains("," + request.getRequestURI());
    }

    /**
     * @description 统一ip和ua信息的限制是否达到 使用redis进行特定url的频率拦截
     * @author CF create on 2018/3/23 14:31
     */
    private boolean ipUaInfoReachLimit(HttpServletRequest request) {
        String realIp = getIpAddress(request);
        String uaInfo = request.getHeader("user-agent");
        String url = request.getRequestURI();
        return redisManager.ipUaInfoReachLimit(uaInfo, realIp, url);
    }

    //校验会话id  超时返回false  不存在返回false
   /* private boolean checkSessionId(HttpServletRequest request) {
        String userId = request.getHeader(HeaderKeys.UID);
        String merchantId = request.getHeader(HeaderKeys.MERCHANT_ID);
        String sid = request.getHeader(HeaderKeys.SID);
        String sysType = request.getHeader(HeaderKeys.SYS_TYPE);
        logger.info("sysType=" + sysType + ",sid=" + sid);
        SessionDO session = getNewSessionDO(userId, merchantId, sid);
        if (session != null) {
            logger.info("db sid=" + session.getSid() + ",req sid=" + sid);
            return checkExpireTimePassed(session.getExpireTime());
        }

        return false;
    }


    private SessionDO getNewSessionDO(String userId, String merchantId, String sid) {
        SessionDO param = new SessionDO();
        param.setSid(sid);
        param.setMerchantId(merchantId);
        param.setUserId(userId);
        return userManager.getUserSession(param);
    }*/

    /**
     * @description 更新会话时间
     * @author CF create on 2017/11/30 15:45
     */
   /* private void updateSessionExpireTime(HttpServletRequest request) {
        String requestUri = request.getRequestURI();
        String lastPath = requestUri.substring(requestUri.lastIndexOf("/"));
        SessionDO param = initSessionParam(request);
        //userId为空说明未登录状态必须去登录 不为空说明打开app后之前是登录状态
        // 如果会话没有超时则更新会话时间 超时则不做操作等其他接口超时校验 重新登陆
        if (!StringUtils.isEmpty(param.getUserId()) && APP_START_ONCE_VISIT_PATH.equals(lastPath)) {
            SessionDO session = userManager.getUserSession(param);
            //有会话没超时则更新会话有效时间
            if (session != null && checkExpireTimePassed(session.getExpireTime())) {
                updateSessionUpdateTime(param);
            }
        }
    }

    private SessionDO initSessionParam(HttpServletRequest request) {
        String userId = request.getHeader(HeaderKeys.UID);
        String merchantId = request.getHeader(HeaderKeys.MERCHANT_ID);
        String sid = request.getHeader(HeaderKeys.SID);
        SessionDO param = new SessionDO();
        param.setSid(sid);
        param.setMerchantId(merchantId);
        param.setUserId(userId);
        return param;
    }

    private void updateSessionUpdateTime(SessionDO param) {
        SessionDO sessionDO = new SessionDO();
        sessionDO.setUpdateTime(new Date());
        Example example = new Example(sessionDO.getClass());
        example.createCriteria().andEqualTo("userId", param.getUserId())
                .andEqualTo("merchantId", param.getMerchantId())
                .andEqualTo("sid", param.getSid());
        userManager.updateSessionByExample(sessionDO, example);
    }
*/
    /**
     * @description 会话过期返回 true
     * @author CF create on 2018/4/24 14:11
     */
    private boolean checkExpireTimePassed(String expireTime) {
        long currentTime = System.currentTimeMillis();
        boolean result = Long.valueOf(expireTime) > currentTime ? true:false;
        if (result) {
            logger.info("== 当前时间秒戳：{}，过期时间：{}, 失效时间：{}== ", expireTime, currentTime, expireTime);
        }
        return result;
    }


    /**
     * 在业务处理器处理请求执行完成后,生成视图之前执行的动作
     * 可在modelAndView中加入数据，比如当前时间
     */
    @Override
    public void postHandle(HttpServletRequest request,
                           HttpServletResponse response, Object handler,
                           ModelAndView modelAndView) throws Exception {

    }

    /**
     * 在DispatcherServlet完全处理完请求后被调用,可用于清理资源等
     * <p>
     * 当有拦截器抛出异常时,会从当前拦截器往回执行所有的拦截器的afterCompletion()
     */
    @Override
    public void afterCompletion(HttpServletRequest request,
                                HttpServletResponse response, Object handler, Exception ex)
            throws Exception {
//        limitLog.info("@####################afterCompletion test");
    }

    /**
     * @param
     * @return
     * @description 返回错误信息
     * @author CF create on 2017/7/12 16:05
     */
    private void returnErrorMessage(HttpServletResponse response, int code, String errorMessage) throws IOException {
        CommonResult rst = new CommonResult(code, null, errorMessage);
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        String res = JSON.toJSONString(rst);
        out.print(res);
        out.flush();
        out.close();
    }


    /**
     * @AUTHOR CF
     * @DATE Created on 2017/7/12 14:28.
     */
    public class CommonResult implements Serializable {
        private int code;

        private Object data;

        private String msg;

        public CommonResult() {
            this(0, null, "");
        }

        public CommonResult(Object data) {
            this(0, data, "");
        }

        public CommonResult(int code, String message) {
            this(code, null, message);
        }

        public CommonResult(int code, Object data, String message) {
            this.code = code;
            this.data = data != null ? data : new HashMap<String, String>();
            this.msg = message != null ? message : "";
        }

        public int getCode() {
            return code;
        }

        public void setCode(int code) {
            this.code = code;
        }

        public Object getData() {
            return data;
        }

        public void setData(Object data) {
            this.data = data;
        }

        public String getMsg() {
            return msg;
        }

        public void setMsg(String message) {
            this.msg = message;
        }
    }




}
