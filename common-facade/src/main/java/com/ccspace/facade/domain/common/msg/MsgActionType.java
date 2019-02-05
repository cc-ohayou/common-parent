package com.ccspace.facade.domain.common.msg;

import com.alibaba.fastjson.JSON;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @AUTHOR CF
 * @DATE Created on 2018/5/24 13:52.
 */
public enum MsgActionType {

    PERSON_SUB_PERSON("p-sub-person", "人员订阅他人",SubRefType.PERSON,MsgQueryPath.NEW_SUB),
    P_SUB_POST("p-sub-post", "人员关注帖子",SubRefType.PERSON,MsgQueryPath.SUB_DYNAMIC),
    P_PUB_POST("p-pub-post", "人员发表帖子",SubRefType.PERSON,
            MsgQueryPath.SUB_DYNAMIC,CustomType.PULL_TYPE),
    PERSON_COMMENT_COMMENT("p-comt-comt", "人员回复评论",SubRefType.PERSON,MsgQueryPath.COMMENT),
    PERSON_COMMENT_POST("p-comt-post", "人员评论帖子",SubRefType.PERSON,MsgQueryPath.COMMENT),
    PERSON_THUMB_POST("p-thumb-post", "人员点赞帖子",SubRefType.PERSON,MsgQueryPath.THUMB),
    PERSON_THUMB_COMMENT("p-thumb-comt", "人员点赞评论",SubRefType.PERSON,MsgQueryPath.THUMB),
    PERSON_PUB_REAL_STRATEGY("p-pub-real-strgy", "人员发表实盘策略",SubRefType.PERSON,MsgQueryPath.SUB_DYNAMIC),
    PERSON_REAL_STRATEGY_SETTLE("p-real-strgy-settle", "人员实盘策略结算",SubRefType.PERSON,MsgQueryPath.SUB_DYNAMIC),
    PERSON_FOLLOW_STRATEGY_SETTLE("p-follow-strgy-settle", "人员实盘跟买策略结算",SubRefType.PERSON,MsgQueryPath.SUB_DYNAMIC),
    PERSON_REAL_STRATEGY_SOLD("p-real-strgy-sold", "人员卖出实盘策略",SubRefType.PERSON,MsgQueryPath.SUB_DYNAMIC),
    PERSON_SIMU_STRATEGY_SETTLE("p-simu-strgy-settle", "人员模拟策略结算",SubRefType.PERSON,MsgQueryPath.SUB_DYNAMIC),
    PERSON_SIMU_STRATEGY_SOLD("p-simu-strgy-sold", "人员卖出模拟策略",SubRefType.PERSON,
            MsgQueryPath.SUB_DYNAMIC,CustomType.PULL_TYPE),
    PERSON_PUB_SIMU_STRATEGY("p-pub-simu-strgy", "人员发表模拟策略",SubRefType.PERSON,
            MsgQueryPath.SUB_DYNAMIC,CustomType.PULL_TYPE),
    //暂未启用
    POST_COMMENT("post-comt", "帖子被评论",SubRefType.POST,MsgQueryPath.COMMENT),
    POST_THUMB("post-thumb", "帖子被点赞",SubRefType.POST,MsgQueryPath.THUMB),
    POST_SUB("post-sub", "帖子被关注",SubRefType.POST,MsgQueryPath.NEW_SUB),

