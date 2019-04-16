package com.jichuangsi.school.parents.controller;

import com.jichuangsi.microservice.common.model.ResponseModel;
import com.jichuangsi.microservice.common.model.UserInfoForToken;
import com.jichuangsi.school.parents.exception.ParentsException;
import com.jichuangsi.school.parents.feign.model.ClassTeacherInfoModel;
import com.jichuangsi.school.parents.feign.model.HomeWorkParentModel;
import com.jichuangsi.school.parents.feign.model.TimeTableModel;
import com.jichuangsi.school.parents.model.GrowthModel;
import com.jichuangsi.school.parents.model.ParentStudentModel;
import com.jichuangsi.school.parents.model.statistics.KnowledgeStatisticsModel;
import com.jichuangsi.school.parents.model.statistics.ParentStatisticsModel;
import com.jichuangsi.school.parents.service.IStudentService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/studentInfo")
@Api("家长关注学生信息")
@CrossOrigin
public class StudentInfoController {

    @Resource
    private IStudentService studentService;

    @ApiOperation(value = "查看关注的学生信息", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String")
    })
    @GetMapping(value = "/getAttentions")
    public ResponseModel<List<ParentStudentModel>> getAttentions(@ModelAttribute UserInfoForToken userInfo){
        try {
            return ResponseModel.sucess("",studentService.getStudentByStudentId(userInfo));
        } catch (ParentsException e) {
            return ResponseModel.fail("",e.getMessage());
        }
    }

    @ApiOperation(value = "更换学生的关注", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String")
    })
    @GetMapping(value = "/updateAttention/{studentId}")
    public ResponseModel updateAttention(@ModelAttribute UserInfoForToken userInfo, @PathVariable String studentId){
        try {
            studentService.updateAttention(userInfo, studentId);
        } catch (ParentsException e) {
            return ResponseModel.fail("",e.getMessage());
        }
        return ResponseModel.sucessWithEmptyData("");
    }

    @ApiOperation(value = "添加学生的成长动态", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String")
    })
    @PostMapping(value = "/insertGrowth/{studentId}")
    public ResponseModel insertGrowth(@RequestParam MultipartFile file, @ModelAttribute UserInfoForToken userInfo, GrowthModel model){
        try {
            studentService.uploadGrowth(file, userInfo, model);
        } catch (ParentsException e) {
            return ResponseModel.fail("",e.getMessage());
        }
        return ResponseModel.sucessWithEmptyData("");
    }

    @ApiOperation(value = "删除学生的成长动态", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String")
    })
    @DeleteMapping(value = "/deleteGrowth/{studentId}/{growthId}")
    public ResponseModel deleteGrowth(@ModelAttribute UserInfoForToken userInfo,@PathVariable("studentId") String studentId,@PathVariable("growthId") String growthId){
        try {
            studentService.deleteGrowth(userInfo, studentId, growthId);
        } catch (ParentsException e) {
            return ResponseModel.fail("",e.getMessage());
        }
        return ResponseModel.sucessWithEmptyData("");
    }

    @ApiOperation(value = "查看学生的成长动态", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String")
    })
    @GetMapping(value = "/getGrowths/{studentId}")
    public ResponseModel<List<GrowthModel>> getGrowths(@ModelAttribute UserInfoForToken userInfo,@PathVariable String studentId){
        try {
            return ResponseModel.sucess("",studentService.getGrowths(userInfo, studentId));
        } catch (ParentsException e) {
            return ResponseModel.fail("",e.getMessage());
        }
    }

    @ApiOperation(value = "查看学生的当天作业状态", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String")
    })
    @GetMapping(value = "/getStudentHomeWork/{studentId}")
    public ResponseModel<Map<String,List<HomeWorkParentModel>>> getStudentHomeWork(@ModelAttribute UserInfoForToken userInfo, @PathVariable String studentId){
        try {
            return ResponseModel.sucess("",studentService.getStudentHomeWork(userInfo, studentId));
        } catch (ParentsException e) {
            return ResponseModel.fail("",e.getMessage());
        }
    }

    @ApiOperation(value = "查看学生的老师信息", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String")
    })
    @GetMapping(value = "/getStudentTeachers/{studentId}")
    public ResponseModel<List<ClassTeacherInfoModel>> getStudentTeachers(@ModelAttribute UserInfoForToken userInfo, @PathVariable String studentId){
        try {
            return ResponseModel.sucess("",studentService.getStudentTeachers(userInfo, studentId));
        } catch (ParentsException e) {
            return ResponseModel.fail("",e.getMessage());
        }
    }

    @ApiOperation(value = "查看学生的各科课堂成绩", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String")
    })
    @PostMapping(value = "/getStudentCourseScore}")
    public ResponseModel<List<KnowledgeStatisticsModel>> getStudentCourseScore(@ModelAttribute UserInfoForToken userInfo, @RequestBody ParentStatisticsModel model){
        try {
            return ResponseModel.sucess("",studentService.getParentCourseStatistics(userInfo, model));
        } catch (ParentsException e) {
            return ResponseModel.fail("",e.getMessage());
        }
    }

    @ApiOperation(value = "查看学生的各科课后作业成绩", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String")
    })
    @PostMapping(value = "/getStudentHomeworkScore}")
    public ResponseModel<List<KnowledgeStatisticsModel>> getStudentHomeworkScore(@ModelAttribute UserInfoForToken userInfo,@RequestBody ParentStatisticsModel model){
        try {
            return ResponseModel.sucess("",studentService.getParentHomeworkStatistics(userInfo, model));
        } catch (ParentsException e) {
            return ResponseModel.fail("",e.getMessage());
        }
    }

    @ApiOperation(value = "获取学生的课程安排", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String")
    })
    @GetMapping(value = "/getStudentTimeTable/{studentId}")
    public ResponseModel<TimeTableModel> getStudentTimeTable(@ModelAttribute UserInfoForToken userInfo,@PathVariable String studentId){
        try {
            return ResponseModel.sucess("",studentService.getStudentTimeTable(userInfo, studentId));
        } catch (ParentsException e) {
            return ResponseModel.fail("",e.getMessage());
        }
    }
}
