package com.ccspace.facade.domain.common.anotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * describe：自定义使方法跳过拦截的注解
 * 1. @Documented – 表示使用该注解的元素应被javadoc或类似工具文档化，它应用于类型声明，
 * 类型声明的注解会影响客户端对注解元素的使用。如果一个类型声明添加了Documented注解，
 * 那么它的注解会成为被注解元素的公共API的一部分。
 * 2. @Target – 表示支持注解的程序元素的种类，一些可能的值有TYPE, METHOD,
 * CONSTRUCTOR, FIELD等等。如果Target元注解不存在，那么该注解就可以使用在任何程序元素之上。
 * 3. @Inherited – 表示一个注解类型会被自动继承，如果用户在类声明的时候查询注解类型，
 * 同时类声明中也没有这个类型的注解，那么注解类型会自动查询该类的父类，这个过程将会不停地重复，
 * 直到该类型的注解被找到为止，或是到达类结构的顶层（Object）。
 * 4. @Retention – 表示注解类型保留时间的长短，它接收RetentionPolicy参数，
 * 可能的值有SOURCE, CLASS, 以及RUNTIME。
 * author: CF
 * date: 2018年01月24日
 **/
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface InterceptRequired {
    //默认true 收到拦截器校验
    boolean required() default true;
    //频率校验标识 非空则进行拦截频率限制 默认不校验
    String rateLimit() default "";
}
