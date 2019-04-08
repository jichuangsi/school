package com.jichuangsi.school.courseservice.service;

import com.jichuangsi.microservice.common.model.UserInfoForToken;
import com.jichuangsi.school.courseservice.Exception.TeacherCourseServiceException;
import com.jichuangsi.school.courseservice.entity.Course;
import com.jichuangsi.school.courseservice.entity.Question;
import com.jichuangsi.school.courseservice.model.*;
import com.jichuangsi.school.courseservice.model.common.DeleteQueryModel;
import com.jichuangsi.school.courseservice.model.transfer.TransferStudent;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

public interface ICourseConsoleService {

    //获取排列多条件课程列表
    @Transactional
    PageHolder<Course> getSortCoursesList(com.jichuangsi.school.courseservice.entity.Course course, PageHolder<Course> page, String keyWord, Integer sortNum, Date nowDay) throws TeacherCourseServiceException;

    //保存课程信息
    @Transactional
    void saveNewCourse(UserInfoForToken userInfoForToken, CourseForTeacher courseForTeacher) throws TeacherCourseServiceException;

    //验证是否存在时间冲突
    @Transactional
    long getCoursesListByTime(CourseForTeacher courseForTeacher);

    //获取新建的课程
    @Transactional
    List<Course> getNewCoursesList(String teacherId);

    //根据id查找course
    @Transactional
    Course getCourseById(String id);

    //删除未开始的课程
    @Transactional
    Boolean deleteCourseIsN(String courseId);

    //修改未开始的课程
    @Transactional
    void updateCourseIsN(UserInfoForToken userInfoForToken, CourseForTeacher courseForTeacher) throws TeacherCourseServiceException;

    @Transactional
    CourseForTeacher uploadIco(UserInfoForToken userInfo, CourseFile courseFile) throws TeacherCourseServiceException;

    @Transactional
    CourseFile downIco(UserInfoForToken userInfo, String pic) throws TeacherCourseServiceException;

    @Transactional
    void updateIco(UserInfoForToken userInfo, CourseFile courseFile, String cid) throws TeacherCourseServiceException;

    @Transactional
    List<Question> getQuestionList(List<String> qIds);

    @Transactional
    AttachmentModel uploadAttachment(UserInfoForToken userInfo, CourseFile courseFile) throws TeacherCourseServiceException;

    @Transactional
    void removeCourseAttachment(UserInfoForToken userInfo, DeleteQueryModel deleteQueryModel) throws TeacherCourseServiceException;

    void publishFileByTeacher(String fileName, String fileId, String courseId, UserInfoForToken userInfo) throws TeacherCourseServiceException;

    List<QuestionForTeacher> getWrongQuestions(List<String> questionIds) throws TeacherCourseServiceException;


}
