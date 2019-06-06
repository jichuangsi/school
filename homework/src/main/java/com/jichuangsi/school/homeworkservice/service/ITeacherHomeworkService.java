package com.jichuangsi.school.homeworkservice.service;

import com.jichuangsi.microservice.common.model.UserInfoForToken;
import com.jichuangsi.school.homeworkservice.exception.TeacherHomeworkServiceException;
import com.jichuangsi.school.homeworkservice.model.*;
import com.jichuangsi.school.homeworkservice.model.common.PageHolder;
import com.jichuangsi.school.homeworkservice.model.transfer.TransferStudent;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface ITeacherHomeworkService {

    @Transactional
    List<HomeworkModelForTeacher> getHomeworksList(UserInfoForToken userInfo) throws TeacherHomeworkServiceException;

    @Transactional
    PageHolder<HomeworkModelForTeacher> getHistoryHomeworksList(UserInfoForToken userInfo, SearchHomeworkModel searchHomeworkModel) throws TeacherHomeworkServiceException;

    @Transactional
    HomeworkModelForTeacher getParticularHomework(UserInfoForToken userInfo, String homeworkId) throws TeacherHomeworkServiceException;

    @Transactional
    HomeworkModelForStudent getParticularStudentHomework(UserInfoForToken userInfo, String homeworkId, String studentId) throws TeacherHomeworkServiceException;

    @Transactional
    QuestionModelForTeacher getParticularQuestion(UserInfoForToken userInfo, String questionId) throws TeacherHomeworkServiceException;

    @Transactional
    AnswerModelForStudent getParticularAnswer(String questionId, String studentId) throws TeacherHomeworkServiceException;

    @Transactional
    void saveTeacherAnswer(UserInfoForToken userInfo, String questionId, String studentAnswerId, AnswerModelForTeacher revise) throws TeacherHomeworkServiceException;

    @Transactional
    void updateParticularHomeworkStatus(UserInfoForToken userInfo, HomeworkModelForTeacher homeworkModelForTeacher) throws TeacherHomeworkServiceException;

    @Transactional
    List<TransferStudent> settleParticularHomework(UserInfoForToken userInfo, String homeworkId) throws TeacherHomeworkServiceException;
}
