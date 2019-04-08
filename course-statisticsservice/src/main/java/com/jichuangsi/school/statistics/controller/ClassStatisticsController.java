package com.jichuangsi.school.statistics.controller;

import com.jichuangsi.microservice.common.model.ResponseModel;
import com.jichuangsi.microservice.common.model.UserInfoForToken;
import com.jichuangsi.school.statistics.exception.QuestionResultException;
import com.jichuangsi.school.statistics.feign.model.TransferStudent;
import com.jichuangsi.school.statistics.model.classType.ClassStatisticsModel;
import com.jichuangsi.school.statistics.model.classType.SearchStudentKnowledgeModel;
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
@CrossOrigin
public class ClassStatisticsController {

    @Resource
    private IClassStatisticsService classStatisticsService;

    @ApiOperation(value = "根据老师id查一个月班级s统计", notes = "")
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

    @ApiOperation(value = "根据一个月的问题ids，以及班级id,查询所有学生的知识点分析", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String")
    })
    @PostMapping(value = "/teacher/getClassStudentKnowledges")
    public ResponseModel getClassStudentKnowledges(@ModelAttribute UserInfoForToken userInfo, @RequestBody SearchStudentKnowledgeModel model){
        try {
            return ResponseModel.sucess("",classStatisticsService.getClassStudentKnowledges(userInfo,model));
        } catch (QuestionResultException e) {
            return ResponseModel.fail("",e.getMessage());
        }
    }

    @ApiOperation(value = "老师获取当前课堂的签到人数", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "path", name = "courseId", value = "课堂ID", required = true, dataType = "String") })
    @GetMapping("/getCourseSign/{courseId}/{classId}")
    public ResponseModel<List<TransferStudent>> getCourseSign(@ModelAttribute UserInfoForToken userInfo, @PathVariable("courseId") String courseId,@PathVariable("classId") String classId){
        try {
            return ResponseModel.sucess("",classStatisticsService.getCourseSign(userInfo,courseId,classId));
        } catch (QuestionResultException e) {
            return ResponseModel.fail("",e.getMessage());
        }
    }
}
