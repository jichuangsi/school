package com.jichuangsi.school.user.controller;

import com.jichuangsi.microservice.common.model.ResponseModel;
import com.jichuangsi.microservice.common.model.UserInfoForToken;
import com.jichuangsi.school.user.exception.UserServiceException;
import com.jichuangsi.school.user.model.System.User;
import com.jichuangsi.school.user.model.backstage.UpdatePwdModel;
import com.jichuangsi.school.user.model.user.StudentModel;
import com.jichuangsi.school.user.model.user.TeacherModel;
import com.jichuangsi.school.user.service.IUserPositionService;
import com.jichuangsi.school.user.service.UserInfoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.List;

/**
 * UserCRUDController 控制器类
 *
 * @author
 * @version 1.0
 * @email
 * @date 2018-07-25 16:10:16
 */
@RestController
@Api("UserCRUDController相关的api")
@CrossOrigin
public class UserCRUDController {

    @Resource
    private UserInfoService userInfoService;
    @Resource
    private IUserPositionService userPositionService;

    //获取所有用户
    @ApiOperation(value = "获取所有用户", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String")})
    @GetMapping("/findAll")
    public ResponseModel<List<User>> findAll(@ModelAttribute UserInfoForToken userInfo) throws UserServiceException {
        return ResponseModel.sucess("", userInfoService.findAllUser());
    }

    //根据账号获取指定用户
    @ApiOperation(value = "根据账号获取指定用户", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String")})
    @GetMapping("/findOne")
    public ResponseModel<User> findOne(@ModelAttribute UserInfoForToken userInfo, String id) throws UserServiceException {
        return ResponseModel.sucess("", userInfoService.findUserInfo(id));
    }


    @ApiOperation(value = "根据账号id获取指定用户", notes = "")
    @GetMapping("/findUser")
    public ResponseModel<User> findUser(String id) throws UserServiceException {
        return ResponseModel.sucess("", userInfoService.findUserInfo(id));
    }

    //根据Id查询被删的指定用户
    @ApiOperation(value = "根据Id查询被删的指定用户", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String")})
    @GetMapping("/findDeleteOne")
    public ResponseModel<User> findDeleteOne(@ModelAttribute UserInfoForToken userInfo, String id) throws UserServiceException {
        return ResponseModel.sucess("", userInfoService.findDeleteOne(id));
    }

    //"获取所有被删的用户信息
    @ApiOperation(value = "获取所有被删的用户信息", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String")})
    @GetMapping("/findAllForDelete")
    public ResponseModel<List<User>> findAllForDelete(@ModelAttribute UserInfoForToken userInfo) throws UserServiceException {
        return ResponseModel.sucess("", userInfoService.findAllForDelete());
    }

    //保存用户信息
    @ApiOperation(value = "保存用户信息", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String")})
    @PostMapping("/save")
    public ResponseModel<User> save(@ModelAttribute UserInfoForToken userInfo, @RequestBody User user) throws UserServiceException {
//        user.getRoles().clear();
        return ResponseModel.sucess("", userInfoService.saveUserInfo(user));
    }

    //更新用户信息
    @ApiOperation(value = "更新用户信息", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String")})
    @PostMapping("/update")
    public ResponseModel<User> update(@ModelAttribute UserInfoForToken userInfo, @RequestBody User user) throws UserServiceException {
        return ResponseModel.sucess("", userInfoService.UpdateUserInfo(user));
    }

    //批量删除用户信息
    @ApiOperation(value = "批量删除用户信息", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String")})

    @GetMapping("/delete")
    public ResponseModel<String> delete(@ModelAttribute UserInfoForToken userInfo, String[] ids) throws UserServiceException {
//        System.out.println("来到这里了没");
//        for (String id:ids
//             ) {
//            System.out.println(id);
//        }
        try {
            userInfoService.deleteUserInfo(ids);
        } catch (UserServiceException e) {
            return ResponseModel.fail("",e.getMessage());
        }
        return ResponseModel.sucessWithEmptyData("");
    }

    //批量真正删除用户信息
    @ApiOperation(value = "批量真正的删除用户信息", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String")})
    @GetMapping("/TrulyDeleted")
    public ResponseModel<String> TrulyDeleted(@ModelAttribute UserInfoForToken userInfo, String[] ids) throws UserServiceException {
        userInfoService.TrulyDeleted(ids);
        return ResponseModel.sucessWithEmptyData("");
    }

    //根据用户id删除用户信息
    @ApiOperation(value = "根据用户账号删除用户信息", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String")})
    @DeleteMapping("/deleteById/{id}")
    public ResponseModel<String> delete(@ModelAttribute UserInfoForToken userInfo, @PathVariable String id) throws UserServiceException {
        ;
        return ResponseModel.sucess("", String.valueOf(userInfoService.deleteById(id)));
    }

    //根据用户id恢复用户信息
    @ApiOperation(value = "根据用户id恢复用户信息", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String")})
    @GetMapping("/restoreById/{id}")
    public ResponseModel<String> RestoreById(@ModelAttribute UserInfoForToken userInfo, @PathVariable String id) throws UserServiceException {
        ;
        return ResponseModel.sucess("", String.valueOf(userInfoService.restoreById(id)));
    }

    //根据用户id批量恢复用户信息
    @ApiOperation(value = "根据用户id批量恢复用户信息", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String")})
    @GetMapping("/restore")
    public ResponseModel<String> RestoreUsers(@ModelAttribute UserInfoForToken userInfo, String[] ids) throws UserServiceException {
        userInfoService.restoreUsers(ids);
        return ResponseModel.sucessWithEmptyData("");
    }

    @ApiOperation(value = "保存老师信息", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String")})
    @PostMapping("/saveTeacher")
    public ResponseModel saveTeacher(@ModelAttribute UserInfoForToken userInfo, @RequestBody TeacherModel model){
        try {
            userInfoService.saveTeacher(userInfo,model);
        } catch (UserServiceException e) {
            return ResponseModel.fail("",e.getMessage());
        }
        return ResponseModel.sucessWithEmptyData("");
    }

    @ApiOperation(value = "保存学生信息", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String")})
    @PostMapping("/saveStudent")
    public ResponseModel saveStudent(@ModelAttribute UserInfoForToken userInfo, @RequestBody StudentModel model){
        try {
            userInfoService.saveStudent(userInfo, model);
        } catch (UserServiceException e) {
            return ResponseModel.fail("",e.getMessage());
        }
        return ResponseModel.sucessWithEmptyData("");
    }

    @ApiOperation(value = "excel批量保存学生信息", notes = "")
    @ApiImplicitParams({
             @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String")})
    @PostMapping(value = "/excel/saveStudentByExcel")
    public ResponseModel<String> saveStudentByExcel(@RequestParam MultipartFile file,@ModelAttribute UserInfoForToken userInfo){
        try {
            return ResponseModel.sucess("",userInfoService.saveExcelStudents(file,userInfo));
        } catch (UserServiceException e) {
            return ResponseModel.fail("",e.getMessage());
        }
    }

    @ApiOperation(value = "非本人修改账号密码", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String")
    })
    @PostMapping(value = "/updateOtherPwd/{userId}")
    public ResponseModel updateOtherPwd(@ModelAttribute UserInfoForToken userInfo, @Validated @RequestBody UpdatePwdModel model, @PathVariable String userId){
        try {
            userInfoService.updateOtherPwd(userInfo, model, userId);
        } catch (UserServiceException e) {
            return ResponseModel.fail("",e.getMessage());
        }
        return ResponseModel.sucessWithEmptyData("");
    }
}
