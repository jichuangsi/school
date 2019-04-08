package com.jichuangsi.school.homeworkservice.controller;

import com.jichuangsi.microservice.common.model.ResponseModel;
import com.jichuangsi.microservice.common.model.UserInfoForToken;
import com.jichuangsi.school.homeworkservice.exception.StudentHomeworkServiceException;
import com.jichuangsi.school.homeworkservice.model.AnswerModelForStudent;
import com.jichuangsi.school.homeworkservice.model.HomeworkModelForStudent;
import com.jichuangsi.school.homeworkservice.model.SearchHomeworkModel;
import com.jichuangsi.school.homeworkservice.model.common.PageHolder;
import com.jichuangsi.school.homeworkservice.service.IStudentHomeworkService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/student")
@Api("StudentHomeworkController相关的api")
public class StudentHomeworkController {

    @Resource
    private IStudentHomeworkService studentHomeworkService;

    //获取学生习题列表
    @ApiOperation(value = "根据学生id获取习题列表信息", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String")})
    @GetMapping("/getList")
    public ResponseModel<List<HomeworkModelForStudent>> getList(@ModelAttribute UserInfoForToken userInfo) throws StudentHomeworkServiceException {

        return ResponseModel.sucess("", studentHomeworkService.getHomeworksList(userInfo));
    }

    //获取学生历史习题列表
    @ApiOperation(value = "根据学生id获取历史习题列表信息", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String")})
    @PostMapping("/getHistory")
    public ResponseModel<PageHolder<HomeworkModelForStudent>> getHistory(@ModelAttribute UserInfoForToken userInfo, @RequestBody SearchHomeworkModel searchHomeworkModel) throws StudentHomeworkServiceException {

        return ResponseModel.sucess("", studentHomeworkService.getHistoryHomeworksList(userInfo, searchHomeworkModel));
    }

    //获取指定习题基本信息
    @ApiOperation(value = "根据习题id查询习题信息", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "path", name = "courseId", value = "课堂ID", required = true, dataType = "String")})
    @GetMapping("/getHomework/{homeworkId}")
    public ResponseModel<HomeworkModelForStudent> getCourse(@ModelAttribute UserInfoForToken userInfo, @PathVariable String homeworkId) throws StudentHomeworkServiceException {
        ;
        return ResponseModel.sucess("", studentHomeworkService.getParticularHomework(userInfo, homeworkId));
    }

    //发送学生答案
    @ApiOperation(value = "根据学生id和问题id保存学生的答案", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "path", name = "questionId", value = "问题ID", required = true, dataType = "String")})
    @PostMapping("/sendAnswer/{homeworkId}/{questionId}")
    public ResponseModel sendAnswer(@ModelAttribute UserInfoForToken userInfo, @PathVariable String homeworkId,
                                                      @PathVariable String questionId,
                                                      @RequestBody AnswerModelForStudent answer) throws StudentHomeworkServiceException {
        studentHomeworkService.saveStudentAnswer(userInfo, homeworkId, questionId, answer);
        return ResponseModel.sucessWithEmptyData("");
    }

    //提交习题
    @ApiOperation(value = "提交习题", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "path", name = "courseId", value = "课堂ID", required = true, dataType = "String")})
    @PutMapping("/submitHomework/{homeworkId}")
    public ResponseModel submitHomework(@ModelAttribute UserInfoForToken userInfo, @PathVariable String homeworkId) throws StudentHomeworkServiceException {
        studentHomeworkService.submitParticularHomework(userInfo, homeworkId);
        return ResponseModel.sucessWithEmptyData("");
    }
}
