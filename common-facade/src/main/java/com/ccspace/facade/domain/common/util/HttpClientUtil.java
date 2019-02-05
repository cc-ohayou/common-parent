package com.ccspace.facade.domain.common.util;


import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import org.apache.commons.codec.binary.Base64;
import org.apache.http.*;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.config.ConnectionConfig;
import org.apache.http.config.SocketConfig;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.ssl.TrustStrategy;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.SSLContext;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.nio.charset.CodingErrorAction;
import java.security.GeneralSecurityException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * 功能说明:http协议控制器
 * <p> 系统版本: v1.0<br>
 * 开发人员: luopeng12856
 * 开发时间: 2016—05-26 上午10:12 <br>
 */
public class HttpClientUtil {
    private static final JsonNodeFactory factory = new JsonNodeFactory(false);

    private static Logger log = LoggerFactory.getLogger(HttpClientUtil.class);

    /**
     * 编码格式.
     */
    public static final String CHARSET = "UTF-8";

    /**
     * HTTP HEADER字段 Authorization应填充字符串Bearer
     */
    public static final String BEARER = "Bearer ";
    /**
     * 环境
     */
    public static final String URL = "https://sandbox.hscloud.cn";
    /**
     * 环境
     */
    public static final String OPENURL = "https://open.hscloud.cn";

    /**
     * HTTP HEADER字段 Authorization应填充字符串BASIC
     */
    public static final String BASIC = "Basic ";


    private static CloseableHttpClient httpClient = null;
    /**
     * 指客户端和服务器建立连接的timeout  毫秒
     */
    public final static int connectTimeout = 10000;

    /**
     * socket连接超时时间 客户端从服务器读取数据的timeout，超出后会抛出SocketTimeOutException
     */
    public final static int socketTimeout = 10000;

    /**
     * 从连接池获取连接的timeout
     */
    public final static int requestTimeout = 5000;
    /**
     * 允许管理器限制最大连接数 ，还允许每个路由器针对某个主机限制最大连接数。
     */
    public static PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager();
    /**
     * 最大连接数
     */
    public final static int MAX_TOTAL_CONNECTIONS = 500;
    /**
     * 每个路由最大连接数 访问每个目标机器 算一个路由 默认 2个
     */
    public final static int MAX_ROUTE_CONNECTIONS = 80;

    private static RequestConfig requestConfig;
    private static final int MAX_TIMEOUT = 10000;


    static {
        cm.setDefaultMaxPerRoute(MAX_ROUTE_CONNECTIONS);// 设置最大路由数
        cm.setMaxTotal(MAX_TOTAL_CONNECTIONS);// 最大连接数

        /**
         * 大量的构造器设计模式，很多的配置都不建议直接new出来，而且相关的API也有所改动，例如连接参数，
         * 以前是直接new出HttpConnectionParams对象后通过set方法逐一设置属性， 现在有了构造器，可以通过如下方式进行构造：
         * SocketConfig.custom().setSoTimeout(100000).build();
         */
        SocketConfig socketConfig = SocketConfig.custom().setTcpNoDelay(true)
                .build();
        cm.setDefaultSocketConfig(socketConfig);
        RequestConfig defaultRequestConfig = RequestConfig.custom()
                .setCookieSpec(CookieSpecs.BEST_MATCH)
                .setExpectContinueEnabled(true)
                .setStaleConnectionCheckEnabled(true).setRedirectsEnabled(true)
                .build();
        // CodingErrorAction.IGNORE指示通过删除错误输入、向输出缓冲区追加 coder
        // 的替换值和恢复编码操作来处理编码错误的操作。
        ConnectionConfig connectionConfig = ConnectionConfig.custom()
                .setCharset(Consts.UTF_8)
                .setMalformedInputAction(CodingErrorAction.IGNORE)
                .setUnmappableInputAction(CodingErrorAction.IGNORE).build();
        //cm 连接manager默认的构造函数含有http https的注册机制
        httpClient = HttpClients.custom().setConnectionManager(cm)
                .setDefaultRequestConfig(defaultRequestConfig)
                .setDefaultConnectionConfig(connectionConfig).build();
        //用于httpPOST HttpGet的请求设置
        RequestConfig.Builder configBuilder = RequestConfig.custom();
        // 设置连接超时
        configBuilder.setConnectTimeout(MAX_TIMEOUT);
        // 设置读取超时
        configBuilder.setSocketTimeout(MAX_TIMEOUT);
        // 设置从连接池获取连接实例的超时
        configBuilder.setConnectionRequestTimeout(MAX_TIMEOUT);
        // 在提交请求之前 测试连接是否可用
        configBuilder.setStaleConnectionCheckEnabled(true);
        requestConfig = configBuilder.build();
    }


