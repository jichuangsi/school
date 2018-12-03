package com.jichuangsi.school.user.controller;

import com.jichuangsi.microservice.common.model.ResponseModel;
import com.jichuangsi.school.user.exception.UserServiceException;
import com.jichuangsi.school.user.model.System.User;
import com.jichuangsi.school.user.model.UserLoginModel;
import com.jichuangsi.school.user.repository.UserRepository;
import com.jichuangsi.school.user.service.ITokenService;
import com.jichuangsi.school.user.service.LoginService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@Api("LoginController相关的api")
public class LoginController {
    @Resource
    private UserRepository userService;
    @Resource
    private ITokenService itokenService;
    @Resource
    private LoginService loginService;

    //根据账号密码登陆
    @ApiOperation(value = "根据账号密码登陆，返回用户详情和登陆口令", notes = "")
    @PostMapping("/login")
    public ResponseModel<UserLoginModel> login(@RequestBody User user) throws UserServiceException{
        return ResponseModel.sucess("", loginService.login(user));
    }

    //根据信息完成用户注册
    @ApiOperation(value = "根据信息完成用户注册", notes = "")
    @PostMapping("/register")
    public ResponseModel register(@RequestBody User user) throws UserServiceException{
        return ResponseModel.sucess("",loginService.register(user));
    }

}
