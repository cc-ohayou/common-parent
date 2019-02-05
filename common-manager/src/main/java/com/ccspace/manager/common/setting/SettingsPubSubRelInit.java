package com.ccspace.manager.common.setting;


import com.ccspace.facade.domain.common.enums.SettingsEnum;
import com.ccspace.facade.domain.common.enums.SettingsHolder;
import com.ccspace.manager.common.config.properties.TestLoadResource;
import com.ccspace.manager.mo.RedisManager;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @AUTHOR CF
 * @DATE Created on 2018/12/7 15:27.
 */
@Slf4j
@Component
public class SettingsPubSubRelInit {
    public static final Logger logger = LoggerFactory.getLogger(SettingsPubSubRelInit.class);

    @Resource
    RedisManager redisManager;
    @Resource
    SettingsPubSub settingsJedisPubSub;
    private static ExecutorService jedisPubSubPool = Executors.newSingleThreadExecutor();
    private static  boolean hasInitAlready=false;


    @PostConstruct
    public void init(){

        if(hasInitAlready){
            log.info("####GlobalConfigInit hasInitAlready");
            return;
        }
        hasInitAlready=true;
        try{
            TestLoadResource.loadProperties();

            String [] patterns= RedisChannel.getAllPsNeedSubChannels();
            Map<String,String> settings=redisManager.hgetAll(SettingsHolder.GLOBAL_SETTINGS);

            logger.info("####GlobalConfigInit init GLOBAL_SETTINGS="+settings.toString());
            SettingsEnum.initWork(settings);
            logger.info("####GlobalConfigInit init SettingHolder properties="+ SettingsHolder.getAllProperties().toString());
            jedisPubSubPool.execute(() ->redisManager.psubscribe(settingsJedisPubSub,
                    "订阅ps中的频道:" + Arrays.toString(patterns), patterns));

        }catch(Exception e){
            logger.warn("#####error SettingsPubSubRelInit  config：",e);
        }
    }


}
