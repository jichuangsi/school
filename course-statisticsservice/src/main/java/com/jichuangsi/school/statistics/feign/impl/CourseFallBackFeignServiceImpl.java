package com.jichuangsi.school.statistics.feign.impl;

import com.jichuangsi.microservice.common.model.ResponseModel;
import com.jichuangsi.school.statistics.feign.ICourseFeignService;
import com.jichuangsi.school.statistics.feign.model.ClassDetailModel;
import com.jichuangsi.school.statistics.feign.model.QuestionRateModel;
import com.jichuangsi.school.statistics.feign.model.ResultKnowledgeModel;
import com.jichuangsi.school.statistics.model.Report.HomworkReportRateModel;
import com.jichuangsi.school.statistics.model.Report.StudentTestModel;
import com.jichuangsi.school.statistics.model.Report.TestModel;
import com.jichuangsi.school.statistics.model.Report.TestScoreModel;
import com.jichuangsi.school.statistics.model.classType.ClassStatisticsModel;
import com.jichuangsi.school.statistics.model.classType.SearchStudentKnowledgeModel;
import com.jichuangsi.school.statistics.model.classType.StudentKnowledgeModel;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CourseFallBackFeignServiceImpl implements ICourseFeignService {

    @Override
    public ResponseModel<List<ResultKnowledgeModel>> getCourseQuestionOnWeek(String classId, String subject) {
        return ResponseModel.fail("");
    }

    @Override
    public ResponseModel<Double> getStudentQuestionRate(QuestionRateModel model) {
        return ResponseModel.fail("");
    }

    @Override
    public ResponseModel<List<ResultKnowledgeModel>> getQuestionKnowledges(List<String> questionIds) {
        return ResponseModel.fail("");
    }

    @Override
    public ResponseModel<Double> getQuetsionIdsCrossByMD5(QuestionRateModel model) {
        return ResponseModel.fail("");
    }

    @Override
    public ResponseModel<List<ClassStatisticsModel>> getClassStatisticsByClassIdsOnMonth(List<ClassDetailModel> classIds) {
        return ResponseModel.fail("");
    }

    @Override
    public ResponseModel<List<StudentKnowledgeModel>> getStudentKnowledges(SearchStudentKnowledgeModel model) {
        return ResponseModel.fail("");
    }

    @Override
    public ResponseModel<HomworkReportRateModel> getCourseByCourseId(String courseId) {
        return ResponseModel.fail("");
    }

}
