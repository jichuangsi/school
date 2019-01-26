package com.jichuangsi.school.user.controller;

import com.jichuangsi.microservice.common.model.ResponseModel;
import com.jichuangsi.school.user.exception.UserServiceException;
import com.jichuangsi.school.user.model.app.AppInfoModule;
import com.jichuangsi.school.user.model.app.AppInfoQueryModule;
import com.jichuangsi.school.user.service.IAppInfoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@Api("AppInfoController相关的api")
public class AppInfoController {
    @Resource
    private IAppInfoService IAppInfoService;

    //根据账号密码登陆
    @ApiOperation(value = "获取最新app信息", notes = "")
    @PostMapping("/getAppInfo")
    public ResponseModel<AppInfoModule> login(@RequestBody AppInfoQueryModule appInfoQueryModule) throws UserServiceException{
        return ResponseModel.sucess("", IAppInfoService.fetchLastestAppInfo(appInfoQueryModule));
    }
}
