package com.jichuangsi.school.user.controller;

import com.github.pagehelper.PageInfo;
import com.jichuangsi.microservice.common.model.ResponseModel;
import com.jichuangsi.microservice.common.model.UserInfoForToken;
import com.jichuangsi.school.user.exception.UserServiceException;
import com.jichuangsi.school.user.model.school.SchoolRoleModel;
import com.jichuangsi.school.user.model.school.UserConditionModel;
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


    @ApiOperation(value = "获取指定班级的学生信息", notes = "")
    @GetMapping("/getStudentsForClassInPage")
    public ResponseModel<PageInfo<TransferStudent>> getStudentsForClassInPage(@ModelAttribute UserInfoForToken userInfo , @RequestParam(value = "classId") String classId, @RequestParam("pageIndex") int pageIndex, @RequestParam("pageSize") int pageSize) throws UserServiceException{
        return ResponseModel.sucess("", userInfoService.getStudentsByClassIdInPage(classId, pageIndex, pageSize));
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

    @ApiOperation(value = "添加系统用户角色", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String")
    })
    @PostMapping("/insertSystemRole/{schoolId}")
    public ResponseModel insertSystemRole(@ModelAttribute UserInfoForToken userInfo, @RequestBody SchoolRoleModel model,@PathVariable String schoolId){
        try {
            userInfoService.insertSchoolRole(userInfo, model,schoolId);
        } catch (UserServiceException e) {
            return ResponseModel.fail("",e.getMessage());
        }
        return ResponseModel.sucessWithEmptyData("");
    }

    @ApiOperation(value = "修改系统用户角色", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String")
    })
    @PostMapping("/updateSystemRole")
    public ResponseModel updateSystemRole(@ModelAttribute UserInfoForToken userInfo,@RequestBody SchoolRoleModel model){
        try {
            userInfoService.updateSchoolRole(userInfo, model);
        } catch (UserServiceException e) {
            return ResponseModel.fail("",e.getMessage());
        }
        return ResponseModel.sucessWithEmptyData("");
    }

    @ApiOperation(value = "查看系统用户角色", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String")
    })
    @GetMapping("/getSystemRoles/{schoolId}")
    public ResponseModel<List<SchoolRoleModel>> getSystemRoles(@ModelAttribute UserInfoForToken userInfo,@PathVariable String schoolId){
        try {
            return ResponseModel.sucess("",userInfoService.getSchoolRoles(userInfo,schoolId));
        } catch (UserServiceException e) {
            return ResponseModel.fail("",e.getMessage());
        }
    }

    @ApiOperation(value = "删除系统用户角色", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String")
    })
    @DeleteMapping("/deleteSystemRole/{roleId}")
    public ResponseModel deleteSystemRole(@ModelAttribute UserInfoForToken userInfo,@PathVariable String roleId){
        try {
            userInfoService.deleteSchoolRole(userInfo, roleId);
        } catch (UserServiceException e) {
            return ResponseModel.fail("",e.getMessage());
        }
        return ResponseModel.sucessWithEmptyData("");
    }

    @ApiOperation(value = "查询本校老师信息", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String")
    })
    @GetMapping("/teacher/getTeachers/{schoolId}")
    public ResponseModel<PageInfo<TeacherModel>> getTeachers(@ModelAttribute UserInfoForToken userInfo,@PathVariable String schoolId,@RequestParam("pageIndex") int pageIndex,@RequestParam("pageSize") int pageSize){
        try {
            return ResponseModel.sucess("",userInfoService.getTeachers(userInfo, schoolId,pageIndex,pageSize));
        } catch (UserServiceException e) {
            return ResponseModel.fail("",e.getMessage());
        }
    }

    @ApiOperation(value = "根据条件查老师", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String")
    })
    @PostMapping(value = "/teacher/getTeacherByCondition")
    public ResponseModel<PageInfo<TeacherModel>> getTeacherByCondition(@ModelAttribute UserInfoForToken userInfo, @RequestBody UserConditionModel model){
        try {
            return ResponseModel.sucess("",userInfoService.getTeachersByCondition(userInfo, model));
        } catch (UserServiceException e) {
            return ResponseModel.fail("",e.getMessage());
        }
    }

    @ApiOperation(value = "根据条件查学生", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String")
    })
    @PostMapping("/student/getStudentByCondition")
    public ResponseModel<PageInfo<StudentModel>> getStudentByCondition(@ModelAttribute UserInfoForToken userInfo,@RequestBody UserConditionModel model){
        try {
            return ResponseModel.sucess("",userInfoService.getStudentByCondition(userInfo, model));
        } catch (UserServiceException e) {
            return ResponseModel.fail("",e.getMessage());
        }
    }
}
