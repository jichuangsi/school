package com.jichuangsi.school.statistics.service;

import com.jichuangsi.microservice.common.model.ResponseModel;
import com.jichuangsi.microservice.common.model.UserInfoForToken;
import com.jichuangsi.school.statistics.model.Report.*;
import com.jichuangsi.school.statistics.exception.SchoolReportException;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

public interface ISchoolReportService {

    ResponseModel<List<Grade>> getGradeBySchoolId(String schoolId);

    List<StudentTestScoreModel> getSchoolTestReport(UserInfoForToken userInfo,StudentTestModel studentTestModel) throws SchoolReportException;
    List<StudentTestScoreModel> getSchoolHomeworkReport(UserInfoForToken userInfo,StudentTestModel studentTestModel) throws SchoolReportException;

    HomworkReportRateModel getHomeReportKnowledgeRate(String homeworkId) throws SchoolReportException;
    HomworkReportRateModel getCourseReportKnowledgeRate(String courseId) throws SchoolReportException;
    HomworkReportRateModel getTestReportKnowledgeRate(String testId) throws SchoolReportException;



}
