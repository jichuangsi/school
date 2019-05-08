package com.jichuangsi.school.questionsrepository.importword.controller;

import com.jichuangsi.microservice.common.model.ResponseModel;
import com.jichuangsi.microservice.common.model.UserInfoForToken;
import com.jichuangsi.school.questionsrepository.exception.QuestionRepositoryServiceException;
import com.jichuangsi.school.questionsrepository.importword.exception.IImportWordServiceException;
import com.jichuangsi.school.questionsrepository.importword.service.IImportWordService;
import com.jichuangsi.school.questionsrepository.importword.util.Byte2File;
import com.jichuangsi.school.questionsrepository.importword.util.HtmlRegex;
//import com.jichuangsi.school.questionsrepository.importword.util.ImportWord;
import com.jichuangsi.school.questionsrepository.importword.util.Office2HtmlUtil;
import com.jichuangsi.school.questionsrepository.model.self.SelfQuestion;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.batik.transcoder.TranscoderException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

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
    @Value("${com.jichuangsi.school.wordLocalPath}")
    private String wordLocalPath;
    @Value("${com.jichuangsi.school.wordWebPath}")
    private String wordWebPath;

    @ApiOperation(value = "进行word转html再拆分题目再返回model,上传单份", notes = "")
    @RequestMapping("/open")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String")})
    public ResponseModel analyzeWord(@ModelAttribute UserInfoForToken userInfo, @RequestParam MultipartFile file) throws IImportWordServiceException, IOException, TranscoderException {
        String uuid=UUID.randomUUID().toString();
        String folderLocalPath = wordLocalPath + userInfo.getUserId()+"/"+uuid +"/";
        String folderWebPath = wordWebPath + userInfo.getUserId()+"/"+ uuid+"/";

        Byte2File.getFile(file.getBytes(),folderLocalPath,file.getOriginalFilename());
        Office2HtmlUtil.wordToHtml(folderLocalPath+file.getOriginalFilename(),folderLocalPath+file.getOriginalFilename().split("\\.")[0]+".html");
        List<SelfQuestion> selfQuestionsList = HtmlRegex.regexKillHtml(folderLocalPath+file.getOriginalFilename().split("\\.")[0]+".html",folderWebPath);
        return ResponseModel.sucess("", selfQuestionsList);
    }

    @ApiOperation(value = "保存题目到mongodb数据库", notes = "")
    @RequestMapping("/save")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String")})
    public ResponseModel save(@ModelAttribute UserInfoForToken userInfo, @RequestParam SelfQuestion[] questions) throws QuestionRepositoryServiceException {
        for (SelfQuestion question : questions
                ) {
            iImportWordService.save(userInfo, question);
        }
        return ResponseModel.sucess("", "导入成功");
    }
}
