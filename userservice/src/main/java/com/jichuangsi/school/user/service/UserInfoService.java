package com.jichuangsi.school.user.service;

import com.jichuangsi.school.user.exception.UserServiceException;
import com.jichuangsi.school.user.commons.PageResult;
import com.jichuangsi.school.user.model.System.User;
import com.jichuangsi.school.user.model.transfer.TransferClass;
import com.jichuangsi.school.user.model.transfer.TransferSchool;
import com.jichuangsi.school.user.model.transfer.TransferTeacher;
import org.springframework.transaction.annotation.Transactional;

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

    /**
     * 批量恢复
     * @param ids
     * @return
     * @throws UserServiceException
     */
    @Transactional
    long restoreUsers(String[] ids)throws UserServiceException;

    /**
     * 通过老师ID获取老师信息
     * @param teacherId
     * @return
     */
    @Transactional
    TransferTeacher getTeacherById(String teacherId);

    /**
     * 通过班级Id获取班级
     * @param id
     * @return
     */
    @Transactional
    List<TransferClass> getTeacherClass(String id);

    /**
     * 通过学校Id获取学习
     * @param id
     * @return
     */
    @Transactional
    TransferSchool getSchoolInfoById(String id);

    /**
     * 获取所有被删的信息
     * @return
     * @throws UserServiceException
     */
    @Transactional
    List<User> findAllForDelete()throws UserServiceException;

    /**
     * 通过Id获取被删的信息
     * @param id
     * @return
     * @throws UserServiceException
     */
    @Transactional
    User findDeleteOne(String id) throws UserServiceException ;

    /**
     * 通过Id批量真正删除信息
     * @param ids
     * @return
     * @throws UserServiceException
     */
    Long TrulyDeleted(String[] ids)throws UserServiceException;
}
