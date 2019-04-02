package com.jichuangsi.school.courseservice.service;

import com.jichuangsi.microservice.common.model.UserInfoForToken;
import com.jichuangsi.school.courseservice.Exception.StudentCourseServiceException;
import com.jichuangsi.school.courseservice.model.*;
import com.jichuangsi.school.courseservice.model.repository.QuestionQueryModel;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface IStudentCourseService {

    @Transactional
    List<CourseForStudent> getCoursesList(UserInfoForToken userInfo) throws StudentCourseServiceException;

    @Transactional
    PageHolder<CourseForStudent> getHistoryCoursesList(UserInfoForToken userInfo, CourseForStudent pageInform) throws StudentCourseServiceException;

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

    @Transactional
    QuestionForStudent getParticularQuestion(UserInfoForToken userInfo, String questionId) throws StudentCourseServiceException;

    @Transactional
    void addParticularQuestionInFavor(UserInfoForToken userInfo, String questionId) throws StudentCourseServiceException;

    @Transactional
    void removeParticularQuestionInFavor(UserInfoForToken userInfo, String questionId) throws StudentCourseServiceException;

    @Transactional
    List<QuestionForStudent> getFavorQuestionsList(UserInfoForToken userInfo) throws StudentCourseServiceException;

    @Transactional
    List<IncorrectQuestionReturnModel> getIncorrectQuestionList(UserInfoForToken userInfo, IncorrectQuestionQueryModel incorrectQuestionQueryModel) throws StudentCourseServiceException;

    @Transactional
    List findSimilarQuestionsList(UserInfoForToken userInfo, QuestionQueryModel questionQueryModel) throws StudentCourseServiceException;

    List<CourseForStudent> getCourseOnWeek(String classId) throws StudentCourseServiceException;

    @Transactional
    CourseFile downloadStudentAttachment(UserInfoForToken userInfo, String fileName) throws StudentCourseServiceException;
}
