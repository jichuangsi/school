package com.jichuangsi.school.statistics.controller;

import com.jichuangsi.microservice.common.model.ResponseModel;
import com.jichuangsi.microservice.common.model.UserInfoForToken;
import com.jichuangsi.school.statistics.service.IClassStatisticsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

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
    @GetMapping(value = "/teacher/getClassCourseByMonth/{classId}")
    public ResponseModel getStatisticsByMonth(@ModelAttribute UserInfoForToken userInfo, @PathVariable String classId){
        return ResponseModel.sucessWithEmptyData("");
    }

}
