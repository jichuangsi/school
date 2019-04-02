package com.jichuangsi.school.courseservice.service;

import com.jichuangsi.school.courseservice.Exception.FeignControllerException;
import com.jichuangsi.school.courseservice.Exception.StudentCourseServiceException;
import com.jichuangsi.school.courseservice.model.feign.QuestionRateModel;
import com.jichuangsi.school.courseservice.model.feign.classType.ClassStatisticsModel;
import com.jichuangsi.school.courseservice.model.result.ResultKnowledgeModel;
import com.jichuangsi.school.courseservice.model.transfer.TransferKnowledge;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface IFeignClientService {

    @Transactional
    TransferKnowledge getKnowledgeOfParticularQuestion(String questionId) throws StudentCourseServiceException;

    List<ResultKnowledgeModel> getStudentQuestionOnWeek(String classId, String subject) throws StudentCourseServiceException;

    double getStudentQuestionRate(QuestionRateModel model) throws FeignControllerException;

    List<ResultKnowledgeModel> getQuestionKnowledges(List<String> questionIds) throws StudentCourseServiceException;

    double getQuetsionIdsCrossByMD5(QuestionRateModel model) throws FeignControllerException;

    List<ClassStatisticsModel> getClassStatisticsByClassIdsOnMonth(List<String> ids) throws FeignControllerException;
}

