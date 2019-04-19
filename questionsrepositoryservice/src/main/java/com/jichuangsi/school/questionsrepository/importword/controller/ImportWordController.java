package com.jichuangsi.school.questionsrepository.importword.controller;

import com.jichuangsi.microservice.common.model.ResponseModel;
import com.jichuangsi.microservice.common.model.UserInfoForToken;
import com.jichuangsi.school.questionsrepository.entity.SelfQuestions;
import com.jichuangsi.school.questionsrepository.exception.QuestionRepositoryServiceException;
import com.jichuangsi.school.questionsrepository.importword.exception.IImportWordServiceException;
import com.jichuangsi.school.questionsrepository.importword.model.TitleSourceInformation;
import com.jichuangsi.school.questionsrepository.importword.service.IImportWordService;
import com.jichuangsi.school.questionsrepository.importword.util.ImportWord;
import com.jichuangsi.school.questionsrepository.model.self.SelfQuestion;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.batik.transcoder.TranscoderException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author LaiJX
 * @Date 18:17 2019/4/1
 * @Param
 * @What?
        * @return
        **/

@RestController
@RequestMapping("/importword")
@Api("操作上传的word文档的api")
@CrossOrigin
public class ImportWordController {
    @Resource
    private IImportWordService iImportWordService;



    @ApiOperation(value = "进行分解word并返回model,上传单份", notes = "")
    @RequestMapping("/open")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String")})
    public ResponseModel analyzeWord(@ModelAttribute UserInfoForToken userInfo, @RequestParam MultipartFile file) throws IImportWordServiceException, IOException, TranscoderException {
        String subjectName= file.getName();
        List<SelfQuestion> selfQuestionsList = ImportWord.breakUpWord(file);
        return ResponseModel.sucess("",selfQuestionsList);
    }
    @ApiOperation(value = "保存题目到mongodb数据库", notes = "")
    @RequestMapping("/save")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String")})
    public ResponseModel save(@ModelAttribute UserInfoForToken userInfo,@RequestParam SelfQuestion[] questions) throws QuestionRepositoryServiceException {
        for (SelfQuestion question:questions
             ) {
            iImportWordService.save(userInfo,question);
        }
        return ResponseModel.sucess("","导入成功");
    }
}
