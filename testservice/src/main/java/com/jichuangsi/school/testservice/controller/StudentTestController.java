package com.jichuangsi.school.testservice.controller;

import com.jichuangsi.microservice.common.model.ResponseModel;
import com.jichuangsi.microservice.common.model.UserInfoForToken;
import com.jichuangsi.school.testservice.exception.StudentTestServiceException;
import com.jichuangsi.school.testservice.model.AnswerModelForStudent;
import com.jichuangsi.school.testservice.model.TestModelForStudent;
import com.jichuangsi.school.testservice.model.SearchTestModel;
import com.jichuangsi.school.testservice.model.common.PageHolder;
import com.jichuangsi.school.testservice.service.IStudentTestService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/student")
@Api("StudentTestController相关的api")
public class StudentTestController {

    @Resource
    private IStudentTestService studentTestService;

    //获取学生习题列表
    @ApiOperation(value = "根据学生id获取习题列表信息", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String")})
    @GetMapping("/getList")
    public ResponseModel<List<TestModelForStudent>> getList(@ModelAttribute UserInfoForToken userInfo) throws StudentTestServiceException {

        return ResponseModel.sucess("", studentTestService.getTestsList(userInfo));
    }

    //获取学生历史习题列表
    @ApiOperation(value = "根据学生id获取历史习题列表信息", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String")})
    @PostMapping("/getHistory")
    public ResponseModel<PageHolder<TestModelForStudent>> getHistory(@ModelAttribute UserInfoForToken userInfo, @RequestBody SearchTestModel searchTestModel) throws StudentTestServiceException {

        return ResponseModel.sucess("", studentTestService.getHistoryTestsList(userInfo, searchTestModel));
    }

    //获取指定习题基本信息
    @ApiOperation(value = "根据习题id查询习题信息", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "path", name = "courseId", value = "课堂ID", required = true, dataType = "String")})
    @GetMapping("/getTest/{testId}")
    public ResponseModel<TestModelForStudent> getCourse(@ModelAttribute UserInfoForToken userInfo, @PathVariable String testId) throws StudentTestServiceException {
        ;
        return ResponseModel.sucess("", studentTestService.getParticularTest(userInfo, testId));
    }

    //发送学生答案
    @ApiOperation(value = "根据学生id和问题id保存学生的答案", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "path", name = "questionId", value = "问题ID", required = true, dataType = "String")})
    @PostMapping("/sendAnswer/{testId}/{questionId}")
    public ResponseModel sendAnswer(@ModelAttribute UserInfoForToken userInfo, @PathVariable String testId,
                                                      @PathVariable String questionId,
                                                      @RequestBody AnswerModelForStudent answer) throws StudentTestServiceException {
        studentTestService.saveStudentAnswer(userInfo, testId, questionId, answer);
        return ResponseModel.sucessWithEmptyData("");
    }

    //提交习题
    @ApiOperation(value = "提交习题", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "path", name = "courseId", value = "课堂ID", required = true, dataType = "String")})
    @PutMapping("/submitTest/{testId}")
    public ResponseModel submitTest(@ModelAttribute UserInfoForToken userInfo, @PathVariable String testId) throws StudentTestServiceException {
        studentTestService.submitParticularTest(userInfo, testId);
        return ResponseModel.sucessWithEmptyData("");
    }
}
