package com.ccspace.server.core.service.impl;

import com.ccspace.manager.mo.UserManager;
import com.ccspace.server.core.service.SupportService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.xml.ws.ServiceMode;

/**
 * @AUTHOR CF
 * @DATE Created on 2019/2/5/005 16:48.
 */
@Service
public class SupportServiceImpl implements SupportService {

    @Resource
    UserManager userManager;

    @Override
    public Object daotest() {
        return userManager.daoTest();
    }
}