    public static String sendPost(String url, Map<String, String> params,
                                  String charSet, String charsetReturn, HttpHost proxy,
                                  String authorization, String interfacename) {
        try {
            HttpPost post = new HttpPost(url);
            RequestConfig.Builder builder = RequestConfig.custom();
            if (proxy != null) {
                builder.setProxy(proxy);
                RequestConfig requestConfig = builder
                        //与服务器连接超时时间：httpclient会创建一个异步线程用以创建socket连接，
                        // 此处设置该socket的连接超时时间
                        .setSocketTimeout(socketTimeout)
                       // 请求获取数据的超时时间(即响应时间)，单位毫秒。
                       // 如果访问一个接口，多少时间内无法返回数据，就直接放弃此次调用。
                        .setConnectTimeout(connectTimeout)
                       //  设置从connect Manager(连接池)获取Connection 超时时间，单位毫秒。
                       // 这个属性是新加的属性，因为目前版本是可以共享连接池的。
                        .setConnectionRequestTimeout(requestTimeout)
                        .setExpectContinueEnabled(false)
                        .setRedirectsEnabled(true).build();
                post.setConfig(requestConfig);
            }

            post.setHeader("Content-Type", "application/x-www-form-urlencoded");
            post.setHeader("Authorization", authorization);
            List<NameValuePair> nvps = new ArrayList<>();
            StringBuffer sb = new StringBuffer();
            if (params != null) {
                int n = 0;
                for (Entry<String, String> set : params.entrySet()) {
                    if (n == 0) {
                        n++;
                        sb.append(set.getKey() + "=" + set.getValue());
                    } else {
                        sb.append("&" + set.getKey() + "=" + set.getValue());
                    }
                    nvps.add(new BasicNameValuePair(set.getKey(), set
                            .getValue()));
                }
            }
            post.setEntity(new UrlEncodedFormEntity(nvps, charSet));
            LogUtils.log("\n功能名称：" + interfacename + "\n" + "post  url = ["
                    + (url.endsWith("?") ? url : url + "?") + sb.toString()
                    + "]", log);

            HttpResponse response = httpClient.execute(post);
            int status = response.getStatusLine().getStatusCode();
            HttpEntity entity = null;
            try {
                entity = response.getEntity();
                if (entity != null) {
                    String result = EntityUtils.toString(entity, charsetReturn);
                    LogUtils.log("result = " + result, log);
                    return result;
                } else {
                    return null;
                }

            } catch (Exception e) {
                LogUtils.log("HttpClient   请求 http状态码 status = [" + status
                        + "]  获取HttpEntity ", e, log);
                throw new RuntimeException(e);
            } finally {
                if (entity != null) {
                    entity.getContent().close();
                }
            }
        } catch (ClientProtocolException e) {
            LogUtils.log("HttpClient   请求  ClientProtocolException ", e, log);
            throw new RuntimeException(e);
        } catch (IOException e) {
            LogUtils.log("HttpClient   请求  IOException ", e, log);
            throw new RuntimeException(e);
        }
    }

