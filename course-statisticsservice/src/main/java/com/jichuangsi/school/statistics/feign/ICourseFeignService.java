package com.jichuangsi.school.statistics.feign;

import com.jichuangsi.microservice.common.model.ResponseModel;
import com.jichuangsi.school.statistics.feign.impl.CourseFallBackFeignServiceImpl;
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
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(value = "courseservice",fallback = CourseFallBackFeignServiceImpl.class)
public interface ICourseFeignService {

    @RequestMapping("/feign/getCourseQuestionOnWeek")
    ResponseModel<List<ResultKnowledgeModel>> getCourseQuestionOnWeek(@RequestParam("classId") String classId, @RequestParam("subject") String subject);

    @RequestMapping("/feign/getStudentQuestionRate")
    ResponseModel<Double> getStudentQuestionRate(@RequestBody QuestionRateModel model);

    @RequestMapping("/feign/getQuestionKnowledges")
    ResponseModel<List<ResultKnowledgeModel>> getQuestionKnowledges(@RequestBody List<String> questionIds);

    @RequestMapping("/feign/getQuetsionIdsCrossByMD5")
    ResponseModel<Double> getQuetsionIdsCrossByMD5(@RequestBody QuestionRateModel model);

    @RequestMapping("/feign/getClassStatisticsByClassIdsOnMonth")
    ResponseModel<List<ClassStatisticsModel>> getClassStatisticsByClassIdsOnMonth(List<ClassDetailModel> classIds);

    @RequestMapping("/feign/getStudentKnowledges")
    ResponseModel<List<StudentKnowledgeModel>> getStudentKnowledges(@RequestBody SearchStudentKnowledgeModel model);

    @PostMapping(value="/feign/getCourseByCourseId")
    ResponseModel<HomworkReportRateModel> getCourseByCourseId(@RequestParam("courseId") String courseId);

}
