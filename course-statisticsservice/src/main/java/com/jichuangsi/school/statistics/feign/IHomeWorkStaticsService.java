package com.jichuangsi.school.statistics.feign;

import com.jichuangsi.microservice.common.model.ResponseModel;
import com.jichuangsi.school.statistics.feign.impl.HomeWorkStaticsServiceImpl;
import com.jichuangsi.school.statistics.model.SearchCapabilityModel;
import com.jichuangsi.school.statistics.model.StudentQuestionIdsModel;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

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
}