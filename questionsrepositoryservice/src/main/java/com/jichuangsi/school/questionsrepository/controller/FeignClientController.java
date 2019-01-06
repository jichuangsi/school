package com.jichuangsi.school.questionsrepository.controller;

import com.jichuangsi.microservice.common.model.ResponseModel;
import com.jichuangsi.school.questionsrepository.exception.QuestionRepositoryServiceException;
import com.jichuangsi.school.questionsrepository.model.PageHolder;
import com.jichuangsi.school.questionsrepository.model.QuestionNode;
import com.jichuangsi.school.questionsrepository.model.QuestionQueryModel;
import com.jichuangsi.school.questionsrepository.service.IQuestionsRepositoryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/feign")
@Api("FeignClientController")
public class FeignClientController {

    @Resource
    private IQuestionsRepositoryService questionsRepositoryService;

    @ApiOperation(value = "根据知识点id，题型，试卷类型，难度，年份，分页获取试题", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String")})
    @PostMapping("/getQuestionsByKnowledge")
    public PageHolder<QuestionNode> getQuestionsByKnowledge(@RequestBody QuestionQueryModel questionQueryModel) throws QuestionRepositoryServiceException {

        return questionsRepositoryService.getListForQuestionsByKnowledge(null, questionQueryModel);
    }
}
