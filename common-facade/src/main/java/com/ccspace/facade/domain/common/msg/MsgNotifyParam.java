package com.ccspace.facade.domain.common.msg;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * @AUTHOR CF
 * @DATE Created on 2018/5/24 13:31.
 */
@Data
@Accessors(chain=true)
public class MsgNotifyParam {
    //消息的标题 推送提示消息显示用
    private String msgTitle;
    //消息内容
    private String content;
    //消息类型 announce 公告 remind 提醒 message 私信
    private String msgType;
    //消息的动作类型归属  譬如  p-pub-post   人员发表忒自   p-comt-post 人员评论帖子
    private String actionType;
    //关联的动态id 或者人员id
    private String relationId;
    //插入user_notify记录时的uid
    private String targetUid;
    //动作发起人uid
    private String senderId;
    private String merchantId;
    //消息产生时间
    private Date msgTime;
    private String msgId;
    private String notifyId;

    //推送消息相关
    //消息的标题
    private String pushMsgTitle;
    //消息内容
    private String pushMsgContent;
    private String pushMsgType;


}
