package com.jichuangsi.school.examservice.controller;

import com.jichuangsi.microservice.common.model.ResponseModel;
import com.jichuangsi.microservice.common.model.UserInfoForToken;
import com.jichuangsi.school.examservice.Model.DeleteQueryModel;
import com.jichuangsi.school.examservice.Model.ExamModel;
import com.jichuangsi.school.examservice.Model.PageHolder;
import com.jichuangsi.school.examservice.Model.QuestionModel;
import com.jichuangsi.school.examservice.Utils.CommonUtils;
import com.jichuangsi.school.examservice.constant.ResultCode;
import com.jichuangsi.school.examservice.exception.ExamException;
import com.jichuangsi.school.examservice.service.IExamService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/exam")
@Api("exam相关api")
public class ExamController {

    @Resource
    private IExamService examService;

    @ApiOperation(value = "通过用户保存试卷", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String")
    })
    @PostMapping("/saveExam")
    public ResponseModel saveExamByTeacher(@ModelAttribute UserInfoForToken userInfoForToken, @RequestBody ExamModel examModel) throws ExamException {
        if (examModel==null){
            throw new ExamException(ResultCode.PARAM_PATTERN_ERR);
        }
        examService.saveExam(userInfoForToken,examModel);
        return ResponseModel.sucessWithEmptyData("");
    }


    @ApiOperation(value = "用户进行批量删除", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String")
    })
    @DeleteMapping("/deleteExam")
    public ResponseModel deleteExamByTeacher(@RequestBody DeleteQueryModel deleteQueryModel) throws ExamException{
        if (CommonUtils.decideList(deleteQueryModel.getIds())){
            throw new ExamException(ResultCode.PARAM_PATTERN_ERR);
        }
        examService.deleteExams(deleteQueryModel);
        return ResponseModel.sucessWithEmptyData("");
    }

    @ApiOperation(value = "根据用户更改试题", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String")
    })
    @PostMapping("/updateExam")
    public ResponseModel updateExamByTeacher(@ModelAttribute UserInfoForToken userInfo, @RequestBody ExamModel examModel) throws ExamException{
        if(StringUtils.isEmpty(examModel.getExamId())||CommonUtils.decideList(examModel.getQuestionModels())){
            throw new ExamException(ResultCode.PARAM_PATTERN_ERR);
        }
        examService.updateExamQ(examModel);
        return ResponseModel.sucessWithEmptyData("");
    }

    @ApiOperation(value = "根据用户及搜索条件，exam列表", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String")
    })
    @PostMapping("/findExams")
    public ResponseModel<PageHolder<ExamModel>> findExams(@ModelAttribute UserInfoForToken userInfo, @RequestBody ExamModel examModel) throws ExamException{
        if(examModel==null){
            throw new ExamException(ResultCode.PARAM_MISS_MSG);
        }
        return ResponseModel.sucess("",examService.getExamByExamName(examModel));
    }

    @ApiOperation(value = "获取exam和exam内的试题集", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String")
    })
    @PostMapping("/getExamInfoForExamId")
    public ResponseModel<PageHolder<QuestionModel>> getExamInfoForExamId(@ModelAttribute UserInfoForToken userInfo,@RequestBody ExamModel examModel) throws  ExamException{
        if(examModel==null) throw new ExamException(ResultCode.PARAM_MISS_MSG);
        return ResponseModel.sucess("",examService.getQuestions(examModel));
    }

    @ApiOperation(value = "exam内的试题集", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String")
    })
    @GetMapping("/getQuestions/{examId}")
    public ResponseModel<List<QuestionModel>> getQuestions(@ModelAttribute UserInfoForToken userInfo,@PathVariable String examId) throws  ExamException{
        if(StringUtils.isEmpty(userInfo.getUserId()) || StringUtils.isEmpty(examId)) throw new ExamException(ResultCode.PARAM_MISS_MSG);
        return ResponseModel.sucess("",examService.getQuestions(examId));
    }

    @ApiOperation(value = "根据id获取试卷类，题型数量", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String")
    })
    @GetMapping("/getExamInfoCount")
    public ResponseModel getExamInfoCount(@ModelAttribute UserInfoForToken userInfo,@RequestParam String eid) throws ExamException{
        if(StringUtils.isEmpty(eid)){
            throw  new ExamException(ResultCode.PARAM_MISS_MSG);
        }
        Map<String,List<Map<String,Object>>> maps = new HashMap<String,List<Map<String,Object>>>();
        maps.put("qt",examService.getQuestionTypegroup(eid));
        maps.put("dt",examService.getQuestionDifficultgroup(eid));
        return ResponseModel.sucess("",maps);
    }
}