    public static String sendPost(String url, Map<String, String> params,Map<String, String> headerMap, HttpHost proxy) {
        try {
            HttpPost post = new HttpPost(url);
            initPostRequestConfig(post,proxy);
            post.setHeader("Content-Type", "application/x-www-form-urlencoded");
            post.setHeader("merchantId", headerMap.get("merchantId"));
            post.setHeader("userId", headerMap.get("userId"));
            List<NameValuePair> npList = organizeParamsAsNamePair(params);
            post.setEntity(new UrlEncodedFormEntity(npList, CHARSET));
            HttpResponse response = httpClient.execute(post);
            int status = response.getStatusLine().getStatusCode();
            HttpEntity entity = null;
            try {
                entity = response.getEntity();
                if (entity != null) {
                    String result = EntityUtils.toString(entity, CHARSET);
                    LogUtils.log("result = " + result, log);
                    return result;
                }else {
                    return null;
                }

            } catch (Exception e) {
                LogUtils.error("HttpClient   请求 http状态码 status = [" + status
                        + "]  获取HttpEntity ", e, log);
                throw new RuntimeException(e);
            } finally {
                if (entity != null) {
                    entity.getContent().close();
                }
            }
        } catch (ClientProtocolException e) {
            LogUtils.log("HttpClient   请求  ClientProtocolException ", e, log);
            throw new RuntimeException(e);
        } catch (IOException e) {
            LogUtils.log("HttpClient   请求  IOException ", e, log);
            throw new RuntimeException(e);
        }
    }

    /**
     * @description
     * @author CF create on 2018/7/4 18:28
     */
    public static String sendPost(String url, String contentType,
                                  StringEntity stringEntity, String charsetReturn, HttpHost proxy) {
        try {
            HttpPost post = new HttpPost(url);
            RequestConfig.Builder builder = RequestConfig.custom();
            if (proxy != null) {
                builder.setProxy(proxy);
                RequestConfig requestConfig = builder
                        .setSocketTimeout(socketTimeout)
                        .setConnectTimeout(connectTimeout)
                        .setConnectionRequestTimeout(requestTimeout)
                        .setExpectContinueEnabled(false)
                        .setRedirectsEnabled(true).build();
                post.setConfig(requestConfig);
            }
            post.setHeader("Content-Type", contentType);
            post.setEntity(stringEntity);
            HttpResponse response = httpClient.execute(post);
            int status = response.getStatusLine().getStatusCode();
            HttpEntity entity = null;
            try {
                entity = response.getEntity();
                if (entity != null) {
                    String result = EntityUtils.toString(entity, charsetReturn);
                    LogUtils.log("result = " + result, log);
                    return result;
                } else {
                    return null;
                }

            } catch (Exception e) {
                LogUtils.log("HttpClient   请求 http状态码 status = [" + status
                        + "]  获取HttpEntity ", e, log);
                throw new RuntimeException(e);
            } finally {
                if (entity != null) {
                    entity.getContent().close();
                }
            }
        } catch (ClientProtocolException e) {
            LogUtils.log("HttpClient   请求  ClientProtocolException ", e, log);
            throw new RuntimeException(e);
        } catch (IOException e) {
            LogUtils.log("HttpClient   请求  IOException ", e, log);
            throw new RuntimeException(e);
        }
    }
    /**
     * get请求
     *
     * @param url
     * @param params
     * @param charSet
     * @return
     */
    public static String sendGet(String url, Map<String, String> params,
                                 String charSet, HttpHost proxy, String authorization,
                                 String interfacename) {
        try {
            StringBuffer urlbuf = new StringBuffer(url);
            if (params != null) {
                int n = 0;
                for (Entry<String, String> set : params.entrySet()) {
                    if (!urlbuf.toString().contains("?")) {
                        urlbuf.append("?");
                    }
                    if (n != 0) {
                        urlbuf.append("&");
                    }
                    urlbuf.append(set.getKey()).append("=")
                            .append(set.getValue());
                    n++;
                }
            }
//            LogUtils.log("\n功能名称：" + interfacename + "\n" + "post  url = [" + urlbuf.toString() + "]", log);
            HttpGet get = new HttpGet(urlbuf.toString());
            get.setHeader("Content-Type", "application/x-www-form-urlencoded");
            get.setHeader("Authorization", authorization);
            // HttpUriRequest get = new HttpGet(urlbuf.toString());
            RequestConfig.Builder builder = RequestConfig.custom();
            if (proxy != null) {
                builder.setProxy(proxy);
            }

            RequestConfig defaultConfig = builder
                    .setSocketTimeout(socketTimeout)
                    .setConnectTimeout(connectTimeout)
                    .setConnectionRequestTimeout(requestTimeout)
                    .setExpectContinueEnabled(false).setRedirectsEnabled(true)
                    .build();
            get.setConfig(defaultConfig);

            HttpResponse response = httpClient.execute(get);

            int status = response.getStatusLine().getStatusCode();
            HttpEntity entity = null;
            try {
                entity = response.getEntity();
                if (entity != null) {
                    String result = EntityUtils.toString(entity, charSet);

                    if (result.startsWith("Error")) {
                        log.error("url is :" + urlbuf.toString());
                        log.error("result:" + result);
                        log.error("status:" + status);
                    }
//                    LogUtils.log("result = " + result, log);
                    return result;

                } else {
                    return null;
                }

            } catch (Exception e) {
                LogUtils.error("HttpClient   请求 http状态码 status = [" + status
                        + "]  ", e, log);
                throw new RuntimeException(e);
            } finally {
                if (entity != null) {
                    entity.getContent().close();
                }
            }
        } catch (ClientProtocolException e) {
            LogUtils.error("HttpClient   请求  ClientProtocolException url=" + url + ",params=" + params, e, log);
            throw new RuntimeException(e);
        } catch (IOException e) {
            LogUtils.error("HttpClient   请求  IOException url=" + url + ",params=" + params, e, log);
            throw new RuntimeException(e);
        }

    }

