package com.jichuangsi.school.statistics.feign;

import com.jichuangsi.microservice.common.model.ResponseModel;
import com.jichuangsi.school.statistics.feign.impl.TestStaticsServiceImpl;
import com.jichuangsi.school.statistics.model.Report.HomworkReportRateModel;
import com.jichuangsi.school.statistics.model.Report.TestModel;
import com.jichuangsi.school.statistics.model.Report.StudentTestModel;
import com.jichuangsi.school.statistics.model.Report.TestScoreModel;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(value = "testservice",fallback = TestStaticsServiceImpl.class)
public interface ITestStaticsService {
    @PostMapping("/Feign/getTestBySubjectNameAndTestId")
    ResponseModel<List<TestModel>> getTestBySubjectNameAndTestId(@RequestParam("classId") List<String> classId,@RequestParam("subjectId")String subjectId,@RequestParam("time")long time);

    @PostMapping("/Feign/getTestByTestId")
    ResponseModel<TestScoreModel> getTestByTestId(@RequestParam("testId") String testId);

    @PostMapping(value="/Feign/getTestById")
    ResponseModel<HomworkReportRateModel> getTestById(@RequestParam("testId") String testId);

}
