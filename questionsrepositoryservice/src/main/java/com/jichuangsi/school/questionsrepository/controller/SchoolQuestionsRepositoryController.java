package com.jichuangsi.school.questionsrepository.controller;

import com.jichuangsi.microservice.common.model.ResponseModel;
import com.jichuangsi.microservice.common.model.UserInfoForToken;
import com.jichuangsi.school.questionsrepository.constant.ResultCode;
import com.jichuangsi.school.questionsrepository.exception.QuestionRepositoryServiceException;
import com.jichuangsi.school.questionsrepository.model.Base64TransferFile;
import com.jichuangsi.school.questionsrepository.model.PageHolder;
import com.jichuangsi.school.questionsrepository.model.common.DeleteQueryModel;
import com.jichuangsi.school.questionsrepository.model.common.QuestionFile;
import com.jichuangsi.school.questionsrepository.model.common.SearchQuestionModel;
import com.jichuangsi.school.questionsrepository.model.school.SchoolQuestion;
import com.jichuangsi.school.questionsrepository.model.transfer.TransferSchool;
import com.jichuangsi.school.questionsrepository.service.ISchoolQuestionRepositoryService;
import com.jichuangsi.school.questionsrepository.service.IUserInfoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;

@RestController
@RequestMapping("/school")
@Api("SchoolQuestionsRepositoryController相关的api")
public class SchoolQuestionsRepositoryController {

    @Resource
    private ISchoolQuestionRepositoryService schoolQuestionService;

    @Resource
    private IUserInfoService userInfoService;

    //保存自定义题目信息
    @ApiOperation(value = "保存学校题目信息", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String")})
    @PostMapping("/saveQuestion")
    public ResponseModel saveQuestion(@ModelAttribute UserInfoForToken userInfo, @RequestBody SchoolQuestion schoolQuestion) throws QuestionRepositoryServiceException {
        TransferSchool transferSchool = userInfoService.getSchoolInfoById(userInfo.getUserId());
        if(transferSchool==null) throw new QuestionRepositoryServiceException(ResultCode.SCHOOL_INFO_NOT_EXISTED);
        schoolQuestionService.addSchoolQuestion(userInfo, transferSchool,schoolQuestion);
        return ResponseModel.sucessWithEmptyData("");
    }

    //获取自定义题目列表
    @ApiOperation(value = "获取学校题目列表", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String")})
    @PostMapping("/getQuestions")
    public ResponseModel<PageHolder<SchoolQuestion>> getQuestions(@ModelAttribute UserInfoForToken userInfo, @RequestBody SearchQuestionModel searchQuestionModel) throws QuestionRepositoryServiceException {
        /*UserForTeacher teacher = teacherService.getUserForTeacherById(userInfo.getUserId());
        if(teacher==null||searchQuestionModel==null){
            throw new QuestionRepositoryServiceException(ResultCode.PARAM_MISS_MSG);
        }*/
        TransferSchool school = userInfoService.getSchoolInfoById(userInfo.getUserId());
        if(school==null) throw new QuestionRepositoryServiceException(ResultCode.SCHOOL_INFO_NOT_EXISTED);
        return ResponseModel.sucess("", schoolQuestionService.getSortSchoolQuestion(school,searchQuestionModel));
    }

    //删除指定的自定义题目
    @ApiOperation(value = "删除指定的学校题目", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String")})
    @DeleteMapping("/deleteQuestions")
    public ResponseModel deleteQuestions(@ModelAttribute UserInfoForToken userInfo, @RequestBody DeleteQueryModel deleteQueryModel) throws QuestionRepositoryServiceException {
        /* SchoolQuestions questions = schoolQuestionService.getSchoolQuestionById(deleteQueryModel.getIds().get(0));
        if(questions==null){
            throw new QuestionRepositoryServiceException(ResultCode.QUESTION_NOT_EXISTED);
        }
        schoolQuestionService.deleteSchoolQuestion(userInfo,questions);*/
        schoolQuestionService.deleteSchoolQuestions(userInfo,deleteQueryModel);
        return ResponseModel.sucessWithEmptyData("");
    }

    //自定义题目图片存根
    @ApiOperation(value = "自定义题目图片存根", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String")})
    @PostMapping("/sendQuestionPic")
    public ResponseModel<SchoolQuestion> sendQuestionPic(@RequestParam MultipartFile file, @ModelAttribute UserInfoForToken userInfo) throws QuestionRepositoryServiceException{
        /*try{
            return ResponseModel.sucess("",  teacherCourseService.uploadTeacherSubjectPic(userInfo, new CourseFile(file.getOriginalFilename(), file.getContentType(), file.getBytes())));
        }catch (IOException ioExp){
            throw new QuestionRepositoryServiceException(ResultCode.FILE_UPLOAD_ERROR);
        }*/
        try{
            return ResponseModel.sucess("",  schoolQuestionService.uploadQuestionPic(userInfo, new QuestionFile(file.getOriginalFilename(), file.getContentType(), file.getBytes())));
        }catch (IOException ioExp){
            throw new QuestionRepositoryServiceException(ResultCode.FILE_UPLOAD_ERROR);
        }
    }

    //获取指定文件名图片
    @ApiOperation(value = "根据老师id和文件名下载指定的文件", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String")})
    @PostMapping("/getQuestionPic")
    public ResponseModel<QuestionFile> getSubjectPic(@ModelAttribute UserInfoForToken userInfo, @RequestBody SchoolQuestion questionPic) throws QuestionRepositoryServiceException{
        /*Base64TransferFile base64TransferFile = new Base64TransferFile();
        QuestionFile questionFile = schoolQuestionService.downQuestionPic(userInfo,questionPic.getQuestionPic());
        base64TransferFile.setName(questionFile.getName());
        base64TransferFile.setContentType(questionFile.getContentType());
        base64TransferFile.setContent(new String(questionFile.getContent()));*/

        return  ResponseModel.sucess("", schoolQuestionService.downQuestionPic(userInfo,questionPic.getQuestionPic()));
    }

    //删除指定文件名图片
    @ApiOperation(value = "根据老师id和文件名删除指定文件", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String")})
    @DeleteMapping("/remoreQuestionPic")
    public ResponseModel<SchoolQuestion> remoreSubjectPic(@ModelAttribute UserInfoForToken userInfo, @RequestBody SchoolQuestion questionPic) throws QuestionRepositoryServiceException{
        schoolQuestionService.deleteQuestionPic(userInfo,questionPic.getQuestionPic());
        return ResponseModel.sucessWithEmptyData("");
    }
}
