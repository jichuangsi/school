package com.jichuangsi.school.user.controller;

import com.jichuangsi.microservice.common.model.ResponseModel;
import com.jichuangsi.microservice.common.model.UserInfoForToken;
import com.jichuangsi.school.user.exception.UserServiceException;
import com.jichuangsi.school.user.model.transfer.TransferClass;
import com.jichuangsi.school.user.model.transfer.TransferSchool;
import com.jichuangsi.school.user.model.transfer.TransferStudent;
import com.jichuangsi.school.user.model.transfer.TransferTeacher;
import com.jichuangsi.school.user.model.user.StudentModel;
import com.jichuangsi.school.user.model.user.TeacherModel;
import com.jichuangsi.school.user.service.UserInfoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@Api("UserInfoController")
@CrossOrigin
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

    @ApiOperation(value = "根据班级id获取老师信息", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String")
    })
    @GetMapping("/getTeachersForClass/{classId}")
    public ResponseModel<List<TransferTeacher>> getTeachersForClass(@ModelAttribute UserInfoForToken userInfo , @PathVariable String classId){
        try {
            return ResponseModel.sucess("",userInfoService.getTeachersByClassId(classId));
        } catch (UserServiceException e) {
            return ResponseModel.fail("",e.getMessage());
        }
    }

    @ApiOperation(value = "冻结用户", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String")
    })
    @DeleteMapping("/user/coldUser/{userId}")
    public ResponseModel coldTeacher(@ModelAttribute UserInfoForToken userInfo,@PathVariable String userId){
        try {
            userInfoService.coldUserInfo(userId);
        } catch (UserServiceException e) {
            return ResponseModel.fail("",e.getMessage());
        }
        return ResponseModel.sucessWithEmptyData("");
    }

    @ApiOperation(value = "修改老师", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String")
    })
    @PostMapping("/teacher/updateTeacher")
    public ResponseModel updateTeacher(@ModelAttribute UserInfoForToken userInfo, @RequestBody TeacherModel model){
        try {
            userInfoService.updateTeacher(userInfo,model);
        } catch (UserServiceException e) {
            return ResponseModel.fail("",e.getMessage());
        }
        return ResponseModel.sucessWithEmptyData("");
    }

    @ApiOperation(value = "修改学生", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String")
    })
    @PostMapping("/student/updateStudent")
    public ResponseModel updateStudent(@ModelAttribute UserInfoForToken userInfo, @RequestBody StudentModel model){
        try {
            userInfoService.updateStudent(userInfo,model);
        } catch (UserServiceException e) {
            return ResponseModel.fail("",e.getMessage());
        }
        return ResponseModel.sucessWithEmptyData("");
    }

}
