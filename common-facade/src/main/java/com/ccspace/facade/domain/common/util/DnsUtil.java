package com.ccspace.facade.domain.common.util;

import com.ccspace.facade.domain.common.enums.SettingsEnum;
import com.ccspace.facade.domain.common.enums.SettingsHolder;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.CompareToBuilder;
import org.xbill.DNS.Lookup;
import org.xbill.DNS.MXRecord;
import org.xbill.DNS.Record;
import org.xbill.DNS.Type;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * @AUTHOR CF
 * @DATE Created on 2019/2/15 10:22.
 */
public class DnsUtil {

    public static void main(String[] args) {
        System.out.println(valid("13758080693@163.com", "www.baidu.com"));
    }

    /**
     * 31      * 验证邮箱是否存在
     * 32      * <br>
     * 33      * 由于要读取IO，会造成线程阻塞
     * 34      *
     * 35      * @param toMail
     * 36      *         要验证的邮箱
     * 37      * @param domain
     * 38      *         发出验证请求的域名(是当前站点的域名，可以任意指定)
     * 39      * @return
     * 40      *         邮箱是否可达
     * 41
     */
    public static boolean valid(String toMail, String domain) {
        if(StringUtils.isBlank(domain)){
            domain= SettingsHolder.getProperty(SettingsEnum.DEFAULT_MAIL_USE_DOMAIN);
        }
        if (StringUtils.isBlank(toMail) ) {
            return false;
        }
        if (!StringUtils.contains(toMail, '@')) {
            return false;
        }
        String host = toMail.substring(toMail.indexOf('@') + 1);
        if (host.equals(domain)) {
            return false;
        }
        Socket socket = new Socket();
        try {
            // 查找mx记录
            Record[] mxRecords = new Lookup(host, Type.MX).run();
            if (ArrayUtils.isEmpty(mxRecords)) {
                return false;
            }
            // 邮件服务器地址
            String mxHost = ((MXRecord) mxRecords[0]).getTarget().toString();
            if (mxRecords.length > 1) { // 优先级排序
                List<Record> arrRecords = new ArrayList<Record>();
                Collections.addAll(arrRecords, mxRecords);
                Collections.sort(arrRecords, new Comparator<Record>() {

                    @Override
                    public int compare(Record o1, Record o2) {
                        return new CompareToBuilder().append(((MXRecord) o1).getPriority(), ((MXRecord) o2).getPriority()).toComparison();
                    }

                });
                mxHost = ((MXRecord) arrRecords.get(0)).getTarget().toString();
            }
            // 开始 smtp
            socket.connect(new InetSocketAddress(mxHost, 25));
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new BufferedInputStream(socket.getInputStream())));
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            // 超时时间(毫秒)
            long timeout = 6000;
            // 睡眠时间片段(50毫秒)
            int sleepSect = 50;

            // 连接(服务器是否就绪)
            if (getResponseCode(timeout, sleepSect, bufferedReader) != 220) {
                return false;
            }

            // 握手
            bufferedWriter.write("HELO " + domain + "\r\n");
            bufferedWriter.flush();
            if (getResponseCode(timeout, sleepSect, bufferedReader) != 250) {
                return false;
            }
            // 身份
            bufferedWriter.write("MAIL FROM: <check@" + domain + ">\r\n");
            bufferedWriter.flush();
            if (getResponseCode(timeout, sleepSect, bufferedReader) != 250) {
                return false;
            }
            // 验证
            bufferedWriter.write("RCPT TO: <" + toMail + ">\r\n");
            bufferedWriter.flush();
            if (getResponseCode(timeout, sleepSect, bufferedReader) != 250) {
                return false;
            }
            // 断开
            bufferedWriter.write("QUIT\r\n");
            bufferedWriter.flush();
            return true;
        } catch (NumberFormatException | InterruptedException | IOException e) {
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
            }
        }
        return false;
    }

    private static int getResponseCode(long timeout, int sleepSect, BufferedReader bufferedReader) throws InterruptedException, NumberFormatException, IOException {
        int code = 0;
        for (long i = sleepSect; i < timeout; i += sleepSect) {
            Thread.sleep(sleepSect);
            if (bufferedReader.ready()) {
                String outline = bufferedReader.readLine();
                // FIXME 读完……
                while (bufferedReader.ready()) {
                    System.out.println(
                            bufferedReader.readLine());
                }
                System.out.println(outline);
                code = Integer.parseInt(outline.substring(0, 3));
                break;
            }
        }
        return code;
    }
}
