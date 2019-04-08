package com.jichuangsi.school.user.controller.backstage;

import com.jichuangsi.microservice.common.model.ResponseModel;
import com.jichuangsi.microservice.common.model.UserInfoForToken;
import com.jichuangsi.school.user.exception.BackUserException;
import com.jichuangsi.school.user.model.backstage.BackRoleModel;
import com.jichuangsi.school.user.model.backstage.orz.PromisedModel;
import com.jichuangsi.school.user.service.IBackRoleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/back/role")
@Api("后台角色的更改")
@CrossOrigin
public class BackRoleInfoController {

    @Resource
    private IBackRoleService backRoleService;

    @ApiOperation(value = "后台添加角色", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String")
    })
    @PostMapping(value = "/insertRole")
    public ResponseModel insertRole(@ModelAttribute UserInfoForToken userInfo, @RequestBody BackRoleModel model){
        try {
            backRoleService.insertRole(userInfo,model);
        } catch (BackUserException e) {
            return ResponseModel.fail("",e.getMessage());
        }
        return ResponseModel.sucessWithEmptyData("");
    }

    @ApiOperation(value = "后台删除角色", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String")
    })
    @DeleteMapping(value = "/deleteRole/{roleId}")
    public ResponseModel deleteRole(@ModelAttribute UserInfoForToken userInfo,@PathVariable String roleId){
        try {
            backRoleService.deleteRole(userInfo, roleId);
        } catch (BackUserException e) {
            return ResponseModel.fail("",e.getMessage());
        }
        return ResponseModel.sucessWithEmptyData("");
    }

    @ApiOperation(value = "给用户绑定角色", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String")
    })
    @GetMapping(value = "/bindRole/{roleId}/{userId}")
    public  ResponseModel bindRole(@ModelAttribute UserInfoForToken userInfo , @PathVariable("roleId")String roleId,@PathVariable("userId") String userId){
        try {
            backRoleService.bindRole(userInfo, roleId, userId);
        } catch (BackUserException e) {
            return ResponseModel.fail("",e.getMessage());
        }
        return ResponseModel.sucessWithEmptyData("");
    }

    @ApiOperation(value = "后台添加允许路径", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String")
    })
    @PostMapping(value = "/insertPromised")
    public ResponseModel insertPromised(@ModelAttribute UserInfoForToken userInfo , @RequestBody PromisedModel model){
        try {
            backRoleService.insertPromised(userInfo, model);
        } catch (BackUserException e) {
            return ResponseModel.fail("",e.getMessage());
        }
        return ResponseModel.sucessWithEmptyData("");
    }

    @ApiOperation(value = "查看后台角色", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String")
    })
    @GetMapping(value = "/getRoles")
    public ResponseModel<List<BackRoleModel>> getRoles(@ModelAttribute UserInfoForToken userInfo){
        try {
            return ResponseModel.sucess("",backRoleService.getRoles(userInfo));
        } catch (BackUserException e) {
            return ResponseModel.fail("",e.getMessage());
        }
    }

    @ApiOperation(value = "角色绑定允许路径", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String")
    })
    @PostMapping(value = "/role/bindPromised/{roleId}")
    public ResponseModel bindPromised(@ModelAttribute UserInfoForToken userInfo , @RequestBody List<PromisedModel> models ,@PathVariable String roleId){
        try {
            backRoleService.bindPromised(userInfo, roleId, models);
        } catch (BackUserException e) {
            return ResponseModel.fail("",e.getMessage());
        }
        return ResponseModel.sucessWithEmptyData("");
    }

    @ApiOperation(value = "后台查看允许路径", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String")
    })
    @GetMapping(value = "/getPromised")
    public ResponseModel<List<PromisedModel>> getPromised(@ModelAttribute UserInfoForToken userInfo){
        try {
            return ResponseModel.sucess("",backRoleService.getPromised(userInfo));
        } catch (BackUserException e) {
            return ResponseModel.fail("",e.getMessage());
        }
    }

    @ApiOperation(value = "删除允许路径", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String")
    })
    @DeleteMapping(value = "/deletePromised/{promisedId}")
    public ResponseModel deletePromised(@ModelAttribute UserInfoForToken userInfo,@PathVariable String promisedId){
        try {
            backRoleService.deletePromised(userInfo, promisedId);
        } catch (BackUserException e) {
            return ResponseModel.fail("",e.getMessage());
        }
        return ResponseModel.sucessWithEmptyData("");
    }
}
