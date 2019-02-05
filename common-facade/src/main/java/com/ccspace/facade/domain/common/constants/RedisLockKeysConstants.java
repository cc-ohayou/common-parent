package com.ccspace.facade.domain.common.constants;

/**
 * @author xianghy
 * @date Created on 2018/12/16
 */
public class RedisLockKeysConstants {

    public static final String PS_STR = "PS_STR";
    public static final String PS_STR_NEW = "ps-str";
    public static final String PS_HASH = "PS_HASH";
    public static final String PS_SET = "PS_SET";
    public static final String PS_LIST = "PS_LIST";
    public static final String BALANCE_LOCK = "_BALANCE_LOCK_";
    public static final String DEMO_LOCK = "_DEMO_LOCK_";
    public static final String SIMULATE_BALANCE_LOCK = "_SIMU_BALANCE_LOCK_";

    public static final String HANG_ORDER_LOCK = "-hang-order-lock-";

    // 用户余额key
    public static String balanceLockKey(String uid) {
        return PS_STR + BALANCE_LOCK  + uid;
    }
    //
    public static String demoLockKey(String uid) {
        return PS_STR + DEMO_LOCK   + uid;
    }

    public static String simulateBalanceLockKey(String uid){
        return PS_STR + SIMULATE_BALANCE_LOCK + uid;
    }

    /**
     * @description 为避免挂单、强平以及撤单的并发情况 获取策略的 全局分布式锁
     * @author CF create on 2018/11/16 10:53
     */
    public static String hangOrderLockKey(String strategyId){
        return PS_STR_NEW + HANG_ORDER_LOCK + strategyId;
    }

}
