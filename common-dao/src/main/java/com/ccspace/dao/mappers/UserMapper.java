package com.ccspace.dao.mappers;

import com.ccspace.facade.domain.bizobject.UserInfo;
import com.ccspace.facade.domain.dataobject.User;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @AUTHOR CF
 * @DATE Created on 2018/4/22/022 15:19.
 */
//@Mapper
public interface UserMapper extends tk.mybatis.mapper.common.Mapper<User>{

    /**
     * 根据城市名称，查询城市信息
     *
     * @param realName 城市名
     */
    UserInfo findByName(@Param("userName") String realName);

    UserInfo findByPhone(@Param("phone") String phone);

    List<UserInfo> getAllUserList();

    /**
     * @description
     * @author CF create on 2019/1/14 11:58
     */
    int updateUserInfoSelective(@Param("pojo") User pojo);

    /**
     * @description
     * @author CF create on 2019/1/15 14:06
     */
    UserInfo selectByUid(String userId);

    UserInfo selectUserInfoBySelective(@Param("pojo") User pojo);
}
