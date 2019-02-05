package com.ccspace.manager.common.setting;

import com.ccspace.facade.domain.common.enums.SettingsHolder;
import com.ccspace.manager.mo.RedisManager;
import com.google.common.collect.Sets;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import redis.clients.jedis.JedisPubSub;

import javax.annotation.Resource;
import java.util.Set;

/**
 * @AUTHOR CF
 * @DATE Created on 2018/12/7 15:13.
 */
@Component
public class SettingsPubSub extends JedisPubSub {
    private Logger logger= LoggerFactory.getLogger(SettingsPubSub.class);
    @Resource
    RedisManager redisManager;

    private static Set<String> patterns= Sets.newConcurrentHashSet();
    public void logInfo(String msg){
        logger.info("###SettingsPubSub {}",msg);
    }

    public void logWarn(String msg, Exception e){
        logger.warn("###SettingsPubSub {}",msg,e);
    }


    @Override
    public void onMessage(String channel, String message) {
        super.onMessage(channel, message);
        logInfo("onMessage received channel=" + channel + " message=" + message);
        //根据频道不同进入不同的处理逻辑 后续逻辑越来越多的话可根据策略设计模式优化代码
        boolean updateFlag=false;
        String title=" 接收信息成功";
        try{
            if(RedisChannel.TEST_CHANNEL.getValue().equals(channel)){
                updateFlag=doUpdateSettingWorkByKey(message);
            }
            logInfo(title+",udpateFlag="+updateFlag);
        }catch(Exception e){
            logWarn("###SettingsPubSub onPMessage failed",e);

        }
    }

    @Override
    public void onPMessage(String pattern, String channel, String message) {
        super.onPMessage(pattern, channel, message);
        logInfo("###PsJedisPubSub onPMessage received pattern=" + pattern + " channel=" + channel + " msg=" + message);
        //根据频道不同进入不同的处理逻辑 后续逻辑越来越多的话可根据策略设计模式优化代码
        boolean updateFlag=false;
        String title=" 接收信息成功";
        try{
            if(RedisChannel.TEST_CHANNEL.getValue().equals(channel)){
                updateFlag=doUpdateSettingWorkByKey(message);
            }
        logInfo(title+",udpateFlag="+updateFlag);
        }catch(Exception e){
            logWarn("###SettingsPubSub onPMessage failed",e);

        }
    }



    /**
     * @description 根据message（value一般为对应redis的配置key）更新配置的任务
     * @author CF create on 2018/10/24 16:52
     */
    private boolean doUpdateSettingWorkByKey(String message) {
        //特殊的需要转化处理的json格式的配置类型 需要额外的逻辑处理的根据自定义的

        return updateGeneralSettings(message);

    }

    /**
     * @description
     * @author CF create on 2018/10/30 11:18
     */
    private boolean updateGeneralSettings(String message) {
        SettingsHolder.addProperty(message, redisManager.hget(SettingsHolder.GLOBAL_SETTINGS, message));
        return true;
    }

    @Override
    public void onSubscribe(String channel, int subscribedChannels) {
        super.onSubscribe(channel, subscribedChannels);
        logInfo("onSubscribe  " + channel + " subscribedChannels=" + subscribedChannels);


    }

    @Override
    public void onPSubscribe(String pattern, int subscribedChannels) {
        if(patterns.contains(pattern)){
            logInfo("####already pSubscribe this channel pattern="+pattern+" just return");
        }
        super.onPSubscribe(pattern, subscribedChannels);
        patterns.add(pattern);
        logInfo("onPSubscribe  pattern=" + pattern + " subscribedChannels=" + subscribedChannels);

    }



}
