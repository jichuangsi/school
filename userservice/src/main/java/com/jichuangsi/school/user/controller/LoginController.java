package com.jichuangsi.school.user.controller;

import com.jichuangsi.microservice.common.model.ResponseModel;
import com.jichuangsi.microservice.common.model.UserInfoForToken;
import com.jichuangsi.school.user.exception.UserServiceException;
import com.jichuangsi.school.user.model.UserBaseInfo;
import com.jichuangsi.school.user.model.UserLoginModel;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Api("LoginController相关的api")
public class LoginController {

    //根据账号密码登陆
    @ApiOperation(value = "根据账号密码登陆，返回用户详情和登陆口令", notes = "")
    @PostMapping("/login")
    public ResponseModel<UserLoginModel> userLogin(@RequestBody UserBaseInfo userBaseInfo) throws UserServiceException {

        return ResponseModel.sucess("",  new UserLoginModel());
    }

    @ApiOperation(value = "test", notes = "")
    @PostMapping("/test")
    public ResponseModel<UserLoginModel> test(@ModelAttribute UserInfoForToken userInfo) throws UserServiceException {

        return ResponseModel.sucess("",  null);
    }
}
