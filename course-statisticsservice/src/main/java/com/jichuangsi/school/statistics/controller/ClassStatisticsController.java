package com.jichuangsi.school.statistics.controller;

import com.jichuangsi.microservice.common.model.ResponseModel;
import com.jichuangsi.microservice.common.model.UserInfoForToken;
import com.jichuangsi.school.statistics.exception.QuestionResultException;
import com.jichuangsi.school.statistics.model.classType.ClassStatisticsModel;
import com.jichuangsi.school.statistics.service.IClassStatisticsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/class")
@Api("关于班级统计controller")
public class ClassStatisticsController {

    @Resource
    private IClassStatisticsService classStatisticsService;

    @ApiOperation(value = "根据老师id，班级id查一个月班级统计", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String")
    })
    @GetMapping(value = "/teacher/getClassCourseByMonth")
    public ResponseModel<List<ClassStatisticsModel>> getStatisticsByMonth(@ModelAttribute UserInfoForToken userInfo){
        try {
            return ResponseModel.sucess("",classStatisticsService.getTeachClassStatistics(userInfo));
        } catch (QuestionResultException e) {
            return ResponseModel.fail("",e.getMessage());
        }
    }

}
