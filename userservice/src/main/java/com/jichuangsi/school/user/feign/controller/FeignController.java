package com.jichuangsi.school.user.feign.controller;

import com.jichuangsi.microservice.common.model.ResponseModel;
import com.jichuangsi.school.user.exception.FeignControllerException;
import com.jichuangsi.school.user.feign.model.*;
import com.jichuangsi.school.user.feign.service.IFeignService;
import com.jichuangsi.school.user.model.backstage.TimeTableModel;
import com.jichuangsi.school.user.model.school.SchoolModel;
import com.jichuangsi.school.user.model.transfer.TransferStudent;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/feign")
@Api("user的feign")
public class FeignController {

    @Resource
    private IFeignService feignService;

    @ApiOperation(value = "获取班级最新信息，包括班级人数，年级", notes = "")
    @GetMapping("/getClassDetail")
    public ResponseModel<ClassDetailModel>  getClassDetail(@RequestParam("classId") String classId){
        try {
            return ResponseModel.sucess("",feignService.findClassDetailByClassId(classId));
        } catch (FeignControllerException e) {
            return ResponseModel.fail("",e.getMessage());
        }
    }

    @ApiOperation(value = "根据老师id，获取所有执教班级", notes = "")
    @GetMapping("/getTeachClassIds")
    public ResponseModel<List<String>> getTeachClassIds(@RequestParam("teacherId") String teacherId){
        try {
            return ResponseModel.sucess("",feignService.getClassIdsByTeacherId(teacherId));
        } catch (FeignControllerException e) {
            return ResponseModel.fail("",e.getMessage());
        }
    }

    @ApiOperation(value = "获取班级s最新信息，包括班级人数，年级", notes = "")
    @PostMapping("/getClassDetail")
    public ResponseModel<List<ClassDetailModel>> getClassDetailByIds(@RequestBody List<String> classIds){
        try {
            return ResponseModel.sucess("",feignService.findClassDetailByClassIds(classIds));
        } catch (FeignControllerException e) {
            return ResponseModel.fail("",e.getMessage());
        }
    }

    @ApiOperation(value = "获取指定班级的学生信息", notes = "")
    @GetMapping("/getStudentsForClass")
    public ResponseModel<List<TransferStudent>> getStudentsForClass(@RequestParam(value = "classId") String classId){
        try {
            return ResponseModel.sucess("",feignService.findStudentsByClassId(classId));
        } catch (FeignControllerException e) {
            return ResponseModel.fail("",e.getMessage());
        }
    }

    @ApiOperation(value = "根据学校id获取学校信息", notes = "")
    @GetMapping("/getSchoolBySchoolId")
    public ResponseModel<SchoolModel> getSchoolBySchoolId(@RequestParam("schoolId") String schoolId){
        try {
            return ResponseModel.sucess("",feignService.findSchoolBySchoolId(schoolId));
        } catch (FeignControllerException e) {
            return ResponseModel.fail("",e.getMessage());
        }
    }

    @ApiOperation(value = "获取后台所有学校信息", notes = "")
    @GetMapping("/getBackSchools")
    public ResponseModel<List<SchoolModel>> getBackSchools(){
        try {
            return ResponseModel.sucess("",feignService.findBackSchools());
        } catch (FeignControllerException e) {
            return ResponseModel.fail("",e.getMessage());
        }
    }

    @ApiOperation(value = "家长获取当选学生的信息", notes = "")
    @PostMapping("/getParentStudent")
    public ResponseModel<List<ParentStudentModel>> getParentStudent(@RequestBody List<String> studentIds){
        try {
            return ResponseModel.sucess("",feignService.getParentStudent(studentIds));
        } catch (FeignControllerException e) {
            return ResponseModel.fail("",e.getMessage());
        }
    }

    @ApiOperation(value = "根据学生id获取班级信息", notes = "")
    @GetMapping("/getStudentClassDetail")
    public ResponseModel<ClassDetailModel> getStudentClassDetail(@RequestParam String studentId){
        try {
            return ResponseModel.sucess("",feignService.getStudentClassDetail(studentId));
        } catch (FeignControllerException e) {
            return ResponseModel.fail("",e.getMessage());
        }
    }

    @ApiOperation(value = "根据学生id获取班级老师信息", notes = "")
    @GetMapping("/getStudentTeachers")
    public ResponseModel<List<ClassTeacherInfoModel>> getStudentTeachers(@RequestParam String studentId){
        try {
            return ResponseModel.sucess("",feignService.getStudentTeachers(studentId));
        } catch (FeignControllerException e) {
            return ResponseModel.fail("",e.getMessage());
        }
    }

    @ApiOperation(value = "根据学生account获取学生信息", notes = "")
    @GetMapping("/getStudentByAccount")
    public ResponseModel<TransferStudent> getStudentByAccount(@RequestParam("account") String account){
        try {
            return ResponseModel.sucess("",feignService.getStudentByAccount(account));
        } catch (FeignControllerException e) {
            return ResponseModel.fail("",e.getMessage());
        }
    }

    @ApiOperation(value = "根据学生id获取学生课程表", notes = "")
    @GetMapping("/getStudentTimeTable")
    public ResponseModel<TimeTableModel> getStudentTimeTable(@RequestParam("studentId") String studentId){
        try {
            return ResponseModel.sucess("",feignService.getStudentTimeTable(studentId));
        } catch (FeignControllerException e) {
            return ResponseModel.fail("",e.getMessage());
        }
    }

    @ApiOperation(value = "根据noticeId获取通知详情", notes = "")
    @RequestMapping("/getNoticeDetails")
    public ResponseModel<NoticeModel> getNoticeDetails(@RequestParam("noticeId") String noticeId){
        try {
            return ResponseModel.sucess("",feignService.getNoticeDetailByNoticeId(noticeId));
        } catch (FeignControllerException e) {
            return ResponseModel.fail("",e.getMessage());
        }
    }

    @ApiOperation(value = "发送学生的签到信息", notes = "")
    @PostMapping("/sendParentStudentMsg")
    public ResponseModel sendParentStudentMsg(@RequestBody CourseSignModel model){
        try {
            feignService.sendParentStudentMsg(model);
        } catch (FeignControllerException e) {
            return ResponseModel.fail("",e.getMessage());
        }
        return ResponseModel.sucessWithEmptyData("");
    }

}
