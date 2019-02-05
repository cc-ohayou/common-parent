package com.ccspace.facade.domain.common.util.sms;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ccspace.facade.domain.common.util.HttpClientUtil;
import com.ccspace.facade.domain.common.util.sms.Sms;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;


/**
 * @AUTHOR CF
 * @DATE Created on 2018/5/15 13:26.
 */
public class XSSender  {
    private static Logger logger = LoggerFactory.getLogger(XSSender.class);
    private static final String urlCode="http://118.31.9.79/msg/HttpBatchSendSM";//发送验证码接口
    private static final String urlDefer="http://120.78.188.185:8088/sms.aspx";//发送延期失败短信
/*    private static SmsConfig config;

    public static SmsConfig getConfig() {
        return config;
    }*/

    public static String sendCode(Sms sms)  {

        Map<String, String> params = initCodeParams(sms);
        logger.info("####sms content="+params.get("content"));
  //      String resultStr;
 //       if ("POST".equals(config.getReqType())) {
        String resultStr = HttpClientUtil.sendPost(urlCode,params, HttpClientUtil.CHARSET,HttpClientUtil.CHARSET,null,"sms","信使短信发送");
 //       }else{
 //           resultStr = HttpClientUtil.sendGet(config.getUrl(),params,HttpClientUtil.CHARSET,null,"sms","信使短信发送");
  //      }
        return resultStr;

    }

    public static String sendDefer(Sms sms)  {

        Map<String, String> params = initDeferParams(sms);
        logger.info("####sms content="+params.get("content"));
        //      String resultStr;
        //       if ("POST".equals(config.getReqType())) {
        String resultStr = HttpClientUtil.sendPost(urlDefer,params,HttpClientUtil.CHARSET,HttpClientUtil.CHARSET,null,"sms","信使短信发送");
        //       }else{
        //           resultStr = HttpClientUtil.sendGet(config.getUrl(),params,HttpClientUtil.CHARSET,null,"sms","信使短信发送");
        //      }
        return resultStr;

    }

/*    *//**
     * @description  返回的结果是xml格式字符串  根据返回成功还是失败进行状态转化
     * @author CF create on 2018/5/15 15:53
     *//*
    private SmsResult transferResult(String resultStr) {
        if(StringUtils.isEmpty(resultStr)){
            logger.error("##### XSSender  sms send failed result is empty,"+resultStr);
            return new SmsResult();
        }else{
           return  readStringXmlOut(resultStr);
        }
    }*/

    private static Map<String, String> initCodeParams(Sms sms) {
        Map<String, String> params = new HashMap<>(8);
        params.put("account", sms.getAccount());
        params.put("pswd", sms.getPswd());
        params.put("mobile", sms.getMobile());
        params.put("msg", sms.getMsg());
        params.put("resptype", sms.getResptype());
        params.put("needstatus",sms.getNeedStatus());
        return params;
    }


    private static Map<String, String> initDeferParams(Sms sms) {
        Map<String, String> params = new HashMap<>(8);
        params.put("action", sms.getAction());
        params.put("userid",sms.getUserId());
        params.put("account",sms.getAccount());
        params.put("password",sms.getPswd());
        params.put("mobile",sms.getMobile());
        params.put("content",sms.getMsg());
        params.put("sendTime",sms.getSendTime());
        params.put("extno",sms.getExtno());
        return params;
    }

/*    private static String replaceContent(String content) {
        return content.replaceAll("【", "[").replaceAll("】", "]");
    }


    public static void setConfig(SmsConfig smsConfig) {
        config = smsConfig;
    }*/

/*    public static void initDefaultConfig(){
        config=new SmsConfig();
        config.setUrl("http://120.78.188.185:8088/sms.aspx");
        config.setAccount("DDy888");
        config.setUserId("73");
        config.setPassword("zfND0JrZ");
        config.setAction("send");
        config.setReqType("POST");
        config.setExtno("");
        config.setSendTime("");
        config.setSign("【点点盈】");
       logger.info("initDefaultConfig XSSender config="+JSON.toJSONString(config));
        System.out.println("initDefaultConfig XSSender config="+JSON.toJSONString(config));
    }*/

    /**
     * @description 将xml字符串转换成map
     * @param xml
     * @return Map
     */
    public static Map<String,String> readStringXmlOut(String xml) {
        Map map = new HashMap();
        Document doc ;
        try {
            doc = DocumentHelper.parseText(xml); // 将字符串转为XML
            Element rootElt = doc.getRootElement(); // 获取根节点
            rootElt.element("returnstatus").getTextTrim();
            rootElt.element("message").getTextTrim();
            rootElt.element("remainpoint").getTextTrim();
            rootElt.element("taskID").getTextTrim();
            rootElt.element("successCounts").getTextTrim();
            logger.info("returnstatus="+rootElt.element("returnstatus").getTextTrim()+
                    ",message="+rootElt.element("message").getTextTrim()+
                    ",remainpoint="+rootElt.element("remainpoint").getTextTrim()
                    +",taskID="+rootElt.element("taskID").getTextTrim()+
                    ",successCounts="+ rootElt.element("successCounts").getTextTrim());
            if(!"Success".equals(rootElt.element("returnstatus").getTextTrim())){
                map.put("code","-1");
                map.put("msgid",rootElt.element("taskID").getTextTrim());
            }else{
                map.put("code","0");
                map.put("msgid",rootElt.element("taskID").getTextTrim());
            }
        } catch (Exception e) {
            logger.warn(" !!!readStringXmlOut failed,",e);
        }
        return map;
    }
//
//
//    private static void smsAmountWarn() {
//        Sms sms = new Sms();
//        sms.setContent("短信余量告警，请及时充值");
//        sms.setPhone("13758080693");
//        sender.send(sms);
//    }


/*    public static void main(String[] args) {

       *//* String rsultStr = "<?xml version=\"1.0\" encoding=\"utf-8\" ?>\n" +
                "<returnsms>\n" +
                "    <returnstatus>Success</returnstatus>\n" +
                "    <message>ok</message>\n" +
                "    <remainpoint>333327</remainpoint>\n" +
                "    <taskID>56022</taskID>\n" +
                "    <successCounts>1</successCounts>\n" +
                "</returnsms>";*//*
        //readStringXmlOut(rsultStr);
        initDefaultConfig();
        Sms sms = new Sms();
        sms.setContent("你有策略即将到期卖出，如需延期请在14:40前去往我的策略申请延期\n");
        sms.setPhone("18768194757");
        //13735475367
//        System.out.println(replaceContent("你的策略【恒信东方(300081)】自动延期失败(资金不足)，请进行手动策略延期"));
              sender.send(sms);

    }*/

}
