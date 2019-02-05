package com.ccspace.facade.domain.common.enums.singleton;


import com.ccspace.facade.domain.common.threadlocals.BaseLockSign;
import com.ccspace.facade.domain.common.threadlocals.DemoRedisLockSign;

/**
 * @AUTHOR CF 获取对应threadLocal的单例对象
 * @DATE Created on 2018/4/4 11:30.
 */
public enum SingletonLocal {

    INSTANCE;
    private DemoRedisLockSign demoLocal;

    SingletonLocal() {
        demoLocal = new DemoRedisLockSign();
    }


    /**
     * @description
     * @author CF create on 2018/11/16 11:00
     */
    public BaseLockSign getDemoLocal() {
        return demoLocal;
    }


}
