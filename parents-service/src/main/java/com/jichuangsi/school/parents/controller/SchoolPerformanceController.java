package com.jichuangsi.school.parents.controller;

import com.jichuangsi.microservice.common.model.ResponseModel;
import com.jichuangsi.microservice.common.model.UserInfoForToken;
import com.jichuangsi.school.parents.exception.ParentsException;

import com.jichuangsi.school.parents.feign.model.*;
import com.jichuangsi.school.parents.model.common.PageHolder;
import com.jichuangsi.school.parents.service.ISchoolPerformanceService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/SchoolPerformance")
@Api("在校情况的feign")
@CrossOrigin
public class SchoolPerformanceController {
    @Resource
    private ISchoolPerformanceService schoolPerformanceService;
    //获取学生历史习题列表
    @ApiOperation(value = "根据学生id获取历史习题列表信息", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String")})
    @PostMapping("/getTestHistory")
    public ResponseModel<PageHolder<TestModelForStudent>> getHistory(@RequestBody SearchTestModelId searchTestModel){
        try {
            ResponseModel<PageHolder<TestModelForStudent>> history = schoolPerformanceService.getHistory(searchTestModel);

            return history;
        } catch (ParentsException e) {
            return ResponseModel.fail("",e.getMessage());
        }
    }
    //获取学生历史课程列表
    @ApiOperation(value = "根据班级id获取历史学生课堂列表信息", notes = "")
    @ApiImplicitParams({})
    @PostMapping("/getCourseHistory")
    ResponseModel<PageHolder<CourseForStudent>> getHistory(@RequestBody CourseForStudentId pageInform){
        try {
             return schoolPerformanceService.getHistory(pageInform);
        } catch (ParentsException e) {
            return ResponseModel.fail("",e.getMessage());
        }
    }
    //    获取历史被点赞课堂
    @ApiOperation(value = "根据学生id获取历史学生课堂点赞", notes = "")
    @ApiImplicitParams({})
    @PostMapping("/getCourseCommend")
    public ResponseModel<List<CourseForStudent>> getCourseCommend(@RequestParam("studentId") String studentId,@RequestParam("pageSize")String pageSize,@RequestParam("pageNum")String pageNum){
        try {
            return schoolPerformanceService.getCourseCommend(studentId,pageSize,pageNum);
        } catch (ParentsException e) {
            return ResponseModel.fail("",e.getMessage());
        }
    }
    //    获取历史被点赞课堂（时间）
    @ApiOperation(value = "根据学生id获取历史学生课堂点赞", notes = "")
    @ApiImplicitParams({})
    @PostMapping("/getCourseCommendTime")
    public ResponseModel<List<CourseForStudent>> getCourseCommendTime(@RequestParam("studentId") String studentId,@RequestParam("statisticsTimes")List<Long> statisticsTimes){
        try {
            ResponseModel<List<CourseForStudent>> list=schoolPerformanceService.getCourseCommendTime(studentId,statisticsTimes);
          System.out.println(list.getData().size());
            return schoolPerformanceService.getCourseCommendTime(studentId,statisticsTimes);
        } catch (ParentsException e) {
            return ResponseModel.fail("",e.getMessage());
        }
    }
}
