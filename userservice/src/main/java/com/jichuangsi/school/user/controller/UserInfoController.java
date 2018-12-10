package com.jichuangsi.school.user.controller;

import com.jichuangsi.school.user.model.transfer.TransferClass;
import com.jichuangsi.school.user.model.transfer.TransferSchool;
import com.jichuangsi.school.user.model.transfer.TransferStudent;
import com.jichuangsi.school.user.model.transfer.TransferTeacher;
import com.jichuangsi.school.user.service.UserInfoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@Api("UserInfoController")
public class UserInfoController {

    @Resource
    private UserInfoService userInfoService;

    @ApiOperation(value = "获取指定老师信息", notes = "")
    @GetMapping("/getUserInfoForTeacher")
    public TransferTeacher getUserForTeacherById(@RequestParam(value = "teacherId") String teacherId){
        return  userInfoService.getTeacherById(teacherId);
    }

    @ApiOperation(value = "获取指定老师的班级信息", notes = "")
    @GetMapping("/getClassInfoForTeacher")
    public List<TransferClass> getTeacherClass(@RequestParam(value = "teacherId") String teacherId){
        try {
            return userInfoService.getTeacherClass(teacherId);
        } catch (Exception e) {
            e.getMessage();
            return null;
        }
    }

    @ApiOperation(value = "获取指定学校的基本信息", notes = "")
    @GetMapping("/getSchoolInfoForTeacher")
    public TransferSchool getSchoolInfo(@RequestParam(value = "userId") String userId){
        try {
            return userInfoService.getSchoolInfoById(userId);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @ApiOperation(value = "获取指定班级的学生信息", notes = "")
    @GetMapping("/getStudentsForClass")
    public List<TransferStudent> getStudentsForClass(@RequestParam(value = "classId") String classId){
        return  userInfoService.getStudentsByClassId(classId);
    }
}
