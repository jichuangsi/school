package com.jichuangsi.school.statistics.controller;

import com.jichuangsi.microservice.common.model.ResponseModel;
import com.jichuangsi.school.statistics.exception.QuestionResultException;
import com.jichuangsi.school.statistics.model.SearchStudentCapabilityModel;
import com.jichuangsi.school.statistics.model.classType.CapabilityStudentModel;
import com.jichuangsi.school.statistics.service.IQuestionResultService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/capability")
@Api("关于科目认知能力统计controller")
@CrossOrigin
public class CapabilityStudentController {


    @Resource
    //private IClassStatisticsService classStatisticsService;
    private IQuestionResultService questionResultService;

    @ApiOperation(value = "根据一个月的问题ids，以及班级id,查询所有学生的认知能力", notes = "")
    @PostMapping(value="/question/getClassStudentByCapability")
    public ResponseModel<CapabilityStudentModel> getClassStudentByCapability(@RequestBody SearchStudentCapabilityModel model) {
        try {
            return ResponseModel.sucess("",questionResultService.getClassStudentCapability(model));
        } catch (QuestionResultException e) {
            return ResponseModel.fail("", e.getMessage());
        }
    }

}
