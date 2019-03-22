package com.jichuangsi.school.statistics.feign.impl;

import com.jichuangsi.microservice.common.model.ResponseModel;
import com.jichuangsi.school.statistics.feign.ICourseFeignService;
import com.jichuangsi.school.statistics.feign.model.QuestionRateModel;
import com.jichuangsi.school.statistics.feign.model.ResultKnowledgeModel;
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
}
