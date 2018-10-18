package com.jichuangsi.school.courseservice.service;

import com.jichuangsi.school.courseservice.Exception.TeacherCourseServiceException;
import com.jichuangsi.school.courseservice.model.*;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface ITeacherCourseService {
    @Transactional
    List<CourseForTeacher> getCoursesList(String teacherId) throws TeacherCourseServiceException;

    @Transactional
    List<CourseForTeacher> getHistoryCoursesList(String teacherId) throws TeacherCourseServiceException;

    @Transactional
    List<CourseForTeacher> queryCoursesList(String teacherId, CourseForTeacher course) throws TeacherCourseServiceException;

    @Transactional
    CourseForTeacher getParticularCourse(String teacherId, String courseId) throws TeacherCourseServiceException;

    @Transactional
    List<QuestionForTeacher> getQuestionsInParticularCourse(String teacherId, String courseId) throws TeacherCourseServiceException;

    @Transactional
    QuestionForTeacher getParticularQuestion(String questionId) throws TeacherCourseServiceException;

    @Transactional
    List<AnswerForStudent> getAnswersInPaticularCourse(String questionId) throws TeacherCourseServiceException;

    @Transactional
    AnswerForStudent getParticularAnswer(String questionId, String studentId) throws TeacherCourseServiceException;

    @Transactional
    CourseForTeacher saveCourse(CourseForTeacher course);

    @Transactional
    void deleteCourse(CourseForTeacher course);

    @Transactional
    List<QuestionForTeacher> saveQuestions(String courseId, List<QuestionForTeacher> questions) throws TeacherCourseServiceException;

    @Transactional
    void startCourse(CourseForTeacher course) throws TeacherCourseServiceException;

    @Transactional
    void updateParticularCourseStatus(CourseForTeacher course) throws TeacherCourseServiceException;

    @Transactional
    void updateParticularQuestionStatus(QuestionForTeacher questionStatus) throws TeacherCourseServiceException;

    @Transactional
    void saveTeacherAnswer(String teacherId, String questinoId, String studentAnswerId, AnswerForTeacher revise) throws TeacherCourseServiceException;

    @Transactional
    AnswerForTeacher uploadTeacherSubjectPic(String teacherId, CourseFile file) throws TeacherCourseServiceException;

    @Transactional
    CourseFile downloadTeacherSubjectPic(String teacherId, String fileName) throws TeacherCourseServiceException;

    @Transactional
    void deleteTeacherSubjectPic(String teacherId, String fileName) throws TeacherCourseServiceException;
}
