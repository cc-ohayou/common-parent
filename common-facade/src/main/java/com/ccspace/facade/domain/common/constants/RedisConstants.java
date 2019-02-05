package com.ccspace.facade.domain.common.constants;

/**
 * @AUTHOR CF
 * @DATE Created on 2017/6/22 16:00.
 */
public class RedisConstants {


    /**
     * redis 全局配置
     */
    public static final int REDIS_RETRY_COUNT = 10;
    //可用连接实例的最大数目，默认值为8；
    //如果赋值为-1，则表示不限制；如果pool已经分配了maxActive个jedis实例，则此时pool的状态为exhausted(耗尽)。
    public final static int MAX_ACTIVE = 1024;
    //控制一个pool最多有多少个状态为idle(空闲的)的jedis实例，默认值也是8。
    public final static int MAX_IDLE = 200;
    //等待可用连接的最大时间，单位毫秒，默认值为-1，表示永不超时。如果超过等待时间，则直接抛出JedisConnectionException；
    public final static int MAX_WAIT = 10000;
    public final static int TIMEOUT = 10000;
    //逐出连接的最小空闲时间 默认1800000毫秒(30分钟)
    public final static int MIN_IDLE_TIME = 60 * 1000 * 5;//5分钟释放
    //在borrow一个jedis实例时，是否提前进行validate操作；如果为true，则得到的jedis实例均是可用的；
    public final static boolean TEST_ON_BORROW = true;

    //分布式锁开关 默认为空不打开
    public static final String REDIS_LOCK_SWITCH = "etf-str-redis-lock-switch";

    //登陆拦截路径
    public static final String LOGIN_AUTH = "etf_login_auth";

    /**
     * 拦截校验相关配置redis key和常量
     */
    public static final String RATE_LIMIT_SCRIPT = "if redis.call('exists',KEYS[1])==1 and tonumber(redis.call('get',KEYS[1]))>tonumber(ARGV[2]) then return 0 end local times = redis.call('incr',KEYS[1]) if times == 1 then redis.call('expire',KEYS[1],ARGV[1])   end if times>tonumber(ARGV[2]) then return 0 end return 1";


    public static final String IP = "ETF-STR-IP-";
    public static final String IP_MINUTE = "-MINUTE";
    public static final String IP_HOUR = "-HOUR";
    public static final String IP_DAY = "-DAY";
    public static final String IP_MONTH = "-MONTH";
    //UA信息
    public static final String UA = "ETF-STR-UA-";
    public static final String UA_MINUTE = "-MINUTE";
    public static final String UA_HOUR = "-HOUR";
    public static final String UA_DAY = "-DAY";
    public static final String UA_MONTH = "-MONTH";


    public static final String ONE_MINUTE ="60" ;
    public static final String ONE_MINUTE_LIMIT_COU ="5" ;
    public static final String ONE_HOUR ="3600" ;
    public static final String ONE_HOUR_LIMIT_COU = "50";
    public static final String ONE_DAY = "86400";
    public static final String ONE_DAY_LIMIT_COU ="100" ;
    public static final String ONE_MONTH = "2678400";
    public static final String ONE_MONTH_LIMIT_COU ="500" ;
    //频率限制校验的开关
    public static final String IP_SWITCH = "etf-str-ip-ua-limit-switch";

    public static final String COU_LIMIT_INFO = "ETF-HASH-IP-COU-LIMIT-CONFIG";
    public static final String COU_LIMIT_INFO_MIN = "min-cou";
    public static final String COU_LIMIT_INFO_HOUR = "hour-cou";
    public static final String COU_LIMIT_INFO_DAY = "day-cou";
    public static final String COU_LIMIT_INFO_MONTH = "month-cou";

    public static final String RATE_LIMIT_WHITE_IP = "etf-set-whiteips";
    public static final String RATE_LIMIT_BLACK_IP = "etf-set-blackips";
    public static final String IP_UA_LIMIT_URLS = "etf-str-ip-ua-limit-urls";

    public static final String KEY_BIZ_TYPE = "TOOL_HASH_KEY_BIZ_TYPE";


    /**
     * 合约到期强平时间节点
     */
    public static final String CONTRACT_UNWIND_TIME = "contract-unwind-time";
    //上次查询时的推送时间
    public static final String TEST_SIGN_SWITCH ="etf-str-test-sign" ;

    public static final int SEND_MESSAGE_EXPIRE_TIME = 60 * 60;

    //redis lock用常量
    //锁允许重试模式
    public static final String LOCK_MODEL_ALLOW_RETRY = "allowRetry";

    //认证支付相关
    public static final String LL_WAP_PAY_TMP = "etf-str-llWapPay-tmp";

    public static final String WEB_PAY_URL = "ETF_STR_WEB_PAY_URL";

    public static final String BG_SPECIAL_URLS = "bg-set-unintercept-urls";
    //结算定时的标识
    public static final String ETF_SETTLE_TASK_SIGN = "ETF_SETTLE_TASK_SIGN";
    //结算队列堆积报警 不设置默认报警
    public static final String ETF_STR_SEND_DINGMSG_SIGN = "etf-str-send-dingmsg-sign";
    //是否同时清除queue的sign  不设置默认清除
    public static final String ETF_STR_SETTLE_CLEAR_QUEUE = "etf-str-settle-clear-queue";
    //结算队列报警界限 默认200
    public static final String ETF_STR_SETTLE_QUEUE_LIMIT = "etf-str-settle-queue-limit";
    //默认为空不开启 设置则开启
    public static final String tradeSettleBlackLimitSign = "etf-str-tradeSettleBlackLimitSign";
    //频率限制校验开关标识
    public static final String SETTLE_RATE_LIMIT_SIGN = "SETTLE_RATE_LIMIT_SIGN";
    //结算获取结算信息模式 设置则取本地的
    public static final String GET_DEAL_INFO_LOCAL_SIGN = "ETF_HASH_GET_DEAL_INFO_LOCAL_SIGN";
    //结算延时策略钉钉报警level配置 配置每个level延时多久
    public static final String TASK_SETTLE_DELAY_TIME_LEVELS = "task-hash-settle-delay-time-levels";
    //后台导出模式开关
    public static final String EXPORT_MODEL_SWITCH = "PS_BG_EXPORT_MODEL_SWITCH";
}
