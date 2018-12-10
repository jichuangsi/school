package com.jichuangsi.school.questionsrepository.controller;

import com.jichuangsi.microservice.common.model.ResponseModel;
import com.jichuangsi.microservice.common.model.UserInfoForToken;
import com.jichuangsi.school.questionsrepository.constant.ResultCode;
import com.jichuangsi.school.questionsrepository.exception.QuestionRepositoryServiceException;
import com.jichuangsi.school.questionsrepository.model.PageHolder;
import com.jichuangsi.school.questionsrepository.model.common.DeleteQueryModel;
import com.jichuangsi.school.questionsrepository.model.common.SearchQuestionModel;
import com.jichuangsi.school.questionsrepository.model.favor.FavorQuestion;
import com.jichuangsi.school.questionsrepository.model.favor.IncludeInfo;
import com.jichuangsi.school.questionsrepository.service.IFavorQuestionsRepositoryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/favor")
@Api("SelfQuestionRepositoryController相关的api")
public class FavorQuestionsRepositoryController {

    @Resource
    private IFavorQuestionsRepositoryService favorQuestionsRepositoryService;

    //添加个人收藏题目信息
    @ApiOperation(value = "添加个人收藏题目信息", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String")})
    @PostMapping("/saveQuestion")
    public ResponseModel saveQuestion(@ModelAttribute UserInfoForToken userInfo, @RequestBody FavorQuestion favorQuestion) throws QuestionRepositoryServiceException {
        if(!favorQuestionsRepositoryService.addFavorQuestion(userInfo,favorQuestion)){
            throw  new QuestionRepositoryServiceException(ResultCode.QUESTION_UPLOAD_ERROR);
        }
        return ResponseModel.sucessWithEmptyData("");
    }

    //获取个人收藏题目列表
    @ApiOperation(value = "获取个人收藏题目列表", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String")})
    @PostMapping("/getQuestions")
    public ResponseModel<PageHolder<FavorQuestion>> getQuestions(@ModelAttribute UserInfoForToken userInfo, @RequestBody SearchQuestionModel searchQuestion) throws QuestionRepositoryServiceException {

        return ResponseModel.sucess("",favorQuestionsRepositoryService.getFavorQuestionSortList(userInfo,searchQuestion) );
    }

    //取消指定个人收藏题目
    @ApiOperation(value = "取消指定个人收藏题目", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String")})
    @DeleteMapping("/deleteQuestions")
    public ResponseModel deleteQuestions(@ModelAttribute UserInfoForToken userInfo, @RequestBody DeleteQueryModel deleteQueryModel) throws QuestionRepositoryServiceException {
        /*FavorQuestions fqs = favorQuestionsRepositoryService.getFavorById(deleteQueryModel.getIds().get(0));
        if(fqs==null){
            throw new QuestionRepositoryServiceException(ResultCode.QUESTION_NOT_EXISTED);
        }
        favorQuestionsRepositoryService.deleteFavorQuestion(fqs);*/
        favorQuestionsRepositoryService.deleteFavorQuestions(deleteQueryModel);
        return ResponseModel.sucessWithEmptyData("");
    }


    //取消指定个人收藏题目
    @ApiOperation(value = "判断是否存在指定个人收藏题目", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String")})
    @GetMapping("/isExistFavor")
    public ResponseModel<IncludeInfo> isExistFavor(@ModelAttribute UserInfoForToken userInfo, @RequestParam String MD52){
        IncludeInfo info = new IncludeInfo();
        info.setResult("none");
        if(favorQuestionsRepositoryService.isExistFavor(userInfo,MD52)){
            info.setResult("include");
        }
        return ResponseModel.sucess("",info);
    }
}
