package com.jichuangsi.school.statistics.controller;

import com.jichuangsi.microservice.common.model.ResponseModel;
import com.jichuangsi.microservice.common.model.UserInfoForToken;
import com.jichuangsi.school.statistics.exception.SchoolReportException;
import com.jichuangsi.school.statistics.model.Report.*;
import com.jichuangsi.school.statistics.service.ISchoolReportService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@Api("关于报告的controller")
@RequestMapping("/report")
@CrossOrigin
public class SchoolReportController {

    @Resource
    private ISchoolReportService schoolReportService;

    @ApiOperation(value = "根据学校Id查询年级", notes = "")
    @GetMapping("/getGradeBySchoolId")
    public ResponseModel<List<Grade>> getGradeBySchoolId(@RequestParam("schoolId") String schoolId){
        return schoolReportService.getGradeBySchoolId(schoolId);
    }
    @ApiOperation(value = "根据每个班级考试成绩对比", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String")
    })
    @PostMapping("/getSchoolTestReport")
    public ResponseModel<List<StudentTestScoreModel>> getSchoolTestReport(@ModelAttribute UserInfoForToken userInfo,@RequestBody StudentTestModel studentTestModel){
        try{
            return ResponseModel.sucess("",schoolReportService.getSchoolTestReport(userInfo,studentTestModel));
        }catch (SchoolReportException e){
            return ResponseModel.fail("",e.getMessage());
        }
    }

    @ApiOperation(value = "根据每个班级习题成绩对比", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String")
    })
    @PostMapping("/getSchoolHomeworkReport")
    public ResponseModel<List<StudentTestScoreModel>> getSchoolHomeworkReport(@ModelAttribute UserInfoForToken userInfo,@RequestBody StudentTestModel studentTestModel){
        try{
            return ResponseModel.sucess("",schoolReportService.getSchoolHomeworkReport(userInfo,studentTestModel));
        }catch (SchoolReportException e){
            return ResponseModel.fail("",e.getMessage());
        }
    }
    @ApiOperation(value = "根据习题id查出本次习题的知识点占比", notes = "")
    @PostMapping("/getHomeReportKnowledgeRate")
    public ResponseModel<HomworkReportRateModel> getHomeReportKnowledgeRate(@RequestParam("homeworkId")String homeworkId){
        try{
            return ResponseModel.sucess("",schoolReportService.getHomeReportKnowledgeRate(homeworkId));
        }catch (SchoolReportException e){
            return ResponseModel.fail("",e.getMessage());
        }
    }

    @ApiOperation(value = "根据课堂d查出本次课堂的知识点占比", notes = "")
    @PostMapping("/getCourseReportKnowledgeRate")
    public ResponseModel<HomworkReportRateModel> getCourseReportKnowledgeRate(@RequestParam("courseId") String courseId){
        try{
            return ResponseModel.sucess("",schoolReportService.getCourseReportKnowledgeRate(courseId));
        }catch (SchoolReportException e){
            return ResponseModel.fail("",e.getMessage());
        }
    }
    @ApiOperation(value = "根据考试id查出本次考试的知识点占比", notes = "")
    @PostMapping("/getTestReportKnowledgeRate")
    public ResponseModel<HomworkReportRateModel> getTestReportKnowledgeRate(@RequestParam("testId") String testId){
        try{
            return ResponseModel.sucess("",schoolReportService.getTestReportKnowledgeRate(testId));
        }catch (SchoolReportException e){
            return ResponseModel.fail("",e.getMessage());
        }
    }
}
