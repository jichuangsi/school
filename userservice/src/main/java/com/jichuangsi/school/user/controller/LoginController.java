package com.jichuangsi.school.user.controller;

import com.jichuangsi.microservice.common.model.ResponseModel;
import com.jichuangsi.school.user.commons.MyResultCode;
import com.jichuangsi.school.user.entity.UserInfo;
import com.jichuangsi.school.user.exception.UserServiceException;
import com.jichuangsi.school.user.model.UserBaseInfo;
import com.jichuangsi.school.user.model.UserLoginModel;
import com.jichuangsi.school.user.repository.UserRepository;
import com.jichuangsi.school.user.service.ITokenService;
import com.jichuangsi.school.user.service.LoginService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Api("LoginController相关的api")
public class LoginController {
    @Autowired
    private UserRepository userService;
    @Autowired
    private ITokenService itokenService;
    @Autowired
    private LoginService loginService;

    //根据账号密码登陆
    @ApiOperation(value = "根据账号密码登陆，返回用户详情和登陆口令", notes = "")
    @PostMapping("/login")
    public ResponseModel<UserLoginModel> login(@RequestBody UserBaseInfo userBaseInfo) throws UserServiceException{
        return ResponseModel.sucess("", loginService.login(userBaseInfo));
    }

    //根据信息完成用户注册
    @ApiOperation(value = "根据信息完成用户注册", notes = "")
    @PostMapping("/register")
    public ResponseModel firstlogin(@RequestBody UserInfo userInfo){
        //如果正确了返回用户信息
        if (loginService.regist(userInfo)){
            return ResponseModel.sucess("",userInfo);
        }else {
            return ResponseModel.fail("", MyResultCode.COMPARE);
        }
    }

}
