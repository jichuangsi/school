package com.jichuangsi.school.testservice.service;

import com.jichuangsi.microservice.common.model.UserInfoForToken;
import com.jichuangsi.school.testservice.exception.TeacherTestServiceException;
import com.jichuangsi.school.testservice.model.TestModelForTeacher;
import com.jichuangsi.school.testservice.model.QuestionModelForTeacher;
import com.jichuangsi.school.testservice.model.SearchTestModel;
import com.jichuangsi.school.testservice.model.common.DeleteQueryModel;
import com.jichuangsi.school.testservice.model.common.Elements;
import com.jichuangsi.school.testservice.model.common.PageHolder;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface ITestConsoleService {

    @Transactional
    void saveNewTest(UserInfoForToken userInfo, TestModelForTeacher testModelForTeacher) throws TeacherTestServiceException;

    @Transactional
    Elements getElementsList(UserInfoForToken userInfo) throws TeacherTestServiceException;

    @Transactional
    PageHolder<TestModelForTeacher> getSortedTestsList(UserInfoForToken userInfo, SearchTestModel searchTestModel) throws TeacherTestServiceException;

    @Transactional
    List<QuestionModelForTeacher> getQuestionList(List<String> qIds) throws TeacherTestServiceException;

    @Transactional
    void deleteTestIsNotStart(UserInfoForToken userInfo, DeleteQueryModel deleteQueryModel) throws TeacherTestServiceException;

    @Transactional
    void updateTestIsNotStart(UserInfoForToken userInfo, TestModelForTeacher testModelForTeacher) throws TeacherTestServiceException;

    @Transactional
    void updateTest2NewStatus(UserInfoForToken userInfo, TestModelForTeacher testModelForTeacher) throws TeacherTestServiceException;

}
