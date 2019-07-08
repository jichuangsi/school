package com.jichuangsi.school.testservice.service;

import com.jichuangsi.microservice.common.model.UserInfoForToken;
import com.jichuangsi.school.testservice.entity.Test;
import com.jichuangsi.school.testservice.exception.StudentTestServiceException;
import com.jichuangsi.school.testservice.model.AnswerModelForStudent;
import com.jichuangsi.school.testservice.model.TestModelForStudent;
import com.jichuangsi.school.testservice.model.SearchTestModel;
import com.jichuangsi.school.testservice.model.common.PageHolder;
import com.jichuangsi.school.testservice.model.feign.SearchTestModelId;
import com.jichuangsi.school.testservice.model.statistics.*;
import com.jichuangsi.school.testservice.model.transfer.TransferKnowledge;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface IStudentTestService {

    @Transactional
    List<TestModelForStudent> getTestsList(UserInfoForToken userInfo) throws StudentTestServiceException;

    @Transactional
    PageHolder<TestModelForStudent> getHistoryTestsList(UserInfoForToken userInfo, SearchTestModel searchTestModel) throws StudentTestServiceException;

    @Transactional
    PageHolder<TestModelForStudent> getHistoryTestsListFeign(String studentId, SearchTestModelId searchTestModel) throws StudentTestServiceException;

    @Transactional
    TestModelForStudent getParticularTest(UserInfoForToken userInfo, String testId) throws StudentTestServiceException;

    @Transactional
    void saveStudentAnswer(UserInfoForToken userInfo, String testId, String questionId, AnswerModelForStudent answer) throws StudentTestServiceException;

    @Transactional
    void submitParticularTest(UserInfoForToken userInfo, String testId) throws StudentTestServiceException;


    TransferKnowledge getKnowledgeOfParticularQuestion(String questionId)throws  StudentTestServiceException;
    List<ResultKnowledgeModel> getQuestionKnowledges(List<String> questionIds) throws StudentTestServiceException;
    List<KnowledgeStatisticsModel> getParentStatistics(ParentStatisticsModel model) throws StudentTestServiceException;

    List<Test> getTestBySubjectNameAndTestName(List<String> classId,String subjectId,long time) throws StudentTestServiceException;


    TestScoreModel getTestByTestId(String testId) throws StudentTestServiceException;

    HomeworkKnoledge getTestById(String testId)throws StudentTestServiceException;


}
