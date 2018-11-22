package com.jichuangsi.school.user.service;

import com.jichuangsi.school.user.exception.UserServiceException;
import com.jichuangsi.school.user.commons.PageResult;
import com.jichuangsi.school.user.entity.UserInfo;

import java.util.List;

public interface UserInfoService {
    /**
     * 根据用户ID获取用户信息
     */
    UserInfo findUserInfo(String userId)throws UserServiceException;;

    /*
     *
     * 删除用户
     * */
    List<String> deleteUserInfo(String[] ids) throws UserServiceException;

    /**
     * 修改用户信息
     */
    //UserInfo UpdateUserInfo(UserInfo UserInfo);

    /**
     * 带@RequestBody注解
     * @param UserInfo
     * @return
     */
    UserInfo UpdateUserInfo(UserInfo UserInfo) throws UserServiceException;

    /**
     * 查询全部
     * @param
     * @param
     * @param
     * @return
     */


    List<UserInfo> findAllUser() throws UserServiceException;
    List<UserInfo> findUser(String id) throws UserServiceException;
    /**
     *根据Id进行修改
     */
    String delteById(String userId)throws UserServiceException;
    /**
     * 根据Id进行恢复操作
     */
    String RestoreById(String userId) throws UserServiceException;

    PageResult findByPage(Integer page, Integer rows);
    /**
     * 新建用户
     */
    UserInfo saveUserInfo(UserInfo userInfo)  throws UserServiceException;

    void RestoreUsers(String[] ids)throws UserServiceException;
}
