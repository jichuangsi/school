package com.jichuangsi.school.statistics.feign.impl;

import com.jichuangsi.microservice.common.model.ResponseModel;
import com.jichuangsi.school.statistics.feign.IHomeWorkStaticsService;
import com.jichuangsi.school.statistics.model.SearchCapabilityModel;
import com.jichuangsi.school.statistics.model.StudentQuestionIdsModel;

import java.util.List;

public class HomeWorkStaticsServiceImpl implements IHomeWorkStaticsService {

    @Override
    public ResponseModel<List<SearchCapabilityModel>> getQuestionBySubjectId(String subjectId) {
        return ResponseModel.fail("");
    }

    @Override
    public ResponseModel<List<StudentQuestionIdsModel>> getQuestionResult(List<String> questionId) {
        return ResponseModel.fail("");
    }

    @Override
    public ResponseModel<List<SearchCapabilityModel>> getQuestion(List<String> questionId) {
        return ResponseModel.fail("");
    }

    @Override
    public ResponseModel<List<StudentQuestionIdsModel>> getQuestionByStudent(String studentId) {
        return ResponseModel.fail("");
    }

    @Override
    public ResponseModel<SearchCapabilityModel> getQuestionKnowedges(String questionId) {
        return ResponseModel.fail("");
    }
}
