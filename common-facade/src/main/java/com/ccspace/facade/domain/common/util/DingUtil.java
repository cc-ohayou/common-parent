package com.ccspace.facade.domain.common.util;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.entity.StringEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * @AUTHOR CF
 * @DATE Created on 2018/7/4 18:10.
 */
public class DingUtil {

    private static String WEBHOOK_TOKEN = "https://oapi.dingtalk.com/robot/send?access_token=dc3f7f13439a4cc92e6132ed2a81574d94d906c683f9150a5ee6db424257f5be";
    private static String WEBHOOK_URL = "https://oapi.dingtalk.com/robot/send?access_token=";
//    https://oapi.dingtalk.com/robot/send?access_token=87a3245e08d5344dc9df811040be75f84dece1522ef821676be43124bf6e4d57
    public static String TEST_TOKEN = "87a3245e08d5344dc9df811040be75f84dece1522ef821676be43124bf6e4d57";
    //监控定时任务执行情况机器人的token 线上

    private static final String DEFAULT_TOKEN="87a3245e08d5344dc9df811040be75f84dece1522ef821676be43124bf6e4d57";

    private static Logger log = LoggerFactory.getLogger(DingUtil.class);

    public static void sendDingMsg(String accessToken, String textMsg) {
        try{
            if(StringUtils.isEmpty(accessToken)){
                accessToken=DEFAULT_TOKEN;
            }
            log.info("####sendDingMsg accessToken=" + accessToken);
            StringEntity se = new StringEntity(textMsg, "utf-8");
            String url=WEBHOOK_URL+accessToken;
            String result = HttpClientUtil.sendPost(url, "application/json; charset=utf-8", se, "utf-8", null);
            log.info("####sendDingMsg result=" + result);
        }catch(Exception e){
            log.warn("####sendDingMsg failed",e);
        }

    }

    public static void sendDingMsg(String accessToken, String title, String content, boolean isAtAll) {
        try{
            if(StringUtils.isEmpty(accessToken)){
                accessToken=DEFAULT_TOKEN;
            }
            content=content+"\n localIp="+ IpUtil.getLocalIp();
            log.info("####sendDingMsg accessToken=" + accessToken);
            StringEntity se = new StringEntity(getMsg(title,content,isAtAll), "utf-8");
            String url=WEBHOOK_URL+accessToken;
            String result = HttpClientUtil.sendPost(url, "application/json; charset=utf-8", se, "utf-8", null);
            log.info("####sendDingMsg result=" + result);
        }catch(Exception e){
            log.warn("####sendDingMsg failed",e);
        }

    }

    /**
     * @description  根据标题和内容@方式组装钉钉消息
     * @author CF create on 2018/11/2 10:53
     */
    public static String getMsg(String title, String content, boolean isAtAll){
        return  "{\"msgtype\": \"markdown\"," +
                "\"markdown\": " +
                "{\"title\":\"" +title+ "\"," +
                "\"text\":\""+content+ "\"}," +
                "\"at\": {\"isAtAll\":"+isAtAll+
                " }" +
                "}";
        //{"msgtype": "markdown","markdown": {"title":全局配置key重复性检查报警,"text":发现重复key:ps-modify-profit-max-times,key describe:止盈调整下限倍数(相对于现价而言),请检查修正枚举类 SettingsEnum},"at": {"isAtAll":true }}
    }


    public static void main(String args[]) throws Exception {
        String textMsg = "{\"msgtype\": \"markdown\",\"markdown\": {\"title\":\"# 测试消息\"," +
                "\"text\":\"# you over died   \n @18868832010  \\n > ![screenshot](http://pic29.photophoto.cn/20131223/0009021147525895_b.jpg)\\n  \"}," +
                "\"at\": {\"atMobiles\": [\"13758080693\",\"18868832010\"], \"isAtAll\": false}}";

      /*  String dingMsg="{\"msgtype\": \"markdown\",\"markdown\": {\"title\":\"报警消息\"," +
                "\"text\":\"## ****新系统交易结算队列堆积警告****   \n\n " +
                "#### 卖出队列size=" +
               212+
                " \n\n #### 买入队列size=" +
                456+
                " \n  \\n   \"}," +
                "\"at\": {\"isAtAll\": true}}";*/
        //sendDingMsg(TEST_TOKEN, textMsg);
        //分发
        sendDingMsg(TEST_TOKEN, textMsg);
//        sendDingMsg(TEST_TOKEN, dingMsg);
    }
}