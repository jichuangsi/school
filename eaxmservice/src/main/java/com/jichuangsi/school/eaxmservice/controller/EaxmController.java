package com.jichuangsi.school.eaxmservice.controller;

import com.jichuangsi.microservice.common.model.ResponseModel;
import com.jichuangsi.microservice.common.model.UserInfoForToken;
import com.jichuangsi.school.courseservice.model.transfer.TransferExam;
import com.jichuangsi.school.eaxmservice.Model.DeleteQueryModel;
import com.jichuangsi.school.eaxmservice.Model.EaxmModel;
import com.jichuangsi.school.eaxmservice.Model.QuestionModel;
import com.jichuangsi.school.eaxmservice.Utils.CommonUtils;
import com.jichuangsi.school.eaxmservice.constant.ResultCode;
import com.jichuangsi.school.eaxmservice.exception.EaxmException;
import com.jichuangsi.school.eaxmservice.service.IEaxmService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/eaxm")
@Api("eaxm相关api")
public class EaxmController {

    @Resource
    private IEaxmService eaxmService;

    @ApiOperation(value = "通过用户保存试卷", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String")
    })
    @PostMapping("/saveEaxm")
    public ResponseModel saveEaxmByTeacher(@ModelAttribute UserInfoForToken userInfoForToken, @RequestBody EaxmModel eaxmModel) throws EaxmException{
        if (eaxmModel==null){
            throw new EaxmException(ResultCode.PARAM_PATTERN_ERR);
        }
        eaxmService.saveEaxm(userInfoForToken,eaxmModel);
        return ResponseModel.sucessWithEmptyData("");
    }


    @ApiOperation(value = "用户进行批量删除", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String")
    })
    @DeleteMapping("/deleteEaxm")
    public ResponseModel deleteEaxmByTeacher(@RequestBody DeleteQueryModel deleteQueryModel) throws EaxmException{
        if (CommonUtils.decideList(deleteQueryModel.getIds())){
            throw new EaxmException(ResultCode.PARAM_PATTERN_ERR);
        }
        eaxmService.deleteEaxms(deleteQueryModel);
        return ResponseModel.sucessWithEmptyData("");
    }

    @ApiOperation(value = "根据用户更改试题", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String")
    })
    @PostMapping("/updateEaxm")
    public ResponseModel updateEaxmByTeacher(@ModelAttribute UserInfoForToken userInfo,@RequestBody EaxmModel eaxmModel) throws EaxmException{
        if(StringUtils.isEmpty(eaxmModel.getEaxmId())||CommonUtils.decideList(eaxmModel.getQuestionModels())){
            throw new EaxmException(ResultCode.PARAM_PATTERN_ERR);
        }
        eaxmService.updateEaxmQ(eaxmModel);
        return ResponseModel.sucessWithEmptyData("");
    }

    @ApiOperation(value = "根据用户及搜索条件，eaxm列表", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String")
    })
    @PostMapping("/findEaxms")
    public TransferExam findEaxms(@ModelAttribute UserInfoForToken userInfo, @RequestBody EaxmModel eaxmModel){

        return null;
    }

    @ApiOperation(value = "courseservice获取eaxm和eaxm内的试题集", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String")
    })
    @PostMapping("/getExamInfoForEaxmId")
    public List<QuestionModel> getExamInfoForEaxmId(@RequestParam(value = "eaxmId") String eaxmId){

        return eaxmService.getQuestions(eaxmId);
    }


}