    /**
     * cifangf 是对"App Key:App Secret"进行 Base64 编码后的字符串（区分大小写，包含冒号，但不包含双引号,采用
     * UTF-8 编码）。 例如: Authorization: Basic eHh4LUtleS14eHg6eHh4LXNlY3JldC14eHg=
     * 其中App Key和App Secret可在开放平台上创建应用后获取。
     */
    public static String Base64(String appkey, String appsecret, String basic) throws UnsupportedEncodingException {
        String str = appkey + ":" + appsecret;
        byte[] encodeBase64 = Base64.encodeBase64(str
                .getBytes(HttpClientUtil.CHARSET));
        LogUtils.log("\n功能名称：AppKey:AppSecret Base64编码" + "\n" + new String(encodeBase64), log);
        return basic + new String(encodeBase64);
    }

    public static class LogUtils {

        public static void log(String msg, Logger log) {
            /*System.out.println(msg);*/
            log.info(msg);
        }

        public static void log(String msg, Exception e, Logger log) {
			/*System.out.println(msg + " 异常 message = [" + e.getMessage() + "]");*/
            //log.info(msg + " 异常 message = [" + e.getMessage() + "]", e);
        }

        public static void error(String msg, Exception e, Logger log) {
			/*System.out.println(msg + " 异常 message = [" + e.getMessage() + "]");*/
            log.error(msg + " 异常 message = [" + e.getMessage() + "]", e);
        }

    }

    public static CloseableHttpClient getCloseableClient() {
        return httpClient;
    }

