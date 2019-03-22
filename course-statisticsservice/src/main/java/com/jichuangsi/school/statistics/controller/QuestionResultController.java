package com.jichuangsi.school.statistics.controller;

import com.jichuangsi.microservice.common.model.ResponseModel;
import com.jichuangsi.microservice.common.model.UserInfoForToken;
import com.jichuangsi.school.statistics.exception.QuestionResultException;
import com.jichuangsi.school.statistics.model.homework.HomeworkModelForTeacher;
import com.jichuangsi.school.statistics.model.result.StudentResultModel;
import com.jichuangsi.school.statistics.model.result.TeacherHomeResultModel;
import com.jichuangsi.school.statistics.model.result.TeacherResultModel;
import com.jichuangsi.school.statistics.service.IQuestionResultService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/result")
@Api("统计试题正确率")
@CrossOrigin
public class QuestionResultController {

    @Resource
    private IQuestionResultService questionResultService;

    @ApiOperation(value = "根据学生id，科目名查询学生近一周的课堂正确率", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String")
    })
    @GetMapping(value = "/student/getCourseSubjectResult")
    public ResponseModel<List<StudentResultModel>> getStudentSubjectResult(@ModelAttribute UserInfoForToken userInfo, @RequestParam("subject") String subject) {
        try {
            return ResponseModel.sucess("", questionResultService.getStudentSubjectResult(userInfo, subject));
        } catch (QuestionResultException e) {
            return ResponseModel.fail("", e.getMessage());
        }
    }

    @ApiOperation(value = "根据学生id，科目名查询学生近一周的习题正确率", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String")
    })
    @GetMapping(value = "/student/getQuestionSubjectResult")
    public ResponseModel<List<StudentResultModel>> getQuestionSubjectResult(@ModelAttribute UserInfoForToken userInfo, @RequestParam("subject") String subject) {
        try {
            return ResponseModel.sucess("", questionResultService.getStudentQuestionResult(userInfo, subject));
        } catch (QuestionResultException e) {
            return ResponseModel.fail("", e.getMessage());
        }
    }

    @ApiOperation(value = "根据老师id，科目名查询近一周的课堂正确率", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String")
    })
    @GetMapping(value = "/teacher/getCourseSubjectResult")
    public ResponseModel<TeacherResultModel> getTeacherCourseSubjectResult(@ModelAttribute UserInfoForToken userInfo, @RequestParam("classId") String classId, @RequestParam("subject") String subject) {
        try {
            return ResponseModel.sucess("", questionResultService.getTeacherCourseResult(userInfo, classId, subject));
        } catch (QuestionResultException e) {
            return ResponseModel.fail("", e.getMessage());
        }
    }

    @ApiOperation(value = "根据老师id，classId查询近一周的课下习题列表", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String")
    })
    @GetMapping(value = "/teacher/getSubjectQuestion")
    public ResponseModel<List<HomeworkModelForTeacher>> getSubjectQuestion(@ModelAttribute UserInfoForToken userInfo,@RequestParam("classId") String classId){
        try {
            return ResponseModel.sucess("",questionResultService.getSubjectQuestion(userInfo,classId));
        } catch (QuestionResultException e) {
            return ResponseModel.fail("",e.getMessage());
        }
    }

    @ApiOperation(value = "根据老师id，习题id，获得该次习题统计", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String")
    })
    @GetMapping(value = "/teacher/getSubjectQuestionRate")
    public ResponseModel<TeacherHomeResultModel> getSubjectQuestionRate(@ModelAttribute UserInfoForToken userInfo, @RequestParam("homeId") String homeId,@RequestParam("classId") String classId){
        try {
            return ResponseModel.sucess("",questionResultService.getSubjectQuestionRate(userInfo,homeId,classId));
        } catch (QuestionResultException e) {
            return ResponseModel.fail("",e.getMessage());
        }
    }
}
