package com.jichuangsi.school.testservice.service;

import com.jichuangsi.microservice.common.model.UserInfoForToken;
import com.jichuangsi.school.testservice.exception.TeacherTestServiceException;
import com.jichuangsi.school.testservice.model.*;
import com.jichuangsi.school.testservice.model.common.PageHolder;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface ITeacherTestService {

    @Transactional
    List<TestModelForTeacher> getTestsList(UserInfoForToken userInfo) throws TeacherTestServiceException;

    @Transactional
    PageHolder<TestModelForTeacher> getHistoryTestsList(UserInfoForToken userInfo, SearchTestModel searchTestModel) throws TeacherTestServiceException;

    @Transactional
    TestModelForTeacher getParticularTest(UserInfoForToken userInfo, String testId) throws TeacherTestServiceException;

    @Transactional
    QuestionModelForTeacher getParticularQuestion(UserInfoForToken userInfo, String questionId) throws TeacherTestServiceException;

    @Transactional
    AnswerModelForStudent getParticularAnswer(String questionId, String studentId) throws TeacherTestServiceException;

    @Transactional
    void saveTeacherAnswer(UserInfoForToken userInfo, String questionId, String studentAnswerId, AnswerModelForTeacher revise) throws TeacherTestServiceException;

    @Transactional
    void updateParticularTestStatus(UserInfoForToken userInfo, TestModelForTeacher testModelForTeacher) throws TeacherTestServiceException;
}
