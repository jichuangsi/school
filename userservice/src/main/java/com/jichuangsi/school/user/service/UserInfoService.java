package com.jichuangsi.school.user.service;

import com.jichuangsi.school.user.exception.UserServiceException;
import com.jichuangsi.school.user.commons.PageResult;
import com.jichuangsi.school.user.entity.UserInfo;
import com.jichuangsi.school.user.model.System.User;
import com.jichuangsi.school.user.model.transfer.TransferClass;
import com.jichuangsi.school.user.model.transfer.TransferSchool;
import com.jichuangsi.school.user.model.transfer.TransferTeacher;
import org.springframework.transaction.annotation.Transactional;
import com.jichuangsi.school.user.model.org.Class;

import java.util.List;

public interface UserInfoService {
    /**
     * 根据用户ID获取用户信息
     */
    @Transactional
    User findUserInfo(String userId)throws UserServiceException;;

    /*
     *
     * 删除用户
     * */
    @Transactional
    long deleteUserInfo(String[] ids) throws UserServiceException;

    /**
     * 修改用户信息
     */
    //UserInfo UpdateUserInfo(UserInfo UserInfo);

    /**
     * 带@RequestBody注解
     * @param user
     * @return
     */
    @Transactional
    User UpdateUserInfo(User user) throws UserServiceException;

    /**
     * 查询全部
     * @param
     * @param
     * @param
     * @return
     */

    @Transactional
    List<User> findAllUser() throws UserServiceException;

    @Transactional
    User findUser(String id) throws UserServiceException;
    /**
     *根据Id进行修改
     */
    @Transactional
    long deleteById(String userId)throws UserServiceException;
    /**
     * 根据Id进行恢复操作
     */
    @Transactional
    long restoreById(String userId) throws UserServiceException;

    PageResult findByPage(Integer page, Integer rows);
    /**
     * 新建用户
     */
    @Transactional
    User saveUserInfo(User user)  throws UserServiceException;

    @Transactional
    long restoreUsers(String[] ids)throws UserServiceException;

    @Transactional
    TransferTeacher getTeacherById(String teacherId);

    @Transactional
    List<TransferClass> getTeacherClass(String id);

    @Transactional
    TransferSchool getSchoolInfoById(String id);
}