    SYS_STRATEGY_DEFER("strategy-defer","策略延期",SubRefType.SYS,MsgQueryPath.STRATEGY_DYNAMIC),
    SYS_STRATEGY_STOP_LOSS("strategy-stop-loss","策略止损",SubRefType.SYS,MsgQueryPath.STRATEGY_DYNAMIC),
    SYS_STRATEGY_DROP("strategy-drop","股票跌幅波动",SubRefType.SYS,MsgQueryPath.STRATEGY_DYNAMIC),
    SYS_STRATEGY_ABANDON("strategy-abandon","策略放弃",SubRefType.SYS,MsgQueryPath.STRATEGY_DYNAMIC),
    SYS_STRATEGY_DEPOSIT_INSUFFICIENT("strategy-deposit-insufficient","策略信用金不足",SubRefType.SYS,MsgQueryPath.STRATEGY_DYNAMIC),
    SYS_STRATEGY_EXCLUDE("sys-strategy-exclude", "除权除息",SubRefType.SYS,MsgQueryPath.STRATEGY_DYNAMIC),
    SYS_STRATEGY_BUY_OUT("sys-strategy-buy-out", "买断",SubRefType.SYS,MsgQueryPath.STRATEGY_DYNAMIC),
    SYS_STRATEGY_ALLOT("sys-strategy-allot", "除权送股",SubRefType.SYS,MsgQueryPath.STRATEGY_DYNAMIC),
    SYS_STRATEGY_RAISE_LIMIT("sys-strategy-raise-limit","涨停修改止盈",SubRefType.SYS,MsgQueryPath.STRATEGY_DYNAMIC),
    SYS_NOTICE("sys-notice", "系统公告",null,null),
    SYS_BALANCEREMING("sys-balance-remind", "系统余额提醒",SubRefType.SYS,MsgQueryPath.SYS_MSG),
    SYS_PRIZE("sys-prize","系统中奖提醒",SubRefType.SYS,MsgQueryPath.SYS_MSG),
    SYS_VOUCHER_UNUSED("sys-voucher-unused","抵用金使用提醒",SubRefType.SYS,MsgQueryPath.SYS_MSG),
    SYS_CANCEL_ORDER("sys-cancel-order","系统撤单提醒",SubRefType.SYS,MsgQueryPath.STRATEGY_DYNAMIC),
    SYS_CANCEL_ORDER_MANNUAL("sys-cancel-order-mannual","风控撤单提醒",SubRefType.SYS,MsgQueryPath.STRATEGY_DYNAMIC),
    SYS_ENTRUST_ORDER_SETTLE("sys-entrust-order-settle","触发价成交提醒",SubRefType.SYS,MsgQueryPath.STRATEGY_DYNAMIC),
    SYS_CANCEL_BUY_ORDER("sys-cancel-buy-order","买入撤单",SubRefType.SYS,MsgQueryPath.STRATEGY_DYNAMIC),
    SYS_CANCEL_SELL_ORDER("sys-cancel-sell-order","卖出撤单",SubRefType.SYS,MsgQueryPath.STRATEGY_DYNAMIC),
    SYS_DATE_ARRIVED_FORCE_SELL("sys-date-arrived-force-sell","合约到期强平",SubRefType.SYS,MsgQueryPath.STRATEGY_DYNAMIC),
    SYS_DATE_ARRIVED_REMIND("sys-date-arrived-remind","合约到期提醒",SubRefType.SYS,MsgQueryPath.STRATEGY_DYNAMIC),

    SYS_BALANCE_NEGATIVE_REMIND("sys-balance-negative-remind","余额负提醒",SubRefType.SYS,MsgQueryPath.SYS_MSG),
    SYS_BALANCE_ADD_REMIND("sys_balance_add_remind","负余额抵扣提醒",SubRefType.SYS,MsgQueryPath.SYS_MSG),
    ;

    private String value;
    private String label;
    //subRefType主要根据谁是主体指定  主体是人的动作关联person类型
    // 主体是帖子的关联类型为post 主体是sys发起的则为sys
    private String subRefType;
    //拉取类型 有些消息需要拉取的 构造时加上此属性 系统初始化时会自动加载配置到本地缓存
    private String pullActionType;
    //消息列表类型归属
    private String msgListTypeBelong;



    //订阅动态涉及到的消息类型
    private static List<String> subDynamicActionTypes=new ArrayList<>(4);
    //点赞涉及到的消息类型
    private static List<String> thumbActionTypes=new ArrayList<>(4);
    //策略动态涉及到的消息类型
    private static List<String> strgyDynamicTypes=new ArrayList<>(4);
    //系统消息涉及到的消息类型
    private static List<String> sysMsgActionTypes=new ArrayList<>(8);
    //评论涉及到的消息类型
    private static List<String> commentActionTypes=new ArrayList<>(4);
    //新的关注（订阅）涉及到的消息类型
    private static List<String> newSubActionTypes=new ArrayList<>(4);

