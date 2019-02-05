package com.ccspace.facade.domain.common.msg;

import lombok.Data;
import lombok.experimental.Accessors;

/**消息中心公用的消息对象
 * @AUTHOR CF
 * @DATE Created on 2018/5/28 10:55.
 */
@Data
@Accessors(chain=true)
public class BaseNotify {
    private String title;
    private String nickName;
    //post、comment、like、sys等等  对应action-type
    private String type;
    private String content;
    //1 已读、0 未读 标示对应数据库is_read字段
    private String readSign;
    //关联的父级内容
    private String parentContent;
    private String headImage;
    private String msgTime;
    private String id;
    //消息关联的 粉丝id、帖子id、评论id、策略id或交易id等等
    private String relationId;
    //关联对象是否还有效 允许查看
    private String targetAvailableSign;
    //帖子的发表状态
    private String postStatus;
    //评论的发表状态
    private String commentStatus;
    //消息发送人ID
    private String senderId;

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        BaseNotify that = (BaseNotify) o;
        return (relationId+type).equals(that.getRelationId()+that.getType());
    }



    @Override
    public int hashCode() {
        int result = type != null ? type.hashCode() : 0;
        result = 31 * result + (relationId != null ? relationId.hashCode() : 0);
        return result;
    }



}
