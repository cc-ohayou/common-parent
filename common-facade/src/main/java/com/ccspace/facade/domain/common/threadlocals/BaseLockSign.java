package com.ccspace.facade.domain.common.threadlocals;

/**
 * @AUTHOR CF
 * @DATE Created on 2018/3/23 16:31.
 */
public  abstract  class BaseLockSign {

    /**
     * @description 获取实际本地变量类型名称
     * @author CF create on 2018/4/4 11:58
     */
    public  abstract String getLockLocalName();

    @Override
    public String toString(){
        return getLockLocalName()+ "@" + Integer.toHexString(hashCode());
    }
    /**
     * @description 设置本地线程对应key的redis分布式锁上锁标识
     * @author CF create on 2018/3/23 16:41
     */
    public  abstract  void setLockSign(boolean lockSign);


    /**
     * @description 获取本地线程对应key的redis分布式锁上锁标识
     * @author CF create on 2018/3/23 16:42
     */
    public  abstract boolean getLockSign();


    /**
     * @description
     * @author CF create on 2018/3/23 16:55
     */
    public  abstract void clear();


}