    /**
     * 发送 SSL POST请（HTTPS），K-V形式
     *
     * @param url
     * @param params
     * @author Charlie.chen
     */
    public static String doPostSSL(String url, Map<String, Object> params) {
       /* CloseableHttpClient httpClient = HttpClients.custom().setSSLSocketFactory(createSSLConn())
                .setConnectionManager(cm)
                .setDefaultRequestConfig(requestConfig).build();*/
        HttpPost httpPost = new HttpPost(url);
        CloseableHttpResponse response = null;
        String httpStr = null;

        try {
            httpPost.setConfig(requestConfig);
            List<NameValuePair> pairList = new ArrayList<NameValuePair>(params.size());
            for (Map.Entry<String, Object> entry : params.entrySet()) {
                NameValuePair pair = new BasicNameValuePair(entry.getKey(), entry
                        .getValue().toString());
                pairList.add(pair);
            }
            httpPost.setEntity(new UrlEncodedFormEntity(pairList, Charset.forName("utf-8")));
            response = httpClient.execute(httpPost);
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode != HttpStatus.SC_OK) {
                return null;
            }
            HttpEntity entity = response.getEntity();
            if (entity == null) {
                return null;
            }
            httpStr = EntityUtils.toString(entity, "utf-8");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (response != null) {
                try {
                    EntityUtils.consume(response.getEntity());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return httpStr;
    }


    /**
     * 创建SSL安全连接
     *
     * @return
     * @author Charlie.chen
     */
    private static SSLConnectionSocketFactory createSSLConn() {
        SSLConnectionSocketFactory sslsf = null;
        try {
            SSLContext sslContext = new SSLContextBuilder().loadTrustMaterial(null, new TrustStrategy() {
                @Override
                public boolean isTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                    return true;
                }
            }).build();
            sslsf = new SSLConnectionSocketFactory(
                    sslContext,
                    new String[]{"TLS"},//v1.2
                    null,
                    SSLConnectionSocketFactory.getDefaultHostnameVerifier());

        } catch (GeneralSecurityException e) {
            e.printStackTrace();
        }
        return sslsf;
    }


    /**
     * 获取请求主机IP地址,如果通过代理进来，则透过防火墙获取真实IP地址;
     *
     * @param request
     * @return
     * @throws IOException
     */
    public static String getIpAddress(HttpServletRequest request) {

        // 获取请求主机IP地址,如果通过代理进来，则透过防火墙获取真实IP地址

        String ip = request.getHeader("X-Forwarded-For");

        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            if (ip == null || ip.length() == 0
                    || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getHeader("Proxy-Client-IP");
            }
            if (ip == null || ip.length() == 0
                    || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getHeader("WL-Proxy-Client-IP");
            }
            if (ip == null || ip.length() == 0
                    || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getHeader("HTTP_CLIENT_IP");
            }
            if (ip == null || ip.length() == 0
                    || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getHeader("HTTP_X_FORWARDED_FOR");
            }
            if (ip == null || ip.length() == 0
                    || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getRemoteAddr();
            }
        } else if (ip.length() > 15) {
            String[] ips = ip.split(",");
            for (int index = 0; index < ips.length; index++) {
                String strIp = ips[index];
                if (!("unknown".equalsIgnoreCase(strIp))) {
                    ip = strIp;
                    break;
                }
            }
        }
        return ip;
    }

    private static void initPostRequestConfig(HttpPost post,HttpHost proxy) {
        RequestConfig.Builder builder = RequestConfig.custom();
        if (proxy != null) {
            builder.setProxy(proxy);
            RequestConfig requestConfig = builder
                    .setSocketTimeout(socketTimeout)
                    .setConnectTimeout(connectTimeout)
                    .setConnectionRequestTimeout(requestTimeout)
                    .setExpectContinueEnabled(false)
                    .setRedirectsEnabled(true).build();
            post.setConfig(requestConfig);
        }
    }

    private static List<NameValuePair> organizeParamsAsNamePair(Map<String, String> params) {
        List<NameValuePair> nvps = new ArrayList<>();
        StringBuilder sb = new StringBuilder();
        if (params != null) {
            int n = 0;
            for (Entry<String, String> set : params.entrySet()) {
                if (n == 0) {
                    n++;
                    sb.append(set.getKey()).append("=").append(set.getValue());
                } else {
                    sb.append("&").append(set.getKey()).append("=").append(set.getValue());
                }
                nvps.add(new BasicNameValuePair(set.getKey(), set.getValue()));
            }
        }
        return nvps;
    }
}
