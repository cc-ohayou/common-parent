package com.ccspace.facade.domain.common.enums;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import javax.annotation.PostConstruct;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @AUTHOR CF 配置掌管者
 * @DATE Created on 2018/10/30 10:09.
 */
@Slf4j
public final class SettingsHolder {


    public static final String GLOBAL_SETTINGS = "common-server-hash-global-settings";
    private static Map<String,String> properties=new ConcurrentHashMap<>();
    public static Map<String,String> getAllProperties(){
        return properties;
    }

    /**
     * @description
     * @author CF create on 2018/10/30 10:11
     */
    public static  void addProperty(String key ,String value)  {
        if(judgeKey(key)){
            log.error("SettingHolder add property key can not be empty ,key is"+key);
            return ;
        }
        log.info("###addProperty key="+key+",value="+value);
        properties.put(key,value);
    }

    public static  void addProperty(SettingsEnum keyEnum ,String value) {
        if(judgeKey(keyEnum.getValue())){
            log.error("SettingHolder add property key can not be empty ,key is"+keyEnum.getValue());
            return ;
        }
        properties.put(keyEnum.getValue(),value);
    }

    /**
     * @description
     * @author CF create on 2018/10/30 10:21
     */
    public static  String getProperty(SettingsEnum key ) {

        if(properties.isEmpty()){
            SettingsEnum.initDefaultSettingHolderValue();
        }

        return properties.get(key.getValue());
    }



    private static boolean judgeKey(String key)  {
        return StringUtils.isEmpty(key);
    }

    public  static void removeProperty(String key ,String value) {
        judgeKey(key);
        properties.remove(key,value);
    }

    public static int  getSize(){
        return properties.size();
    }
    public static  void  clear(){
        properties.clear();
    }


    @PostConstruct
    public  void   initDefaultSettings(){

        SettingsEnum.initDefaultSettingHolderValue();
    }

}
