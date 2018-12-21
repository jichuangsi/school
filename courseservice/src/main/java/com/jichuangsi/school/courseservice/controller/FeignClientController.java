package com.jichuangsi.school.courseservice.controller;

import com.jichuangsi.microservice.common.model.ResponseModel;
import com.jichuangsi.school.courseservice.Exception.StudentCourseServiceException;
import com.jichuangsi.school.courseservice.service.IFeignClientService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/feign")
@Api("StudentCourseController相关的api")
public class FeignClientController {

    @Resource
    private IFeignClientService iFeignClientService;

    //获取指定课堂题目知识点
    @ApiOperation(value = "根据问题id查询问题知识点", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "path", name = "questionId", value = "问题ID", required = true, dataType = "String") })
    @GetMapping("/getQuestionKnowledge/{questionId}")
    public ResponseModel<String> getQuestionKnowledge(@PathVariable String questionId) throws StudentCourseServiceException {

        return ResponseModel.sucess("",  iFeignClientService.getKnowledgeOfParticularQuestion(questionId));
    }
}
