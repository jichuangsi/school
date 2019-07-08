package com.jichuangsi.school.statistics.feign.impl;

import com.jichuangsi.microservice.common.model.ResponseModel;
import com.jichuangsi.school.statistics.feign.IHomeWorkStaticsService;
import com.jichuangsi.school.statistics.model.Report.*;
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
    public SearchCapabilityModel getQuestion(String questionId) {
        return null;
    }

    @Override
    public ResponseModel<List<StudentQuestionIdsModel>> getQuestionByStudent(String studentId) {
        return ResponseModel.fail("");
    }

    @Override
    public ResponseModel<SearchCapabilityModel> getQuestionKnowedges(String questionId) {
        return ResponseModel.fail("");
    }

    @Override
    public ResponseModel<List<StudentHomeworkModel>> getTotalScoreByHomeworkId(String homeworkId) {
        return null;
    }

    @Override
    public ResponseModel<Homework> getSubjectIdByHomeworkId(String homeworkId, String classId) {
        return null;
    }

    @Override
    public ResponseModel<HomworkReportRateModel> getHomeworkByHomeworkId(String homeworkId) {
        return ResponseModel.fail("");
    }

    @Override
    public ResponseModel<List<TestModel>> getHomeworkBySubjectNameAndHomeworkId(List<String> classId, String subjectId, long time) {
        return null;
    }

    @Override
    public ResponseModel<TestScoreModel> getHomeworkById(String HomeworkId) {
        return  ResponseModel.fail("");
    }
}
