package com.ccspace.facade.domain.common.constants;

/**
 * @AUTHOR CF
 * @DATE Created on 2018/7/10 16:47.
 */
public class CommonConstants {
    /**
     * 个推推送相关配置
     */
    public static final String GETUI_host = "http://sdk.open.api.igexin.com/apiex.htm";
    public static final Integer READ_NOT =0 ;
    public static final int REGISTER_VERIFY_EXPIRE_TIME = 600;
    public static final String MERCHANT_ID = "merchantId";

    public static String GLOBAL_RESOURCES_DIR = "GLOBAL_RESOURCES_DIR";
    //系统用户
    public static final String USER_SYSTEM = "SYS";
    /**
     * 手机号码正则表达式
     */
    public static String PHONE_REG = "^1[0-9]{10}";
    /**
     * 密码正则
     */
    public static String PASS_REG = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{6,16}$";
    public static final String AVAILABLE = "1";

    public static final String CLIENT_TYPE_PC = "PC";
    public static final String CLIENT_TYPE_APP = "app";


    public static final String PROJECT_PREFIX = "common-";

    public static final int LOGIN_VERIFY_EXPIRE_TIME = 60 * 30;

    //手动排序设置
    public static final String ASC = "ascend";
    public static final String DESC = "descend";

    //Redis_Limit值
    public static final Integer START = 0;
    public static final Integer END = -1;



    // Map - Key - Request
    public static final String KEY_REQUEST = "_request";
    // Map - Key - Request Raw
    public static final String KEY_REQUEST_RAW = "_request_raw";
    // Map - Key - Request header
    public static final String KEY_REQUEST_HEADER = "_request_header";
    // Map - Key - Request desensitize time
    public static final String KEY_REQUEST_DESENSITIZE_TIME = "_request_des_time";
    // Map - Key - Response
    public static final String KEY_RESPONSE = "_response";
    // Map - Key - Response Raw
    public static final String KEY_RESPONSE_RAW = "_response_raw";
    // Map - Key - Response desensitize time
    public static final String KEY_RESPONSE_DESENSITIZE_TIME = "_response_des_time";
    // Map - Key - List<Map<String, Object>>
    public static final String KEY_LIST_MAP = "_list_map";
    // Map - Key - API cost time
    public static final String KEY_API_COST_TIME = "_api_cost_time";
    // Map - Key - API
    public static final String KEY_API = "_api";
    // Map - Key - Method
    public static final String KEY_METHOD = "_method";


    /**
     * 路径分隔符
     */
    public static final String SPT = "/";
    /**
     * 索引页
     */
    public static final String INDEX = "index";
    /**
     * 默认模板
     */
    public static final String DEFAULT = "default";
    /**
     * UTF-8编码
     */
    public static final String UTF8 = "UTF-8";
    /**
     * 提示信息
     */
    public static final String MESSAGE = "message";
    /**
     * cookie中的JSESSIONID名称
     */
    public static final String JSESSION_COOKIE = "JSESSIONID";
    /**
     * url中的jsessionid名称
     */
    public static final String JSESSION_URL = "jsessionid";
    /**
     * HTTP POST请求
     */
    public static final String POST = "POST";
    /**
     * HTTP GET请求
     */
    public static final String GET = "GET";

    /**
     * 全文检索索引路径
     */
    public static final String LUCENE_PATH = "/WEB-INF/lucene";
    public static final String X_AUTH_MODE = "client_auth";
    public static final String UPLOAD_MODE = "pic";



   }
