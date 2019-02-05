package com.ccspace.facade.domain.common.threadlocals;

/**
 * @AUTHOR CF
 * @DATE Created on 2018/3/23 16:27.
 */
public class DemoRedisLockSign extends BaseLockSign{


    private static ThreadLocal<Boolean> lockSign = new ThreadLocal<Boolean>() {
        @Override
        protected Boolean initialValue() {
            return false;
        }
    };

    @Override
    public  boolean getLockSign() {
//        System.out.println("lockSign="+lockSign);
        return lockSign.get();
    }

    @Override
    public String getLockLocalName() {
        return "demoLock";
    }

    @Override
    public  void setLockSign(boolean sign) {
        lockSign.set(sign);
    }

    @Override
    public  void clear() {
        lockSign.remove();
    }

}
