package com.ccspace.facade.domain.common.util;

import com.ccspace.facade.domain.common.handler.MyThreadTaskAbortPolicy;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import org.slf4j.Logger;

import java.util.concurrent.*;

/**
 * @AUTHOR CF
 * @DATE Created on 2018/10/24 17:57.
 */
public class ThreadPoolInit {

    public static ExecutorService initFixedPool(int maxpoolSize, int coreSize, String type, Logger logger) {

        ThreadFactory namedThreadFactory = new ThreadFactoryBuilder()
                .setNameFormat(type + "-pool-%d").build();
        return new ThreadPoolExecutor(8, 8,
                0L, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<>(10000),
                namedThreadFactory,
                new MyThreadTaskAbortPolicy(logger, type));
    }

    public static ExecutorService initCachePool(String type, Logger logger) {

        ThreadFactory namedThreadFactory = new ThreadFactoryBuilder()
                .setNameFormat(type + "-pool-%d").build();
        return Executors.newCachedThreadPool();
    }
}
