package com.jichuangsi.school.user.service;

import com.github.pagehelper.PageInfo;
import com.jichuangsi.microservice.common.model.UserInfoForToken;
import com.jichuangsi.school.user.commons.PageResult;
import com.jichuangsi.school.user.exception.UserServiceException;
import com.jichuangsi.school.user.feign.model.ClassTeacherInfoModel;
import com.jichuangsi.school.user.model.System.User;
import com.jichuangsi.school.user.model.backstage.UpdatePwdModel;
import com.jichuangsi.school.user.model.school.SchoolRoleModel;
import com.jichuangsi.school.user.model.school.UserConditionModel;
import com.jichuangsi.school.user.model.transfer.TransferClass;
import com.jichuangsi.school.user.model.transfer.TransferSchool;
import com.jichuangsi.school.user.model.transfer.TransferStudent;
import com.jichuangsi.school.user.model.transfer.TransferTeacher;
import com.jichuangsi.school.user.model.user.StudentModel;
import com.jichuangsi.school.user.model.user.TeacherModel;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

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
    void deleteUserInfo(String[] ids) throws UserServiceException;

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
    void restoreUsers(String[] ids)throws UserServiceException;

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
    void TrulyDeleted(String[] ids)throws UserServiceException;

    @Transactional
    List<TransferStudent> getStudentsByClassId(String classId);

    @Transactional
    PageInfo<TransferStudent> getStudentsByClassIdInPage(String classId, int pageIndex,int pageSize) throws UserServiceException;

    void saveTeacher(UserInfoForToken userInfo , TeacherModel model) throws UserServiceException;

    void saveStudent(UserInfoForToken userInfo, StudentModel model) throws UserServiceException;

    String saveExcelStudents(MultipartFile file,UserInfoForToken userInfo) throws UserServiceException;

    List<TransferTeacher> getTeachersByClassId(String classId) throws UserServiceException;

    void coldUserInfo(String userId) throws UserServiceException;

    void updateTeacher(UserInfoForToken userInfo,TeacherModel model) throws UserServiceException;

    void updateStudent(UserInfoForToken userInfo,StudentModel model) throws UserServiceException;

    void updateOtherPwd(UserInfoForToken userInfo, UpdatePwdModel model, String userId) throws UserServiceException;

    void insertSchoolRole(UserInfoForToken userInfo, SchoolRoleModel model,String schoolId) throws UserServiceException;

    void updateSchoolRole(UserInfoForToken userInfo ,SchoolRoleModel model) throws UserServiceException;

    List<SchoolRoleModel> getSchoolRoles(UserInfoForToken userInfo,String schoolId) throws UserServiceException;

    void deleteSchoolRole(UserInfoForToken userInfo , String roleId) throws UserServiceException;

    PageInfo<TeacherModel> getTeachers(UserInfoForToken userInfo,String schoolId,int pageIndex,int pageSize) throws UserServiceException;

    List<ClassTeacherInfoModel> getStudentTeachers(String studentId) throws UserServiceException;

    PageInfo<TeacherModel> getTeachersByCondition(UserInfoForToken userInfo, UserConditionModel model) throws UserServiceException;

    PageInfo<StudentModel> getStudentByCondition(UserInfoForToken userInfo,UserConditionModel model) throws UserServiceException;
}
