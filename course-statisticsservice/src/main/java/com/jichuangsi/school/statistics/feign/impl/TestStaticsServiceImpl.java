package com.jichuangsi.school.statistics.feign.impl;


import com.jichuangsi.microservice.common.model.ResponseModel;
import com.jichuangsi.school.statistics.feign.ITestStaticsService;
import com.jichuangsi.school.statistics.model.Report.HomworkReportRateModel;
import com.jichuangsi.school.statistics.model.Report.TestModel;
import com.jichuangsi.school.statistics.model.Report.TestScoreModel;

import java.util.List;

public class TestStaticsServiceImpl implements ITestStaticsService {
    @Override
    public ResponseModel<HomworkReportRateModel> getTestById(String testId) {
        return ResponseModel.fail("");
    }

    @Override
    public ResponseModel<List<TestModel>> getTestBySubjectNameAndTestId(List<String> classId, String subjectId, long time) {
        return ResponseModel.fail("");
    }

    @Override
    public ResponseModel<TestScoreModel> getTestByTestId(String testId) {
        return ResponseModel.fail("");
    }
}
