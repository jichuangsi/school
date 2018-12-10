package com.jichuangsi.school.questionsrepository.controller;

import com.jichuangsi.microservice.common.model.ResponseModel;
import com.jichuangsi.microservice.common.model.UserInfoForToken;
import com.jichuangsi.school.questionsrepository.constant.ResultCode;
import com.jichuangsi.school.questionsrepository.exception.QuestionRepositoryServiceException;
import com.jichuangsi.school.questionsrepository.model.PageHolder;
import com.jichuangsi.school.questionsrepository.model.common.DeleteQueryModel;
import com.jichuangsi.school.questionsrepository.model.common.QuestionFile;
import com.jichuangsi.school.questionsrepository.model.common.SearchQuestionModel;
import com.jichuangsi.school.questionsrepository.model.self.SelfQuestion;
import com.jichuangsi.school.questionsrepository.service.ISelfQuestionsRepositoryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/self")
@Api("SelfQuestionRepositoryController相关的api")
public class SelfQuestionsRepositoryController {

    @Resource
    private ISelfQuestionsRepositoryService selfQuestionsRepositoryService;

    //保存自定义题目信息
    @ApiOperation(value = "保存自定义题目信息", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String")})
    @PostMapping("/saveQuestion")
    public ResponseModel<SelfQuestion> saveQuestion(@ModelAttribute UserInfoForToken userInfo, @RequestBody SelfQuestion selfSelfQuestion) throws QuestionRepositoryServiceException {
        selfQuestionsRepositoryService.addSelfQuestion(userInfo,selfSelfQuestion);
        return ResponseModel.sucessWithEmptyData("");
    }

    //获取自定义题目列表
    @ApiOperation(value = "获取自定义题目列表", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String")})
    @PostMapping("/getQuestions")
    public ResponseModel<PageHolder<SelfQuestion>> getQuestions(@ModelAttribute UserInfoForToken userInfo, @RequestBody SearchQuestionModel searchQuestion) throws QuestionRepositoryServiceException {

        return ResponseModel.sucess("",selfQuestionsRepositoryService.getSelfQuestionSortList(userInfo,searchQuestion)  );
    }

    //删除指定的自定义题目
    @ApiOperation(value = "删除指定的自定义题目", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String")})
    @DeleteMapping("/deleteQuestions")
    public ResponseModel<List<SelfQuestion>> deleteQuestions(@ModelAttribute UserInfoForToken userInfo, @RequestBody DeleteQueryModel deleteQueryModel) throws QuestionRepositoryServiceException {
        /*SelfQuestions selfQuestions = selfQuestionsRepositoryService.getSelfQuestionsById(deleteQueryModel.getIds().get(0));
        if(selfQuestions==null){
            throw new QuestionRepositoryServiceException(ResultCode.QUESTION_NOT_EXISTED);
        }
        selfQuestionsRepositoryService.deleteSelfQuestion(userInfo,selfQuestions);*/
        selfQuestionsRepositoryService.deleteSelfQuestions(userInfo,deleteQueryModel);
        return ResponseModel.sucessWithEmptyData("");
    }

    //自定义题目图片存根
    @ApiOperation(value = "自定义题目图片存根", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String")})
    @PostMapping("/sendQuestionPic")
    public ResponseModel<SelfQuestion> sendQuestionPic(@RequestParam MultipartFile file, @ModelAttribute UserInfoForToken userInfo) throws QuestionRepositoryServiceException{
        /*try{
            return ResponseModel.sucess("",  teacherCourseService.uploadTeacherSubjectPic(userInfo, new CourseFile(file.getOriginalFilename(), file.getContentType(), file.getBytes())));
        }catch (IOException ioExp){
            throw new QuestionRepositoryServiceException(ResultCode.FILE_UPLOAD_ERROR);
        }*/
        try{
            return ResponseModel.sucess("",  selfQuestionsRepositoryService.uploadQuestionPic(userInfo, new QuestionFile(file.getOriginalFilename(), file.getContentType(), file.getBytes())));
        }catch (IOException ioExp){
            throw new QuestionRepositoryServiceException(ResultCode.FILE_UPLOAD_ERROR);
        }
    }

    //获取指定文件名图片
    @ApiOperation(value = "根据老师id和文件名下载指定的文件", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String")})
    @PostMapping("/getQuestionPic")
    public ResponseModel<QuestionFile> getQuestionPic(@ModelAttribute UserInfoForToken userInfo, @RequestBody SelfQuestion questionPic) throws QuestionRepositoryServiceException{

        return  ResponseModel.sucess("", selfQuestionsRepositoryService.downQuestionPic(userInfo,questionPic.getQuestionPic()));
    }

    //删除指定文件名图片
    @ApiOperation(value = "根据老师id和文件名删除指定文件", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String")})
    @DeleteMapping("/remoreQuestionPic")
    public ResponseModel<SelfQuestion> remoreQuestionPic(@ModelAttribute UserInfoForToken userInfo, @RequestBody SelfQuestion questionPic) throws QuestionRepositoryServiceException{
        selfQuestionsRepositoryService.deleteQuestionPic(userInfo,questionPic.getQuestionPic());
        return ResponseModel.sucessWithEmptyData("");
    }
}
