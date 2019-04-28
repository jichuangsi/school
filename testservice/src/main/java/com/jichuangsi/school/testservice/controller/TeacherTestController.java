package com.jichuangsi.school.testservice.controller;

import com.jichuangsi.microservice.common.model.ResponseModel;
import com.jichuangsi.microservice.common.model.UserInfoForToken;
import com.jichuangsi.school.testservice.exception.TeacherTestServiceException;
import com.jichuangsi.school.testservice.model.*;
import com.jichuangsi.school.testservice.model.common.PageHolder;
import com.jichuangsi.school.testservice.service.ITeacherTestService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/teacher")
@Api("TeacherTestController相关的api")
public class TeacherTestController {

    @Resource
    private ITeacherTestService teacherTestService;

    //获取老师试卷列表
    @ApiOperation(value = "根据老师id获取试卷列表信息", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String")})
    @GetMapping("/getList")
    public ResponseModel<List<TestModelForTeacher>> getList(@ModelAttribute UserInfoForToken userInfo) throws TeacherTestServiceException {

        return ResponseModel.sucess("",  teacherTestService.getTestsList(userInfo));
    }

    //获取老师历史试卷列表
    @ApiOperation(value = "根据用户id获取老师历史试卷列表信息", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String")})
    @PostMapping("/getHistory")
    public ResponseModel<PageHolder<TestModelForTeacher>> getHistory(@ModelAttribute UserInfoForToken userInfo,
                                                                         @RequestBody SearchTestModel searchTestModel) throws TeacherTestServiceException{

        return ResponseModel.sucess("",  teacherTestService.getHistoryTestsList(userInfo, searchTestModel));
    }

    //获取指定试卷
    @ApiOperation(value = "根据习题id查询试卷信息", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "path", name = "courseId", value = "课堂ID", required = true, dataType = "String") })
    @GetMapping("/getTest/{testId}")
    public ResponseModel<TestModelForTeacher> getTest(@ModelAttribute UserInfoForToken userInfo, @PathVariable String testId) throws TeacherTestServiceException{

        return ResponseModel.sucess("",  teacherTestService.getParticularTest(userInfo, testId));
    }

    //获取指定学生作业
    @ApiOperation(value = "根据习题id查询习题信息", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "path", name = "homeworkId", value = "课堂ID", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "path", name = "studentId", value = "学生ID", required = true, dataType = "String")})
    @GetMapping("/getStudentHomework/{homeworkId}/{studentId}")
    public ResponseModel<TestModelForStudent> getStudentTest(@ModelAttribute UserInfoForToken userInfo, @PathVariable String testId, @PathVariable String studentId) throws TeacherTestServiceException{

        return ResponseModel.sucess("",  teacherTestService.getParticularStudentTest(userInfo, testId, studentId));
    }

    //获取指定试卷题目
    @ApiOperation(value = "根据问题id查询问题信息", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "path", name = "questionId", value = "问题ID", required = true, dataType = "String") })
    @GetMapping("/getQuestion/{questionId}")
    public ResponseModel<QuestionModelForTeacher> getQuestion(@ModelAttribute UserInfoForToken userInfo, @PathVariable String questionId) throws TeacherTestServiceException{

        return ResponseModel.sucess("",  teacherTestService.getParticularQuestion(userInfo, questionId));
    }

    //获取指定学生答案
    @ApiOperation(value = "根据问题id和学生id查询答案信息", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "path", name = "questionId", value = "问题ID", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "path", name = "studentId", value = "学生ID", required = true, dataType = "String") })
    @GetMapping("/getAnswer/{questionId}/{studentId}")
    public ResponseModel<AnswerModelForStudent> getAnswer(@ModelAttribute UserInfoForToken userInfo, @PathVariable String questionId,
                                                          @PathVariable String studentId) throws TeacherTestServiceException{

        return ResponseModel.sucess("",  teacherTestService.getParticularAnswer(questionId, studentId));
    }

    //发送老师批改
    @ApiOperation(value = "根据老师id，问题id和学生答案id保存老师的批改", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "path", name = "questionId", value = "问题ID", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "path", name = "studentAnswerId", value = "学生答案ID", required = true, dataType = "String") })
    @PostMapping("/sendAnswer/{questionId}/{studentAnswerId}")
    public ResponseModel sendAnswer(@ModelAttribute UserInfoForToken userInfo,
                                                      @PathVariable String questionId,  @PathVariable String studentAnswerId,
                                                      @RequestBody AnswerModelForTeacher revise) throws TeacherTestServiceException{
        teacherTestService.saveTeacherAnswer(userInfo, questionId, studentAnswerId, revise);
        return ResponseModel.sucessWithEmptyData("");
    }

    //更新问题状态
    @ApiOperation(value = "根据习题id更新问题状态", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String")})
    @PostMapping("/updateTestStatus")
    public ResponseModel updateTestStatus(@ModelAttribute UserInfoForToken userInfo, @RequestBody TestModelForTeacher testModelForTeacher) throws TeacherTestServiceException{
        teacherTestService.updateParticularTestStatus(userInfo, testModelForTeacher);
        return ResponseModel.sucessWithEmptyData("");
    }
}
