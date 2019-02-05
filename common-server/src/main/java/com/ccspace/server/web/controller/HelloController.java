package com.ccspace.server.web.controller;

import com.ccspace.facade.domain.common.anotation.InterceptRequired;
import com.ccspace.server.core.service.SupportService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @AUTHOR CF
 * @DATE Created on 2018/4/14/014 18:31.
 */
@RestController("/hello")
@RequestMapping(value = "/hello",produces = "application/json;charset=utf-8")
public class HelloController {
    @Resource
    SupportService supportService;

    @InterceptRequired(required = false)
    @RequestMapping(value = "/say")
    @ResponseBody
    public String index() {
        return "Hello!  project is alive!";
    }

    @InterceptRequired(required = false)
    @RequestMapping(value = "/testDb")
    @ResponseBody
    public Object daoTest(){
        return supportService.daotest();
    }



}