    public static Map<String,List<String>> msgTypes=new HashMap<>(8);



    static List<String> sysSubRefActionTypes=new ArrayList<>(4);
    static List<String> personSubRefActionTypes=new ArrayList<>(4);
    static List<String> postSubRefActionTypes=new ArrayList<>(4);
    static List<String> pullActionTypes=new ArrayList<>(4);

    public static PersonalSubConfig personSubConf= new PersonalSubConfig();




    /**
     * @description  初始化订阅消息配置 消息中心列表配置
     * @author CF create on 2018/11/6 15:31
     */
    public  static void initSubConfigAndMsgTypeListConfig() {


        initMsgCenterListTypeSetting();

        msgTypes.put(MsgQueryPath.NEW_SUB.getValue(),newSubActionTypes);
        msgTypes.put(MsgQueryPath.THUMB.getValue(),thumbActionTypes);
        msgTypes.put(MsgQueryPath.COMMENT.getValue(),commentActionTypes);
        msgTypes.put(MsgQueryPath.STRATEGY_DYNAMIC.getValue(),strgyDynamicTypes);
        msgTypes.put(MsgQueryPath.SUB_DYNAMIC.getValue(),subDynamicActionTypes);
        msgTypes.put(MsgQueryPath.SYS_MSG.getValue(),sysMsgActionTypes);

        initSubRefConfig();


    }


    /**
     * @description 初始化订阅消息配置
     * @author CF create on 2018/11/6 15:30
     */
    private static void initSubRefConfig() {

        Map<String,ChildSubConfig> confMap=new HashMap<>(4);

        ChildSubConfig pullConf=new ChildSubConfig();
        pullConf.setTargetType(CustomType.PULL_TYPE.getValue());
        pullConf.setActionTypes(pullActionTypes);
        confMap.put(CustomType.PULL_TYPE.getValue(),pullConf);
        //拉取消息类型配置组装
        putConfIntoMap(confMap,pullActionTypes,CustomType.PULL_TYPE.getValue());
        //帖子关联订阅消息类型配置组装
        putConfIntoMap(confMap,postSubRefActionTypes,SubRefType.POST.getValue());
        //人员关联订阅消息类型配置组装
        putConfIntoMap(confMap,personSubRefActionTypes,SubRefType.PERSON.getValue());
        //系统关联订阅消息类型配置组装
        putConfIntoMap(confMap,sysSubRefActionTypes,SubRefType.SYS.getValue());


        personSubConf.setSubConfigs(confMap);
        //默认接收所有可接收的消息
        personSubConf.setGlobalMsgSwitch(GlobalMsgReceiveType.ALL.getValue());
        //默认120秒拉取一次  即请求服务端一次拉取消息
        personSubConf.setMsgPullPeriod(120);
        //拉取最大时间间隔 天数 默认最多拉取两天前的消息
        personSubConf.setPullTimeDayLimit(2);

    }

    private static void putConfIntoMap(Map<String, ChildSubConfig> confMap, List<String> actionTypes, String targetType) {
        ChildSubConfig config=new ChildSubConfig();
        config.setTargetType(targetType);
        config.setActionTypes(actionTypes);
        confMap.put(targetType,config);
    }

