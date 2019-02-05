package com.ccspace.facade.domain.common.util;

import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.URL;
import java.net.UnknownHostException;

/**
 * @AUTHOR CF
 * @DATE Created on 2018/10/26 16:26.
 */
@Slf4j
public class IpUtil {
    private static final String STR_URL="http://2018.ip138.com/ic.asp";

    /**
     * 获取外网地址
     */
    public static String getWebIP() {
        BufferedReader br = null;
        try {
            //连接网页
            URL url = new URL(STR_URL);
            //编码自己查看抓取网页的编码方式
            br = new BufferedReader(new InputStreamReader(url.openStream(), "gb2312"));
            String s = "";
            StringBuilder sb = new StringBuilder("");
            String webContent = "";
            //读取网页信息
            while ((s = br.readLine()) != null) {
                sb.append(s).append("\r\n");
            }
            br.close();
            //网页信息
            webContent = sb.toString();
            int start = webContent.indexOf("[")+1;
            int end = webContent.indexOf("]");
            //获取网页中  当前 的 外网IP
            webContent = webContent.substring(start,end);
            return webContent;

        } catch (Exception e) {
            e.printStackTrace();
            return "error open url:" + STR_URL;
        } finally {
            if (null != br) {
                try {
                    br.close();
                } catch (IOException e) {
                    log.error("method getWebIP exception when BufferedReader try to close ", e);
                }
            }
        }
    }

    /**
     * @description
     * @author CF create on 2018/10/26 16:28
     */
    public static String getLocalIp() {
        InetAddress address =  null;
        //获取的是本地的IP地址 //PC-20140317PXKX/192.168.0.121
        try {
            address = InetAddress.getLocalHost();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        return address==null?"":address.getHostAddress();
    }

    public static void main(String[] args) {
        System.out.println(getWebIP());
        System.out.println(getLocalIp());
    }


}
