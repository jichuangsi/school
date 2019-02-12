package com.jichuangsi.school.homeworkservice.controller;

import com.jichuangsi.microservice.common.model.ResponseModel;
import com.jichuangsi.microservice.common.model.UserInfoForToken;
import com.jichuangsi.school.homeworkservice.exception.TeacherHomeworkServiceException;
import com.jichuangsi.school.homeworkservice.model.*;
import com.jichuangsi.school.homeworkservice.model.common.PageHolder;
import com.jichuangsi.school.homeworkservice.service.ITeacherHomeworkService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/teacher")
@Api("TeacherHomeworkController相关的api")
public class TeacherHomeworkController {

    @Resource
    private ITeacherHomeworkService teacherHomeworkService;

    //获取老师课堂列表
    @ApiOperation(value = "根据老师id获取习题列表信息", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String")})
    @GetMapping("/getList")
    public ResponseModel<List<HomeworkModelForTeacher>> getList(@ModelAttribute UserInfoForToken userInfo) throws TeacherHomeworkServiceException {

        return ResponseModel.sucess("",  teacherHomeworkService.getHomeworksList(userInfo));
    }

    //获取老师历史课堂列表
    @ApiOperation(value = "根据用户id获取老师历史习题列表信息", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String")})
    @PostMapping("/getHistory")
    public ResponseModel<PageHolder<HomeworkModelForTeacher>> getHistory(@ModelAttribute UserInfoForToken userInfo,
                                                                         @RequestBody SearchHomeworkModel searchHomeworkModel) throws TeacherHomeworkServiceException{

        return ResponseModel.sucess("",  teacherHomeworkService.getHistoryHomeworksList(userInfo, searchHomeworkModel));
    }

    //获取指定课堂
    @ApiOperation(value = "根据习题id查询习题信息", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "path", name = "courseId", value = "课堂ID", required = true, dataType = "String") })
    @GetMapping("/getHomework/{homeworkId}")
    public ResponseModel<HomeworkModelForTeacher> getCourse(@ModelAttribute UserInfoForToken userInfo, @PathVariable String homeworkId) throws TeacherHomeworkServiceException{

        return ResponseModel.sucess("",  teacherHomeworkService.getParticularHomework(userInfo, homeworkId));
    }

    //获取指定课堂题目
    @ApiOperation(value = "根据问题id查询问题信息", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "path", name = "questionId", value = "问题ID", required = true, dataType = "String") })
    @GetMapping("/getQuestion/{questionId}")
    public ResponseModel<QuestionModelForTeacher> getQuestion(@ModelAttribute UserInfoForToken userInfo, @PathVariable String questionId) throws TeacherHomeworkServiceException{

        return ResponseModel.sucess("",  teacherHomeworkService.getParticularQuestion(userInfo, questionId));
    }

    //获取指定学生答案
    @ApiOperation(value = "根据问题id和学生id查询答案信息", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "path", name = "questionId", value = "问题ID", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "path", name = "studentId", value = "学生ID", required = true, dataType = "String") })
    @GetMapping("/getAnswer/{questionId}/{studentId}")
    public ResponseModel<AnswerModelForStudent> getAnswer(@ModelAttribute UserInfoForToken userInfo, @PathVariable String questionId,
                                                          @PathVariable String studentId) throws TeacherHomeworkServiceException{

        return ResponseModel.sucess("",  teacherHomeworkService.getParticularAnswer(questionId, studentId));
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
                                                      @RequestBody AnswerModelForTeacher revise) throws TeacherHomeworkServiceException{
        teacherHomeworkService.saveTeacherAnswer(userInfo, questionId, studentAnswerId, revise);
        return ResponseModel.sucessWithEmptyData("");
    }

    //更新问题状态
    @ApiOperation(value = "根据习题id更新问题状态", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String")})
    @PostMapping("/updateHomeworkStatus")
    public ResponseModel updateHomeworkStatus(@ModelAttribute UserInfoForToken userInfo, @RequestBody HomeworkModelForTeacher homeworkModelForTeacher) throws TeacherHomeworkServiceException{
        teacherHomeworkService.updateParticularHomeworkStatus(userInfo, homeworkModelForTeacher);
        return ResponseModel.sucessWithEmptyData("");
    }
}