    /**
     * @description 初始化消息中心类型分配设置
     * @author CF create on 2018/11/6 15:30
     */
    private static void initMsgCenterListTypeSetting() {
        for (MsgActionType actionType : MsgActionType.values()) {
            if (MsgQueryPath.NEW_SUB.getValue().equals(actionType.getMsgListTypeBelong())) {
                newSubActionTypes.add(actionType.getValue());
            } else if (MsgQueryPath.THUMB.getValue().equals(actionType.getMsgListTypeBelong())) {
                thumbActionTypes.add(actionType.getValue());
            } else if (MsgQueryPath.COMMENT.getValue().equals(actionType.getMsgListTypeBelong())) {
                commentActionTypes.add(actionType.getValue());
            } else if (MsgQueryPath.STRATEGY_DYNAMIC.getValue().equals(actionType.getMsgListTypeBelong())) {
                strgyDynamicTypes.add(actionType.getValue());
            } else if (MsgQueryPath.SUB_DYNAMIC.getValue().equals(actionType.getMsgListTypeBelong())) {
                subDynamicActionTypes.add(actionType.getValue());
            } else if (MsgQueryPath.SYS_MSG.getValue().equals(actionType.getMsgListTypeBelong())) {
                sysMsgActionTypes.add(actionType.getValue());
            }
            //初始化订阅配置
            if(SubRefType.PERSON.getValue().equals(actionType.getSubRefType())){
                personSubRefActionTypes.add(actionType.getValue());
            }else if(SubRefType.POST.getValue().equals(actionType.getSubRefType())){
                postSubRefActionTypes.add(actionType.getValue());
            }else if(SubRefType.SYS.getValue().equals(actionType.getSubRefType())){
                sysSubRefActionTypes.add(actionType.getValue());
            }
            //初始化拉取消息类型集合
            if(CustomType.PULL_TYPE.getValue().equals(actionType.getPullActionType())){
                pullActionTypes.add(actionType.getValue());
            }



        }


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


    MsgActionType(String value, String label, SubRefType subRefType, MsgQueryPath msgListTypeBelong) {
        this.value = value;
        this.label = label;
        if(subRefType==null){
            this.subRefType ="";
        }else{
            this.subRefType = subRefType.getValue();
        }
        if (msgListTypeBelong == null) {
            this.msgListTypeBelong="";
        }else{
            this.msgListTypeBelong = msgListTypeBelong.getValue();
        }
    }

    MsgActionType(String value, String label, SubRefType subRefType, MsgQueryPath msgListTypeBelong, CustomType pullActionType) {
        this.value = value;
        this.label = label;
        if(subRefType==null){
            this.subRefType ="";
        }else{
            this.subRefType = subRefType.getValue();
        }
        if (msgListTypeBelong == null) {
            this.msgListTypeBelong="";
        }else{
            this.msgListTypeBelong = msgListTypeBelong.getValue();
        }
        if(pullActionType==null){
            this.pullActionType ="";
        }else{
            this.pullActionType = pullActionType.getValue();
        }


    }


    public String getMsgListTypeBelong() {
        return msgListTypeBelong;
    }

    public void setMsgListTypeBelong(String msgListTypeBelong) {
        this.msgListTypeBelong = msgListTypeBelong;
    }

    public String getPullActionType() {
        return pullActionType;
    }

    public void setPullActionType(String pullActionType) {
        this.pullActionType = pullActionType;
    }



    public String getValue() {
        return this.value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getSubRefType() {
        return subRefType;
    }

    public void setSubRefType(String subRefType) {
        this.subRefType = subRefType;
    }

    public static void main(String[] args) {




        System.out.println(JSON.toJSONString(msgTypes));

        String str="{\"sysNotice\":[\"sys-balance-remind\",\"sys-voucher-unused\",\"sys-prize\"],\"comment\":[\"p-comt-post\"],\"subDynamic\":[\"p-pub-post\",\"p-pub-simu-strgy\",\"p-simu-strgy-sold\",\"p-follow-strgy-settle\"],\"newSub\":[\"p-sub-person\"],\"strategyDynamic\":[\"strategy-defer\",\"strategy-stop-loss\",\"strategy-drop\",\"strategy-abandon\",\"strategy-deposit-insufficient\",\"sys-strategy-exclude\",\"sys-strategy-allot\"],\"like\":[\"p-thumb-comt\",\"p-thumb-post\"]}";
    }

}
