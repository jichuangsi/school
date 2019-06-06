package com.jichuangsi.school.user.controller;

import com.jichuangsi.microservice.common.model.ResponseModel;
import com.jichuangsi.microservice.common.model.UserInfoForToken;
import com.jichuangsi.school.user.entity.UserPosition;
import com.jichuangsi.school.user.service.IUserPositionService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@CrossOrigin
@Api("试卷分享相关api")
public class UserShareContrller {
    @Resource
    private IUserPositionService iUserPositionService;
    @ApiOperation(value = "添加UserPosition")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String")
    })
    @PostMapping("/addUserPosition")
    public ResponseModel insertUserPosition(UserInfoForToken userInfoForToken, @RequestBody UserPosition userPosition){
        try {
            iUserPositionService.insertUserPosition(userInfoForToken,userPosition);
        }catch (Exception e){
            return ResponseModel.fail("",e.getMessage());
        }
        return ResponseModel.sucessWithEmptyData("");
    }

    @ApiOperation(value = "删除UserPosition")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String")
    })
    @PostMapping("/deleteUserPosition")
    public ResponseModel deleteUserPosition(UserInfoForToken userInfoForToken, @PathVariable String userId){
        try {
            iUserPositionService.deleteUserPosition(userInfoForToken,userId);
        }catch (Exception e){
            return ResponseModel.fail("",e.getMessage());
        }
        return ResponseModel.sucessWithEmptyData("");
    }

    @ApiOperation(value = "修改UserPosition")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String")
    })
    @PostMapping("/deleteUserPosition/{userId}")
    public ResponseModel updateUserPosition(UserInfoForToken userInfoForToken, @PathVariable String userId){
        try {
            iUserPositionService.updateUserPosition(userInfoForToken,userId);
        }catch (Exception e){
            return ResponseModel.fail("",e.getMessage());
        }
        return ResponseModel.sucessWithEmptyData("");
    }

    @ApiOperation(value = "查询UserPosition")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String")
    })
    @PostMapping("/getUserPosition")
    public ResponseModel<List<UserPosition>> selectUserPosition(UserInfoForToken userInfoForToken){
       return ResponseModel.sucess("",iUserPositionService.getAllUserPosition(userInfoForToken));
    }
}
