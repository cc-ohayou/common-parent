package com.ccspace.manager.mo;


import com.ccspace.facade.domain.common.threadlocals.BaseLockSign;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPubSub;
import redis.clients.jedis.ScanParams;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 避免将底层的redis ip 等全局配置暴露给外界
 * 给外界提供 统一的、抽象的  kv查询服务、排序服务器、队列服务、set服务等
 * <p>
 * Create By GarryKing on 2017/9/17 上午10:03
 */
public interface RedisManager {

    /**
     * 获取所有元素
     *
     * @param key
     */
    Map<String, String> hgetAll(String key);

    List hscan(String key, ScanParams scanParams, Jedis jedis);

    /**
     * @param key
     * @param field
     * @return
     */
    String hget(String key, String field);

    /**
     * @param key
     * @param fields
     * @return
     */
    List<String> hmget(String key, String... fields);

    /**
     * 设置hash类型的key 已存在则会覆盖
     *
     * @param key
     * @param field
     * @param value
     * @return
     */
    long hset(String key, String field, String value);

    /**
     * 第一次设置hash类型的key某个field时可以设置超时时间  后面再设置field调用非超时的即可
     *
     * @param key
     * @param field
     * @param value
     * @param expireTime
     * @return
     */
    long hset(String key, String field, String value, int expireTime);

    /**
     * 批量设置hash key的多个field的值
     *
     * @param key
     * @param fieldValueMap
     */
    void hmset(String key, Map<String, String> fieldValueMap);

    /**
     * 设置简单的String类型的key 已存在则会覆盖
     *
     * @param key
     * @param value
     */
    void set(String key, String value);

    /**
     * @description 获取key的值
     * @author CF create on 2017/9/20 15:07
     */
    String get(String key);

    /**
     * @description 设置key值同时设置过期时间 单位s
     * @author CF create on 2017/9/20 15:27
     */
    void set(String key, String value, int expireTime);

    /**
     * @description 根据需求自行对任何key设置过期时间
     * @author CF create on 2017/9/20 15:29
     */
    long expire(String key, int expireTime);

    /**
     * @description 删除key
     * @author CF create on 2017/9/20 15:30
     */
    long del(String key);
    long del(String... keys);

    long hdel(String key, String field);

    /**
     * @description 存在设置值并返回1 不存在返回
     * @author CF create on 2017/10/25 9:55
     */
    long hsetnx(String key, String field, String value);

    /**
     * 存在则返回1，不存在返回0
     * @param key
     * @param value
     * @return
     */
    long setnx(String key, String value);

    /**
     * @description expireTime大于0 则设置超时时间 单位s
     * @author CF create on 2018/3/10 14:03
     */
    void incr(String tradeKey, int expireTime);

    /**
     * @description
     * @author CF create on 2018/3/10 18:25
     */
    void incr(String tradeKey);

    void zadd(String errorNeedHandleTradeIdSetKey, long l, String value);

    boolean reachRateLimit(String key, String s, String s1);

    /**
     * @description
     * 借助redis完成全局的分布式锁
     * 业务执行完后注意unlock 否则会持有该锁60s 默认值
     * 其他线程才有机会再次获取
     * 限制：单机模式
     * 集群下使用redession来实现 超高并发 集群不同步的情况下锁不住
     * 也可借助zookeeper来实现分布式锁
     *
     * @author CF create on 2018/3/21 9:40
     */
    boolean  lock(String key, String lockModel, BaseLockSign lockThreadLocal) throws Exception;

    /**
     * @description 自定义传入超时时间
     * @author CF create on 2018/11/29 14:46
     */
    boolean lock(String lockKey, String lockModel, BaseLockSign lockThreadLocal, int timeOut);

    /**
     * @description 使用完释放锁
     * @author CF create on 2018/3/21 9:41
     */
    void  unLock(String key, BaseLockSign lockThreadLocal);

    /**
     * @description 获取集合
     * @author CF create on 2018/3/23 11:21
     */
    Set<String> smembers(String key);

    /**
     * @description 根据ip ua信息限流特定url
     * @author CF create on 2018/3/26 15:51
     */
    boolean ipUaInfoReachLimit(String uaInfo, String realIp, String url);

    void sadd(String phoenixStopStockCodes, String stockCode);

    /**
     * @description 在某个频道发布消息
     * @author CF create on 2018/3/15 10:08
     */
    Long publish(String channel, String message);

    /**
     * @description 使用具体的jedisPubSub订阅某个频道的消息 或者多个频道的 具体频道
     * @author CF create on 2018/3/15 10:08
     */
    void subscribe(JedisPubSub jedisPubSub, String describe, String... channels);

    /**
     * @description 订阅某种规则的频道消息
     * @author CF create on 2018/3/15 10:10
     */
    void psubscribe(JedisPubSub jedisPubSub, String describe, String... patterns);

    Double zscore(String blackTradeSet, String tradeId);

    /**
     * @description 判断key是否存在
     * @author CF create on 2018/9/6 15:40
     */
    boolean exists(Jedis jedis, String s);

    /**
     * 判断成员元素是否是集合的成员
     * @param key
     * @param member
     * @return
     */
    boolean sismember(String key, String member);

    /**
     * zset移除元素
     * @param key
     * @param member
     */
    void zrem(String key, String member);


    boolean hexists(String phoenixInsufficientDepositPush, String strategyId, Jedis jedis);

    /**
     * 查询指定范围元素
     * @param key
     * @param min
     * @param max
     * @return
     */
    Set<String> zrangebyscore(String key, Double min, Double max);

    Set<String> zrevrangebyscore(String key, Double min, Double max);

    Set<String> zrange(String key, Integer start, Integer end);

    Set<String> zrevrange(String key, Integer start, Integer end);

}
