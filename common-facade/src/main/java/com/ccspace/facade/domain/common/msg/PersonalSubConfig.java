package com.ccspace.facade.domain.common.msg;

import com.alibaba.fastjson.JSON;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 人员订阅设置对象  对应sub_config表的value  json字符串对象
 * @AUTHOR CF
 * @DATE Created on 2018/5/24 9:57.
 */
@Data
@Accessors(chain = true)
public class PersonalSubConfig {


    // 全局消息开关设置 关闭不接收任何消息  默认开启
    // 开启分两种模式 接收关注人的 onlySub 所有人的 all 不开启设置为off 什么消息都不接收
    // 关联枚举类 GlobalMsgReceiveType
    private String globalMsgSwitch;
    //拉取更新消息的频率  参考新浪微博 0.5分钟 2分钟 五分钟 几个选项
    // 数据库存储为30s  120s 300s  默认两分钟 120
    private int msgPullPeriod;
    //拉取消息最大的天数  默认2  即如果距离上次拉取通知的时间大于2天
    // 取两天前的时间作为此次拉取的参考时间
    //   可支持用户配置化 譬如最大消息拉取范围 2~7天
    private int pullTimeDayLimit;
    //具体的订阅动作集合   key值对应枚举类  SubRefType 的value
    // 举例 post  person pullActionTypes sys 直接推送消息或者拉取消息时 根据自己的订阅设置决定接不接收对应的消息
    //编码时即时推送的消息 注意推送时该消息类型的SubRefType归属 譬如
    private Map<String,ChildSubConfig> subConfigs;


    public static void main(String[] args) {
        PersonalSubConfig demo= new PersonalSubConfig();

        Map<String,ChildSubConfig> confMap=new HashMap<>(4);
        setSysConf(confMap);
        setPostConf(confMap);
        setPullActionTypes(confMap);
        setPersonActionTypes(confMap);
        demo.setSubConfigs(confMap);
        demo.setGlobalMsgSwitch(GlobalMsgReceiveType.ALL.getValue());
        demo.setMsgPullPeriod(120);
        demo.setPullTimeDayLimit(2);
        //此demo即为每个商户人员默认的通知订阅设置
        // 人员不设置订阅设置的情况 sub_config表不存在人员订阅设置数据就取这里的默认值
        System.out.println(JSON.toJSONString(demo));
        System.out.println(new ArrayList<>(0));
    }

    private static void setSysConf(Map<String, ChildSubConfig> confMap) {
        ChildSubConfig sysConf=new ChildSubConfig();
        sysConf.setTargetType(SubRefType.SYS.getValue());
        List<String> sysActionTypes=new ArrayList<>(4);
        sysActionTypes.add(MsgActionType.SYS_STRATEGY_DEFER.getValue());
        sysActionTypes.add(MsgActionType.SYS_STRATEGY_STOP_LOSS.getValue());
        sysActionTypes.add(MsgActionType.SYS_BALANCEREMING.getValue());
        sysActionTypes.add(MsgActionType.SYS_STRATEGY_ABANDON.getValue());
        sysActionTypes.add(MsgActionType.SYS_STRATEGY_DROP.getValue());
        sysConf.setActionTypes(sysActionTypes);
        confMap.put(SubRefType.SYS.getValue(),sysConf);
    }

    private static void setPostConf(Map<String, ChildSubConfig> confMap) {
        ChildSubConfig postConf = setPostSubActionTypes();
        confMap.put(SubRefType.POST.getValue(),postConf);
    }

    private static ChildSubConfig setPostSubActionTypes() {
        ChildSubConfig postConf=new ChildSubConfig();
        postConf.setTargetType(SubRefType.POST.getValue());
        List<String> postActionTypes=new ArrayList<>(4);
        postActionTypes.add(MsgActionType.POST_COMMENT.getValue());
        postActionTypes.add(MsgActionType.POST_THUMB.getValue());
        postConf.setActionTypes(postActionTypes);
        return postConf;
    }

    private static void setPersonActionTypes(Map<String, ChildSubConfig> confMap) {
        ChildSubConfig personConf=new ChildSubConfig();
        List<String> pActionTypes=new ArrayList<>(4);
        // 人员评论帖子
        pActionTypes.add(MsgActionType.PERSON_COMMENT_POST.getValue());
        // 评论别人的评论
        pActionTypes.add(MsgActionType.PERSON_COMMENT_COMMENT.getValue());
        // 人员发帖 配合订阅关系表 subscription   target_type为person的
        // 可以实现只接收关注的人的帖子动态
        pActionTypes.add(MsgActionType.P_PUB_POST.getValue());
        // 人员发布了模拟策略
        pActionTypes.add(MsgActionType.PERSON_PUB_SIMU_STRATEGY.getValue());
        pActionTypes.add(MsgActionType.PERSON_SIMU_STRATEGY_SOLD.getValue());
        pActionTypes.add(MsgActionType.PERSON_SUB_PERSON.getValue());
        pActionTypes.add(MsgActionType.PERSON_FOLLOW_STRATEGY_SETTLE.getValue());
        pActionTypes.add(MsgActionType.PERSON_THUMB_COMMENT.getValue());
        pActionTypes.add(MsgActionType.PERSON_THUMB_POST.getValue());
        // 人员发布实盘策略 配合订阅关系表 subscription
        // 可以实现只接收关注的人的策略动态  好友策略动态
        // pActionTypes.add("p-pub-real-strgy");
        personConf.setTargetType(SubRefType.PERSON.getValue());
        personConf.setActionTypes(pActionTypes);
        confMap.put(SubRefType.PERSON.getValue(),personConf);
    }

    private static void setPullActionTypes(Map<String, ChildSubConfig> confMap) {
        //拉取的动态类型
        ChildSubConfig pullActionTypesConf=new ChildSubConfig();
        List<String> pullActionTypes=new ArrayList<>(4);

        pullActionTypes.add(MsgActionType.P_PUB_POST.getValue());
        // 人员发布了模拟策略
        pullActionTypes.add(MsgActionType.PERSON_PUB_SIMU_STRATEGY.getValue());
        pullActionTypes.add(MsgActionType.PERSON_SIMU_STRATEGY_SOLD.getValue());
        pullActionTypesConf.setTargetType(CustomType.PULL_TYPE.getValue());
        pullActionTypesConf.setActionTypes(pullActionTypes);
        confMap.put(CustomType.PULL_TYPE.getValue(),pullActionTypesConf);
    }


}
