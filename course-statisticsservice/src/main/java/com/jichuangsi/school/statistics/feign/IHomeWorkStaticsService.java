package com.jichuangsi.school.statistics.feign;

import com.jichuangsi.microservice.common.model.ResponseModel;
import com.jichuangsi.school.statistics.feign.impl.HomeWorkStaticsServiceImpl;
import com.jichuangsi.school.statistics.model.Report.*;
import com.jichuangsi.school.statistics.model.SearchCapabilityModel;
import com.jichuangsi.school.statistics.model.StudentQuestionIdsModel;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(value = "homeworkservice",fallback = HomeWorkStaticsServiceImpl.class)
public interface IHomeWorkStaticsService {
    @PostMapping("/feign/getQuestionBySubjectId")
    ResponseModel<List<SearchCapabilityModel>> getQuestionBySubjectId(@RequestBody String subjectId);
    @PostMapping("/feign/getQuestion")
    SearchCapabilityModel getQuestion(@RequestBody String questionId);
    @PostMapping("/feign/getQuestionResult")
    ResponseModel<List<StudentQuestionIdsModel>> getQuestionResult(@RequestBody List<String> questionId);

    @PostMapping("/feign/getQuestionByStudent")
    ResponseModel<List<StudentQuestionIdsModel>>getQuestionByStudent(@RequestBody String studentId);
    @PostMapping("/feign/getQuestionKnowedges")
    ResponseModel<SearchCapabilityModel> getQuestionKnowedges(@RequestBody String questionId);

    @PostMapping(value="/feign/getSubjectIdByHomeworkId")
    ResponseModel<Homework> getSubjectIdByHomeworkId(@RequestParam("homeworkId") String homeworkId,@RequestParam("classId")  String classId);
    @PostMapping(value="/feign/getTotalScoreByHomeworkId")
    ResponseModel<List<StudentHomeworkModel>> getTotalScoreByHomeworkId(@RequestParam("homeworkId") String homeworkId);

    @PostMapping(value="/feign/getHomeworkByHomeworkId")
    ResponseModel<HomworkReportRateModel> getHomeworkByHomeworkId(@RequestParam("homeworkId") String homeworkId);

    @PostMapping("/feign/getHomeworkBySubjectNameAndHomeworkId")
    ResponseModel<List<TestModel>> getHomeworkBySubjectNameAndHomeworkId(@RequestParam("classId") List<String> classId, @RequestParam("subjectId")String subjectId, @RequestParam("time")long time);

    @PostMapping("/feign/getHomeworkById")
    ResponseModel<TestScoreModel> getHomeworkById(@RequestParam("HomeworkId") String HomeworkId);

}