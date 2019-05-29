package com.jichuangsi.school.homeworkservice.feign.controller;

import com.jichuangsi.microservice.common.model.ResponseModel;
import com.jichuangsi.school.homeworkservice.entity.Question;
import com.jichuangsi.school.homeworkservice.entity.StudentAnswer;
import com.jichuangsi.school.homeworkservice.feign.service.FeignStaticsService;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/feign")
@Api("FeignStaticsController")
public class FeignStaticsController {
    @Resource
    private FeignStaticsService feignStaticsService;
    @PostMapping(value="/getQuestionBySubjectId")
    public ResponseModel<List<Question>> getQuestionBySubjectId(@RequestBody String subjectId){
        return ResponseModel.sucess("",feignStaticsService.getQuestionBySubjectId(subjectId));
    }
    @PostMapping(value="/getQuestion")
    public ResponseModel<List<Question>> getQuestion(@RequestBody List<String> questionIds){

        return ResponseModel.sucess("",feignStaticsService.getQuestion(questionIds));
    }

    @PostMapping(value="/getQuestionResult")
    public ResponseModel<List<StudentAnswer>> getQuestionResult(@RequestBody List<String> questionIds){

        return ResponseModel.sucess("",feignStaticsService.getQuestionResult(questionIds));
    }
    @PostMapping(value="/getQuestionByStudent")
    public ResponseModel<List<StudentAnswer>> getQuestionByStudent(@RequestBody String studentId){

        return ResponseModel.sucess("",feignStaticsService.getQuestionByStudentId(studentId));
    }
    @PostMapping(value="/getQuestionKnowedges")
    public ResponseModel<Question> getQuestionKnowledges(@RequestBody String questionId){
        return ResponseModel.sucess("",feignStaticsService.getQuestionKnowledges(questionId));
    }

}
