package com.jichuangsi.school.courseservice.service;

import com.jichuangsi.microservice.common.model.UserInfoForToken;
import com.jichuangsi.school.courseservice.Exception.StudentCourseServiceException;
import com.jichuangsi.school.courseservice.model.AnswerForStudent;
import com.jichuangsi.school.courseservice.model.CourseFile;
import com.jichuangsi.school.courseservice.model.CourseForStudent;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface IStudentCourseService {

    @Transactional
    List<CourseForStudent> getCoursesList(UserInfoForToken userInfo) throws StudentCourseServiceException;

    @Transactional
    List<CourseForStudent> getHistoryCoursesList(UserInfoForToken userInfo) throws StudentCourseServiceException;

    @Transactional
    CourseForStudent getParticularCourse(UserInfoForToken userInfo, String courseId) throws StudentCourseServiceException;

    @Transactional
    AnswerForStudent uploadStudentSubjectPic(UserInfoForToken userInfo, CourseFile file) throws StudentCourseServiceException;

    @Transactional
    CourseFile downloadStudentSubjectPic(UserInfoForToken userInfo, String fileName) throws StudentCourseServiceException;

    @Transactional
    void deleteStudentSubjectPic(UserInfoForToken userInfo, String fileName) throws StudentCourseServiceException;

    @Transactional
    void saveStudentAnswer(UserInfoForToken userInfo, String courseId, String questionId, AnswerForStudent answer) throws StudentCourseServiceException;
}
