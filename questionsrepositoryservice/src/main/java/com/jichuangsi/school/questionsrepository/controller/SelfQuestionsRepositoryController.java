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
import com.jichuangsi.school.questionsrepository.model.common.SendCodePic;
import com.jichuangsi.school.questionsrepository.model.self.SelfQuestion;
import com.jichuangsi.school.questionsrepository.service.ISelfQuestionsRepositoryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.Date;
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
    public ResponseModel<SelfQuestion> sendQuestionPic(@RequestParam MultipartFile file, @ModelAttribute UserInfoForToken userInfo,@RequestParam String code) throws QuestionRepositoryServiceException{
        /*try{
            return ResponseModel.sucess("",  teacherCourseService.uploadTeacherSubjectPic(userInfo, new CourseFile(file.getOriginalFilename(), file.getContentType(), file.getBytes())));
        }catch (IOException ioExp){
            throw new QuestionRepositoryServiceException(ResultCode.FILE_UPLOAD_ERROR);
        }*/
        SendCodePic sendCodePic = new SendCodePic();
        sendCodePic.setCode(code);
        sendCodePic.setTeacherId(userInfo.getUserId());
        try {
            selfQuestionsRepositoryService.uploadQuestionPic(new QuestionFile(file.getOriginalFilename(), file.getContentType(), file.getBytes()),sendCodePic);
        } catch (IOException e) {
            throw new QuestionRepositoryServiceException(ResultCode.FILE_UPLOAD_ERROR);
        }
        return ResponseModel.sucessWithEmptyData("");
    }

    //获取指定文件名图片
    @ApiOperation(value = "根据老师id和文件名下载指定的文件", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String")})
    @PostMapping("/getQuestionPic")
    public ResponseModel<Base64TransferFile> getQuestionPic(@ModelAttribute UserInfoForToken userInfo, @RequestBody SelfQuestion questionPic) throws QuestionRepositoryServiceException{
        Base64TransferFile base64TransferFile = new Base64TransferFile();
        QuestionFile questionFile =  selfQuestionsRepositoryService.downQuestionPic(userInfo,questionPic.getQuestionPic());
        base64TransferFile.setName(questionFile.getName());
        base64TransferFile.setContentType(questionFile.getContentType());
        base64TransferFile.setContent(new String(questionFile.getContent()));

        return  ResponseModel.sucess("", base64TransferFile);
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

    //二维码上传指定文件名图片
    @ApiOperation(value = "二维码上传指定文件名图片", notes = "")
    @ApiImplicitParams({})
    @PostMapping("/codeSendQuestionPic")
    public ResponseModel codeSendQuestionPic(@RequestParam MultipartFile file, @RequestParam String code,@RequestParam String teacherId,@RequestParam long safeTime) throws QuestionRepositoryServiceException{
        System.out.println("code:"+code+",teacherId："+teacherId+",safeTime"+safeTime);
        if(safeTime!=0 && new Date().getTime()<safeTime){
            System.out.println("==1==");
            SendCodePic sendCodePic = new SendCodePic();
            sendCodePic.setTeacherId(teacherId);
            sendCodePic.setCode(code);
            try {
                System.out.println("==2==");
                selfQuestionsRepositoryService.uploadQuestionPic(new QuestionFile(file.getOriginalFilename(), file.getContentType(), file.getBytes()),sendCodePic);
                System.out.println("==7==");
            } catch (IOException e) {
                throw new QuestionRepositoryServiceException(ResultCode.FILE_UPLOAD_ERROR);
            }
        }else{
            throw new QuestionRepositoryServiceException(ResultCode.OVER_SALE_TIME);
        }
        System.out.println("==8==");
        return ResponseModel.sucessWithEmptyData("");
    }
}
