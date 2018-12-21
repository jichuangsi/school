package com.jichuangsi.school.questionsrepository.controller;

import com.jichuangsi.microservice.common.model.ResponseModel;
import com.jichuangsi.microservice.common.model.UserInfoForToken;
import com.jichuangsi.school.questionsrepository.constant.ResultCode;
import com.jichuangsi.school.questionsrepository.exception.QuestionRepositoryServiceException;
import com.jichuangsi.school.questionsrepository.model.*;
import com.jichuangsi.school.questionsrepository.model.transfer.TransferTeacher;
import com.jichuangsi.school.questionsrepository.service.IQuestionsRepositoryService;
import com.jichuangsi.school.questionsrepository.service.IUserInfoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.annotation.Resource;
import java.time.Duration;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/question")
@CrossOrigin
@Api("QuestionsRepositoryController相关的api")
public class QuestionsRepositoryController {

    @Resource
    private IUserInfoService userInfoService;

    @Resource
    private IQuestionsRepositoryService questionsRepositoryService;

    //根据知识点id获取试题
    /*@ApiOperation(value = "根据知识点id获取试题", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String")})
    @GetMapping("/getList")*/

    //获取学段、学科、年级、版本信息
    @ApiOperation(value = "获取学段、学科、年级、版本信息", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String")})
    @GetMapping("/getSubjectEditionInfo")
    public Mono<ResponseModel<List<EditionTreeNode>>> getSubjectEditionInfo(@ModelAttribute UserInfoForToken userInfo) throws QuestionRepositoryServiceException{

        return Mono.just(ResponseModel.sucess("", questionsRepositoryService.getTreeForSubjectEditionInfo(userInfo)));
    }

    @ApiOperation(value = "根据老师信息获取学段、学科、年级、版本信息", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String")})
    @GetMapping("/getSubjectEditionInfoByTeacher")
    public Mono<ResponseModel<List<EditionTreeNode>>> getSubjectEditionInfoByTeacher(@ModelAttribute UserInfoForToken userInfo) throws QuestionRepositoryServiceException{

        return Mono.just(ResponseModel.sucess("", questionsRepositoryService.getTreeForSubjectEditionInfoByTeacher(userInfo)));
    }

    @ApiOperation(value = "获取题型、难易度、试卷类型、年份、地区信息", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String")})
    @GetMapping("/getOtherBasicInfo")
    public Mono<ResponseModel<Map<String, List>>> getOtherBasicInfo(@ModelAttribute UserInfoForToken userInfo) throws QuestionRepositoryServiceException{

        return Mono.just(ResponseModel.sucess("", questionsRepositoryService.getMapForOtherBasicInfo(userInfo)));
    }

    @ApiOperation(value = "获取学段、学科、年级、版本、题型、难易度、试卷类型信息", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String")})
    @GetMapping("/getAllBasicInfo")
    public Mono<ResponseModel<Map<String, List>>> getAllBasicInfo(@ModelAttribute UserInfoForToken userInfo) throws QuestionRepositoryServiceException{

        return Mono.just(ResponseModel.sucess("", questionsRepositoryService.getAllBasicInfoForQuestionSelection(userInfo)));
    }

    @ApiOperation(value = "获取章节及知识点数据", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String")})
    @PostMapping("/getChapterInfo")
    public Mono<ResponseModel<List<ChapterTreeNode>>> getChapterInfo(@ModelAttribute UserInfoForToken userInfo, @RequestBody ChapterQueryModel chapterQueryModel) throws QuestionRepositoryServiceException{

        return Mono.just(ResponseModel.sucess("", questionsRepositoryService.getTreeForChapterInfo(userInfo, chapterQueryModel)));
    }

    @ApiOperation(value = "根据知识点id，题型，试卷类型，难度，年份，分页获取试题", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String")})
    @PostMapping("/getQuestionsByKnowledge")
    public Mono<ResponseModel<PageHolder<QuestionNode>>> getQuestionsByKnowledge(@ModelAttribute UserInfoForToken userInfo, @RequestBody QuestionQueryModel questionQueryModel) throws QuestionRepositoryServiceException{

        return Mono.just(ResponseModel.sucess("", questionsRepositoryService.getListForQuestionsByKnowledge(userInfo, questionQueryModel)));
    }

    @ApiOperation(value = "根据知识点id，题型，试卷类型，难度，年份，分页获取试题以及试题的统计数据", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String")})
    @PostMapping("/getQuestionsExtraByKnowledge")
    public Mono<ResponseModel<PageHolder<QuestionExtraNode>>> getQuestionsExtraByKnowledge(@ModelAttribute UserInfoForToken userInfo, @RequestBody QuestionQueryModel questionQueryModel) throws QuestionRepositoryServiceException{

        return Mono.just(ResponseModel.sucess("", questionsRepositoryService.getListForQuestionsExtraByKnowledge(userInfo, questionQueryModel)));
    }

    @ApiOperation(value = "根据试题id获取试题答案", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String")})
    @PostMapping("/getAnswerViaQuestions")
    public Mono<ResponseModel<List<AnswerNode>>> getAnswerViaQuestions(@ModelAttribute UserInfoForToken userInfo, @RequestBody AnswerQueryModel answerQueryModel) throws QuestionRepositoryServiceException{

        return Mono.just(ResponseModel.sucess("", questionsRepositoryService.getListForAnswersByQuestionId(userInfo, answerQueryModel)));
    }

    @ApiOperation(value = "根据老师科目，学段信息获取知识点信息", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String")})
    @GetMapping("/getKnowledgeInfoByTeacher")
    public Mono<ResponseModel<List<KnowledgeTreeNode>>> getKnowledgeInfoByTeacher(@ModelAttribute UserInfoForToken userInfo) throws QuestionRepositoryServiceException{
        TransferTeacher teacher = userInfoService.getUserForTeacherById(userInfo.getUserId());
        if(teacher==null) throw new QuestionRepositoryServiceException(ResultCode.TEACHER_INFO_NOT_EXISTED);
        if(StringUtils.isEmpty(teacher.getPhraseId()) || StringUtils.isEmpty(teacher.getSubjectId())) throw new QuestionRepositoryServiceException(ResultCode.PARAM_MISS_MSG);
        return Mono.just(ResponseModel.sucess("", questionsRepositoryService.getTreeForKnowledgeInfoByTeacher(teacher.getPhraseId(), teacher.getSubjectId())));
    }

    /*@ApiOperation(value = "根据组卷次数进行排序", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String")})
    @CrossOrigin
    @PostMapping("/SortByAddPapercount")
    public Mono<ResponseModel<PageHolder<Map<QuestionNode, Integer>>>> SortByAddPapercount(@ModelAttribute UserInfoForToken userInfo, @RequestBody QuestionQueryModel questionQueryModel) throws QuestionRepositoryServiceException {

        return Mono.just(ResponseModel.sucess("", questionsRepositoryService.SortByAddPapercount(userInfo, questionQueryModel)));
    }

    @ApiOperation(value = "根据回答次数进行排序", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String")})
    @CrossOrigin
    @PostMapping("/SortByAnswerCount")
    public Mono<ResponseModel<PageHolder<Map<QuestionNode, Integer>>>> SortByAnswerCount(@ModelAttribute UserInfoForToken userInfo, @RequestBody QuestionQueryModel questionQueryModel) throws QuestionRepositoryServiceException {

        return Mono.just(ResponseModel.sucess("", questionsRepositoryService.SortByAnswerCount(userInfo, questionQueryModel)));
    }*/
}
