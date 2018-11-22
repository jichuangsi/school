package com.jichuangsi.school.user.controller;


import com.jichuangsi.microservice.common.model.ResponseModel;
import com.jichuangsi.microservice.common.model.UserInfoForToken;
import com.jichuangsi.school.user.entity.UserInfo;
import com.jichuangsi.school.user.exception.UserServiceException;
import com.jichuangsi.school.user.service.UserInfoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * UserCRUDController 控制器类
 * @author
 * @email
 * @date 2018-07-25 16:10:16
 * @version 1.0
 */
@RestController
@Api("UserCRUDController相关的api")
public class UserCRUDController {

    @Autowired
    private UserInfoService userInfoService;

    //获取所有用户
    @ApiOperation(value = "获取所有用户", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String")})
    @GetMapping("/findAll")
    public @ResponseBody ResponseModel<List<UserInfo>> findAll(@ModelAttribute UserInfoForToken userInfo)throws UserServiceException{
        return ResponseModel.sucess("",userInfoService.findAllUser()) ;
    }

    //根据账号获取指定用户
    @ApiOperation(value = "根据账号获取指定用户", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String")})
    @GetMapping("/findOne/{id}")
    public   ResponseModel<UserInfo> findOne(@ModelAttribute UserInfoForToken userInfo, @PathVariable String id) throws UserServiceException {
        return ResponseModel.sucess("",userInfoService.findUserInfo(id));

    }

    //保存用户信息
    @ApiOperation(value = "保存用户信息", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String")})
    @PostMapping("/save")
    public ResponseModel<UserInfo> save(@ModelAttribute UserInfoForToken userInfo, @RequestBody UserInfo user) throws UserServiceException{
        return ResponseModel.sucess("", userInfoService.saveUserInfo(user));
    }

    //更新用户信息
    @ApiOperation(value = "更新用户信息", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String")})
    @PutMapping("/update")
    public ResponseModel update(@ModelAttribute UserInfoForToken userInfo, @RequestBody UserInfo user) throws UserServiceException{

        return ResponseModel.sucess("",userInfoService.UpdateUserInfo(user));


    }

    //批量删除用户信息
    @ApiOperation(value = "批量删除用户信息", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String")})
    @GetMapping("/delete")
    public ResponseModel<List<String>> delete(@ModelAttribute UserInfoForToken userInfo, String[] ids)throws UserServiceException {
        return ResponseModel.sucess("",userInfoService.deleteUserInfo(ids));
    }

    //根据用户id删除用户信息
    @ApiOperation(value = "根据用户账号删除用户信息", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String")})
    @GetMapping("/deleteById/{id}")
    public ResponseModel<String> delete (@ModelAttribute UserInfoForToken userInfo, @PathVariable String id) throws UserServiceException {
        System.out.println(id);
        return ResponseModel.sucess("",userInfoService.delteById(id));
    }

    //根据用户id恢复用户信息
    @ApiOperation(value = "根据用户id恢复用户信息", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String")})
    @GetMapping("/restoreById/{id}")
    public ResponseModel<String> RestoreById (@ModelAttribute UserInfoForToken userInfo, @PathVariable String id)throws UserServiceException {
        return ResponseModel.sucess("",userInfoService.RestoreById(id)) ;
    }

    //根据用户id批量恢复用户信息
    @ApiOperation(value = "根据用户id批量恢复用户信息", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String")})
    @GetMapping("/restore")
    public void RestoreUsers(@ModelAttribute UserInfoForToken userInfo, String[] ids)throws UserServiceException {

        userInfoService.RestoreUsers(ids);

    }
}
