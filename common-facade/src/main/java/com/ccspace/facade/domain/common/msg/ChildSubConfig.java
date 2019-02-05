package com.ccspace.facade.domain.common.msg;

import lombok.Data;

import java.util.List;

/**
 * @AUTHOR CF 个人消息订阅配置 某个子项  譬如说  系统消息订阅
 * @DATE Created on 2018/5/24 10:00.
 */
@Data
public class ChildSubConfig {
    /**
     * 订阅目标的类型  譬如 post  person  订阅的对象不同对应不同的动作组
     */
    private String targetType;
    private List<String> actionTypes;
}
