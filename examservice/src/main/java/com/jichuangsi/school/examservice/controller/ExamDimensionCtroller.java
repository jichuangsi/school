package com.jichuangsi.school.examservice.controller;

import com.jichuangsi.microservice.common.model.ResponseModel;
import com.jichuangsi.microservice.common.model.UserInfoForToken;
import com.jichuangsi.school.examservice.Model.DeleteQueryModel;
import com.jichuangsi.school.examservice.Model.Dimension.DimensionModel;
import com.jichuangsi.school.examservice.Model.ExamModel;
import com.jichuangsi.school.examservice.Model.PageHolder;
import com.jichuangsi.school.examservice.Model.QuestionModel;
import com.jichuangsi.school.examservice.Utils.CommonUtils;
import com.jichuangsi.school.examservice.constant.ResultCode;
import com.jichuangsi.school.examservice.entity.ExamDimension;
import com.jichuangsi.school.examservice.entity.Question;
import com.jichuangsi.school.examservice.exception.ExamException;
import com.jichuangsi.school.examservice.service.ExamDimensionSerivce;
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
import java.util.regex.Pattern;

@RestController
@RequestMapping("/examdimension")
@Api("ExamDimensionController相关api")
@CrossOrigin
public class ExamDimensionCtroller {
    @Resource
    private ExamDimensionSerivce dimensionSerivce;

    @ApiOperation(value = "通过用户保存试卷", notes = "")
    @ApiImplicitParams({
        @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String")
    })
    @PostMapping("/saveExamDimension")
    public ResponseModel saveExamDimension(@ModelAttribute UserInfoForToken userInfoForToken, @RequestBody ExamModel examModel) throws ExamException {
        if (examModel==null){
            throw new ExamException(ResultCode.PARAM_PATTERN_ERR);
        }
        dimensionSerivce.saveExam(userInfoForToken,examModel);
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
        dimensionSerivce.deleteExams(deleteQueryModel);
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
        return ResponseModel.sucess("",dimensionSerivce.getExamByExamName(userInfo, examModel));
    }

    @ApiOperation(value = "获取exam和exam内的试题集", notes = "")
    @ApiImplicitParams({
        @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String")
    })
    @PostMapping("/getExamInfoForExamId")
    public ResponseModel<PageHolder<QuestionModel>> getExamInfoForExamId(@ModelAttribute UserInfoForToken userInfo, @RequestBody ExamModel examModel) throws  ExamException{
        if(examModel==null) throw new ExamException(ResultCode.PARAM_MISS_MSG);
          return ResponseModel.sucess("",dimensionSerivce.getQuestions(examModel));
    }

    @ApiOperation(value = "exam内的试题集", notes = "")
    @ApiImplicitParams({
        @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String")
    })
    @GetMapping("/getQuestions/{examId}")
    public ResponseModel<List<QuestionModel>> getQuestions(@ModelAttribute UserInfoForToken userInfo, @PathVariable String examId) throws  ExamException{
        if(StringUtils.isEmpty(userInfo.getUserId()) || StringUtils.isEmpty(examId)) throw new ExamException(ResultCode.PARAM_MISS_MSG);
        return ResponseModel.sucess("",dimensionSerivce.getQuestions(examId));
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
        maps.put("qt",dimensionSerivce.getQuestionTypegroup(eid));
        maps.put("dt",dimensionSerivce.getQuestionDifficultgroup(eid));
        return ResponseModel.sucess("",maps);
    }

    @ApiOperation(value = "获取实体类型", notes = "")
    @ApiImplicitParams({
        @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String")
    })
    @GetMapping("/getExamDimensionModel")
    public ResponseModel<List<DimensionModel>> getExamDimensionModel(@ModelAttribute UserInfoForToken userInfo) throws ExamException{

        return ResponseModel.sucess("",dimensionSerivce.getExamDimensionModel(userInfo));
    }

    @ApiOperation(value = "根据id查试卷", notes = "")
    @ApiImplicitParams({
        @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String")
    })
    @PostMapping("/getExamDimensionById")
    public ResponseModel<PageHolder<ExamModel>> getExamDimensionById(@ModelAttribute UserInfoForToken userInfo,@RequestBody ExamModel examModel) throws ExamException{
        if (StringUtils.isEmpty(examModel.getExamId())){
            return ResponseModel.sucess("",dimensionSerivce.getExamByExamName(userInfo,examModel));
        }
        return ResponseModel.sucess("",dimensionSerivce.getExamDimensionById(userInfo,examModel));
    }

    /*@ApiOperation(value = "根据试卷id查询题目信息", notes = "")
    @ApiImplicitParams({
        @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String")
    })
    @PostMapping("/getExamDimensionById")
    public ResponseModel<List<Question>> getQuestionById(@ModelAttribute UserInfoForToken userInfo, @RequestParam String examId) throws ExamException{

        return ResponseModel.sucess("",dimensionSerivce.getQuestionById(userInfo,examId));
    }*/

    @ApiOperation(value = "通过用户保存试卷,根据试卷类型生成组题试卷，", notes = "")
    @ApiImplicitParams({
        @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String")
    })
    @PostMapping("/saveGroupQuestions")
    public ResponseModel saveGroupQuestions(@ModelAttribute UserInfoForToken userInfoForToken, @RequestParam String examId) throws ExamException {
        if (StringUtils.isEmpty(examId)){
            throw new ExamException(ResultCode.PARAM_PATTERN_ERR);
        }
        dimensionSerivce.saveGroupQuestions(userInfoForToken,examId);
        return ResponseModel.sucessWithEmptyData("");
    }
}
