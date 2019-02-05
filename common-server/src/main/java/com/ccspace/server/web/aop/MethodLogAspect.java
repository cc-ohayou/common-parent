package com.ccspace.server.web.aop;


import com.ccspace.facade.domain.common.exception.BizException;
import com.ccspace.facade.domain.common.exception.BusinessException;
import com.ccspace.facade.domain.common.exception.ParamException;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;

/**  * describe: spring aop只支持方法几倍的 Joinpoint
	 * @author CAI.F
	 * @date:  日期:2018/12/1/001 时间:17:30
	 * @param
	 */
@Aspect
public abstract class MethodLogAspect {

    private Logger logger = LoggerFactory.getLogger(MethodLogAspect.class);





    @Pointcut("within(com.ccspace.server.web.controller..*)")
    public void pointName(){}
    @Pointcut("execution(public void *.getUser())")
    public void pointName2(){}
    /**  * describe:
     * pointCut表达式三部分
     * 返回类型  方法名 参数类型
      表达式字符 * 可用于这三部分任意位置的匹配
     void *(*) 返回类型为空 名称不限参数类型不限但只有一个参数的所有方法
      * *(String,*) 匹配所有两个参数的方法 第一个参数为String

     ..符号 则可以用于 后两部分的匹配  用于方法参数列表处
     void getUser(..)表示匹配的方法可以有多个参数
     用于名称位置
     com.cc.ccbootdemo.web.controller..
     表示匹配com.cc.ccbootdemo.web.controller下的所有类型以及
     这个路径下层包的所有类型

     表达式也可以多个一起使用 由于
     spring aop是对aspect的包装实现aspect是基于java的实现逻辑运算符是一样的
     但在xml配置时由于是按apring语义定制的 会用 and  or这些来表示
     *   ..匹配
    	 * @author CAI.F
    	 * @date:  日期:2018/12/1/001 时间:17:47
    	 * @param
    	 */
    @Pointcut("execution(public void *.getUser()) ||execution(void *(*)) ")
    public void pointName3(){}
    /***
     * 记录业务日志切面
     *
     * @param joinPoint
     * @return
     * @throws Throwable
     */
//    @Around(value="pointName()||pointName2")
    @Around(value="pointName()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature joinPointObject = (MethodSignature) joinPoint.getSignature();
        Method method = joinPointObject.getMethod();
        String methodName = method.getName();
        Object[] args = joinPoint.getArgs();
        long startTime = System.nanoTime();
        Object result = null;

        try {
            result = joinPoint.proceed(args);
            recordlog(joinPointObject, methodName, args, startTime, result, System.nanoTime());
        } catch (ParamException e) {
            logger.warn("MethodLogAspect.around",e);
            recordlog(joinPointObject, methodName, args, startTime, result, System.nanoTime());
            throw new ParamException(e.getMessage());
        }catch (BusinessException e) {
            logger.warn("MethodLogAspect.around", e);
            recordlog(joinPointObject, methodName, args, startTime, result, System.nanoTime());
            throw new BusinessException(e.getMessage());
        }catch (BizException e) {
            logger.warn("MethodLogAspect.around", e);
            recordlog(joinPointObject, methodName, args, startTime, result, System.nanoTime());
            throw new BizException(e.getMessage());
        }catch (Exception e) {
            logger.error("MethodLogAspect.around", e);
            recordlog(joinPointObject, methodName, args, startTime, result, System.nanoTime());
            throw new Exception(e);
        }

        return result;
    }

    @Pointcut("execution(* com.cc.ccspace.service..*.*(..))")
    public void pointCut(){}

    @Before("pointCut()")
    public void doBefore(JoinPoint joinPoint){
        System.out.println("AOP Before pointCut  Advice...");
    }

    @After("pointCut()")
    public void doAfter(JoinPoint joinPoint){
        System.out.println("AOP After pointCut Advice...");
    }

    @AfterReturning(pointcut="pointCut()",returning="returnVal")
    public void afterReturn(JoinPoint joinPoint, Object returnVal){
        System.out.println("AOP AfterReturning pointCut Advice:" + returnVal);
    }

    /**
     * @description :Spring AOP的环绕通知会影响到AfterThrowing通知的运行,
     * 不要同时使用!同时使用也没啥意义
     * @author CF create on 2018/12/4 11:26
     */
    /*@AfterThrowing(pointcut="pointCut()",throwing="error")
    public void afterThrowing(JoinPoint joinPoint,Throwable error){
        System.out.println("AOP AfterThrowing pointCut Advice..." + error);
        System.out.println("AfterThrowing...");
    }*/



    private void recordlog(MethodSignature joinPointObject, String methodName, Object[] args, long startTime, Object result, long endTime) {
        String logString = extractLog(joinPointObject, result, args, methodName, startTime, endTime);
        Logger customLogger = getLogger();
        if (customLogger == null) {
            customLogger = logger;
        }
        customLogger.info(logString);
    }

    protected abstract Logger getLogger();

    protected abstract String extractLog(MethodSignature joinPointObject, Object result, Object[] args, String methodName, long startTime, long endTime);

    /**
     * 获取完整的日志
     *
     * @param request
     * @param error
     * @param exception
     */
    protected abstract String extractLog(HttpServletRequest request, String error, Exception exception);


    /**
     * 获取时间，单位：微妙 μs
     *
     * @param startTime 单位：纳秒
     * @param endTime   单位：纳秒
     * @return
     */
    protected String getCostTime(long startTime, long endTime) {
        return (endTime - startTime) / 1000 + "μs";
    }


    /**
     * 给没被切面捕获的方法提供日志入口，目前只有方法抛出异常的时候才会跳出切面
     *
     * @param request
     * @param error
     * @param outException
     */
    public void logProxy(HttpServletRequest request, String error, Exception outException) {
        try {
            String logString = extractLog(request, error, outException);
            Logger customLogger = getLogger();
            if (customLogger == null) {
                customLogger = logger;
            }
            customLogger.info(logString);
        } catch (Exception exception) {
            logger.error("MethodLogAspect.around", exception);
        }
    }
}