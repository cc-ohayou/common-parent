package com.ccspace.facade.domain.common.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetAddress;

/**
 * @AUTHOR CF
 * @DATE Created on 2017/9/12 20:44.
 */
public class IdGen {
    static Logger logger = LoggerFactory.getLogger(IdGen.class);

    public synchronized static Long genId() {
        long id = worker.nextId();
        logger.info("gen Id is: " + id);
        return Long.valueOf(id);
    }

    //根据具体机器环境提供
    private final long workerId;
    //滤波器,使时间变小,生成的总位数变小,一旦确定不能变动
    private final static long twepoch = 1288834974657L;
    private long sequence = 0L;
    private final static long workerIdBits = 10L;
    private final static long maxWorkerId = -1L ^ -1L << workerIdBits;
    private final static long sequenceBits = 12L;

    private final static long workerIdShift = sequenceBits;
    private final static long timestampLeftShift = sequenceBits + workerIdBits;
    private final static long sequenceMask = -1L ^ -1L << sequenceBits;

    private long lastTimestamp = -1L;
    //根据主机id获取机器码
    private static IdGen worker = new IdGen();

    public IdGen() {
        this.workerId = getAddress() % (IdGen.maxWorkerId + 1);
    }

    public synchronized long nextId() {
        long timestamp = this.timeGen();
        if (this.lastTimestamp == timestamp) {
            this.sequence = (this.sequence + 1) & IdGen.sequenceMask;
            if (this.sequence == 0) {
                //System.out.println("###########" + sequenceMask);//等待下一毫秒
                timestamp = this.tilNextMillis(this.lastTimestamp);
            }
        } else {
            this.sequence = 0;
        }
        if (timestamp < this.lastTimestamp) {
            try {
                throw new Exception(String.format(
                        "Clock moved backwards.  Refusing to generate id for %d milliseconds", this.lastTimestamp
                                - timestamp));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        this.lastTimestamp = timestamp;


        long nextId = ((timestamp - twepoch << timestampLeftShift)) | (this.workerId << IdGen.workerIdShift)
                | (this.sequence);
        //  System.out.println("timestamp:" + timestamp + ",timestampLeftShift:"
        //      + timestampLeftShift + ",nextId:" + nextId + ",workerId:"
        //      + workerId + ",sequence:" + sequence);
        return nextId;
    }

    private long tilNextMillis(final long lastTimestamp1) {
        long timestamp = this.timeGen();
        while (timestamp <= lastTimestamp1) {
            timestamp = this.timeGen();
        }
        return timestamp;
    }

    private static long getAddress() {
        try {
            String currentIpAddress = InetAddress.getLocalHost().getHostAddress();
            String[] str = currentIpAddress.split("\\.");
            StringBuilder hardware = new StringBuilder();
            for (int i = 0; i < str.length; i++) {
                hardware.append(str[i]);
            }
            return Long.parseLong(hardware.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }

        return 2L;
    }

    private long timeGen() {
        return System.currentTimeMillis();
    }


    public static void main(String[] args) { System.out.println(IdGen.genId());
    }
}
