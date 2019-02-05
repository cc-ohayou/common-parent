package com.ccspace.manager.mo.impl;

import com.ccspace.dao.mappers.UserMapper;
import com.ccspace.manager.mo.UserManager;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @AUTHOR CF
 * @DATE Created on 2019/2/5/005 16:49.
 */
@Component
public class UserManagerImpl extends BaseManagerImpl implements UserManager {

    @Resource
    UserMapper  userMapper;

    @Override
    public Object daoTest() {
        return userMapper.getAllUserList();
    }
}
