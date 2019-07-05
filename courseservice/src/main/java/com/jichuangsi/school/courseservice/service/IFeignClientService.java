package com.jichuangsi.school.courseservice.service;

import com.jichuangsi.school.courseservice.Exception.FeignControllerException;
import com.jichuangsi.school.courseservice.Exception.StudentCourseServiceException;
import com.jichuangsi.school.courseservice.entity.Course;
import com.jichuangsi.school.courseservice.model.feign.report.HomeworkKnoledge;
import com.jichuangsi.school.courseservice.model.feign.QuestionRateModel;
import com.jichuangsi.school.courseservice.model.feign.classType.ClassDetailModel;
import com.jichuangsi.school.courseservice.model.feign.classType.ClassStatisticsModel;
import com.jichuangsi.school.courseservice.model.feign.classType.SearchStudentKnowledgeModel;
import com.jichuangsi.school.courseservice.model.feign.classType.StudentKnowledgeModel;
import com.jichuangsi.school.courseservice.model.feign.report.IdAndNameModel;
import com.jichuangsi.school.courseservice.model.feign.report.TestScoreModel;
import com.jichuangsi.school.courseservice.model.feign.statistics.KnowledgeStatisticsModel;
import com.jichuangsi.school.courseservice.model.feign.statistics.ParentStatisticsModel;
import com.jichuangsi.school.courseservice.model.result.ResultKnowledgeModel;
import com.jichuangsi.school.courseservice.model.transfer.TransferKnowledge;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

public interface IFeignClientService {

    @Transactional
    TransferKnowledge getKnowledgeOfParticularQuestion(String questionId) throws StudentCourseServiceException;

    List<ResultKnowledgeModel> getStudentQuestionOnWeek(String classId, String subject) throws StudentCourseServiceException;

    double getStudentQuestionRate(QuestionRateModel model) throws FeignControllerException;

    List<ResultKnowledgeModel> getQuestionKnowledges(List<String> questionIds) throws StudentCourseServiceException;

    double getQuetsionIdsCrossByMD5(QuestionRateModel model) throws FeignControllerException;

    List<ClassStatisticsModel> getClassStatisticsByClassIdsOnMonth(List<ClassDetailModel> classModels) throws FeignControllerException;

    List<StudentKnowledgeModel> getStudentKnowledges(SearchStudentKnowledgeModel model) throws FeignControllerException;

    List<KnowledgeStatisticsModel> getParentStatistics(ParentStatisticsModel model) throws FeignControllerException;


    HomeworkKnoledge getCourseByCourseId(String courseId) throws FeignControllerException;

}

