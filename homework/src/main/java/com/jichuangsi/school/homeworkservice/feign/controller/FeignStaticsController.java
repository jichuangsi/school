package com.jichuangsi.school.homeworkservice.feign.controller;

import com.jichuangsi.microservice.common.model.ResponseModel;
import com.jichuangsi.school.homeworkservice.entity.Homework;
import com.jichuangsi.school.homeworkservice.entity.Question;
import com.jichuangsi.school.homeworkservice.entity.StudentAnswer;
import com.jichuangsi.school.homeworkservice.feign.service.FeignStaticsService;
import com.jichuangsi.school.homeworkservice.model.Report.HomeworkKnoledge;
import com.jichuangsi.school.homeworkservice.model.Report.TestScoreModel;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;
import com.jichuangsi.school.homeworkservice.entity.StudentHomeworkCollection;

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
    public Question getQuestion(@RequestBody String questionIds){

        return feignStaticsService.getQuestion(questionIds);
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

    //根据习题id查科目
    @PostMapping(value="/getSubjectIdByHomeworkId")
    public ResponseModel<Homework> getSubjectIdByHomeworkId(@RequestParam("homeworkId") String homeworkId,@RequestParam("classId")  String classId){

        return ResponseModel.sucess("",feignStaticsService.getSubjectIdByHomeworkId(homeworkId,classId));
    }
    //根据习题id查分数
    @PostMapping(value="/getTotalScoreByHomeworkId")
    public ResponseModel<List<StudentHomeworkCollection>> getTotalScoreByHomeworkId(@RequestParam("homeworkId") String homeworkId){

        return ResponseModel.sucess("",feignStaticsService.getTotalScoreByHomeworkId(homeworkId));
    }



    //根据习题id
    @PostMapping(value="/getHomeworkByHomeworkId")
    public ResponseModel<HomeworkKnoledge> getHomeworkByHomeworkId(@RequestParam("homeworkId") String homeworkId){

        return ResponseModel.sucess("",feignStaticsService.getHomeworkByHomeworkId(homeworkId));
    }
    @ApiOperation(value = "校长查询班级考试统计", notes = "")
    @ApiImplicitParams({})
    @PostMapping("/getHomeworkBySubjectNameAndHomeworkId")
    public ResponseModel<List<Homework>> getHomeworkBySubjectNameAndHomeworkId(@RequestParam("classId") List<String> classId, @RequestParam("subjectId")String subjectId, @RequestParam("time")long time){

        return ResponseModel.sucess("",feignStaticsService.getHomeworkBySubjectNameAndHomeworkId(classId, subjectId, time));
    }
    @ApiOperation(value = "根据考试id查询考试详情", notes = "")
    @ApiImplicitParams({})
    @PostMapping("/getHomeworkById")
    public  ResponseModel<TestScoreModel> getHomeworkById(@RequestParam("HomeworkId") String HomeworkId){

        return ResponseModel.sucess("",feignStaticsService.getHomeworkById(HomeworkId));
    }
}
