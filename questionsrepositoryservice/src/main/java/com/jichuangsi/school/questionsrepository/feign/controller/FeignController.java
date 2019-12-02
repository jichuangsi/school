package com.jichuangsi.school.questionsrepository.feign.controller;

import com.jichuangsi.microservice.common.model.ResponseModel;
import com.jichuangsi.microservice.common.model.UserInfoForToken;
import com.jichuangsi.school.questionsrepository.exception.QuestionRepositoryServiceException;
import com.jichuangsi.school.questionsrepository.feign.model.SechQuesionModel;
import com.jichuangsi.school.questionsrepository.feign.service.IFeignService;
import com.jichuangsi.school.questionsrepository.model.PageHolder;
import com.jichuangsi.school.questionsrepository.model.QuestionNode;
import com.jichuangsi.school.questionsrepository.model.QuestionQueryModel;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import javax.annotation.Resource;

@RestController
@RequestMapping("/feign")
@Api("关于question的feignController")
public class FeignController {
    @Resource
    private IFeignService feignService;

    @ApiOperation(value = "根据知识点id，题型，试卷类型，难度，年份，分页获取试题", notes = "")
    @ApiImplicitParams({
        @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String")})
    @PostMapping("/getListForQuestionsBy")
    public Mono<ResponseModel<PageHolder<QuestionNode>>> getListForQuestionsBy(@ModelAttribute UserInfoForToken userInfo, @RequestBody SechQuesionModel questionModel) throws QuestionRepositoryServiceException {

        return Mono.just(ResponseModel.sucess("", feignService.getListForQuestionsBy(userInfo, questionModel)));
    }
}
