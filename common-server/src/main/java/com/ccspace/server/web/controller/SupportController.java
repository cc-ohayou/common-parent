package com.ccspace.server.web.controller;


import com.alibaba.fastjson.JSON;
import com.ccspace.facade.domain.common.anotation.InterceptRequired;
import com.ccspace.facade.domain.common.enums.SettingsHolder;
import com.ccspace.server.core.service.SupportService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * @AUTHOR CF
 * @DATE Created on 2018/8/29 18:04.
 */
@Slf4j
@Controller
@RequestMapping(value = "/{ver}/support")
public class SupportController {

    @Resource
    SupportService supportService;


   /* @ResponseBody
    @InterceptRequired(required = false)
    @RequestMapping(value = "/getCustomProperties")
    public CustomProperties getCustomProperties(){
        return supportService.getCustomProperties(HeaderInfoHolder.getMerchantId(), HeaderInfoHolder.getUserId());
    }*/

   /* @ResponseBody
    @RequestMapping("/clean/pushConfig")
    @InterceptRequired(required = false)
    public void cleanPushConfig() {
        pushService.cleanPushConfig();
    }

    @ResponseBody
    @RequestMapping("/get/subConfig")
    @InterceptRequired(required = false)
    public PersonalSubConfig getSubConfig(GateWayTransferParam param) {
        param.setMerchantId(HeaderInfoHolder.getMerchantId());
        param.setUserId(HeaderInfoHolder.getUserId());
        return  userManager.getSubConfigByUid(param.getUserId(), param.getMerchantId());
    }

    @ResponseBody
    @RequestMapping("/pubMsg")
    @InterceptRequired(required = false)
    public void pubMsgToChannel(String channelType,String msg) {
        supportService.pubMsgToChannel( channelType, msg);
    }

    *//**
     * @description patterns ["ps-settings-channel","cc-channel"]
     * @author CF create on 2018/10/24 17:00
     *//*
    @ResponseBody
    @RequestMapping("/subChannel")
    @InterceptRequired(required = false)
    public void subRedisChannelByType(@PathVariable("ver")String ver, SubChannelParam subParam) {
        supportService.subRedisChannels(subParam.getPatterns());
    }

    @ResponseBody
    @RequestMapping("/update/settings")
    @InterceptRequired(required = false)
    public void updateSettings(String key, String value) {
        SettingsHolder.addProperty(key,value);
    }
*/
    @ResponseBody
    @RequestMapping("/get/settings")
    @InterceptRequired(required = false)
    public Object getSettings() {

        Map<String,Object> map=new HashMap<>();
        map.put("globalSettings", SettingsHolder.getAllProperties());

        return map;
    }

   /* @ResponseBody
    @RequestMapping("/clean/Config")
    @InterceptRequired(required = false)
    public void cleanConfigByType(String type) {
        supportService.cleanConfigByType(type);
    }
*/}
