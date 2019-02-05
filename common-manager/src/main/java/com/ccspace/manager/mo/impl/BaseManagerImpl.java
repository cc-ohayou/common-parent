package com.ccspace.manager.mo.impl;

import com.alibaba.fastjson.JSON;

import com.ccspace.manager.mo.BaseManager;
import com.google.common.collect.Sets;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.util.*;

/**
 * @AUTHOR CF
 * @DATE Created on 2017/9/22 9:25.
 */
@Component
public class BaseManagerImpl implements BaseManager {
    public static Set<Integer> dayOfYearSet = Sets.newConcurrentHashSet();//节假日集合
    private static Logger logger = LoggerFactory.getLogger(BaseManagerImpl.class);

    @Override
    public boolean isTradeDay() {
        return false;
    }
}
