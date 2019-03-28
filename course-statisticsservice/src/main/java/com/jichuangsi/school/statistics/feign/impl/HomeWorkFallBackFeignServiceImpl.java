package com.jichuangsi.school.statistics.feign.impl;

import com.jichuangsi.microservice.common.model.ResponseModel;
import com.jichuangsi.school.statistics.feign.IHomeWorkFeignService;
import com.jichuangsi.school.statistics.feign.model.HomeWorkRateModel;
import com.jichuangsi.school.statistics.feign.model.QuestionRateModel;
import com.jichuangsi.school.statistics.model.homework.HomeworkModelForTeacher;
import com.jichuangsi.school.statistics.model.result.TeacherHomeResultModel;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HomeWorkFallBackFeignServiceImpl implements IHomeWorkFeignService {

    @Override
    public ResponseModel<List<HomeWorkRateModel>> getStudentQuestionOnWeek(String studentId, String subject) {
        return ResponseModel.fail("");
    }

    @Override
    public ResponseModel<Double> getStudentQuestionRate(QuestionRateModel model) {
        return ResponseModel.fail("");
    }

    @Override
    public ResponseModel<Double> getStudentQuestionClassRate(List<String> questionIds) {
        return ResponseModel.fail("");
    }

    @Override
    public ResponseModel<List<HomeworkModelForTeacher>> getHomeWorkByTeacherIdAndclassId(String teacherId, String classId) {
        return ResponseModel.fail("");
    }

    @Override
    public ResponseModel<TeacherHomeResultModel> getHomeWorkRate(String teacherId, String homeId) {
        return ResponseModel.fail("");
    }
}
