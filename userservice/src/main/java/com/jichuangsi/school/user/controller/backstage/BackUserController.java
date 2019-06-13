package com.jichuangsi.school.user.controller.backstage;

import com.jichuangsi.microservice.common.model.ResponseModel;
import com.jichuangsi.microservice.common.model.UserInfoForToken;
import com.jichuangsi.school.user.exception.BackUserException;
import com.jichuangsi.school.user.model.backstage.BackUserModel;
import com.jichuangsi.school.user.model.backstage.UpdatePwdModel;
import com.jichuangsi.school.user.model.school.SchoolModel;
import com.jichuangsi.school.user.service.IBackUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/back/user")
@Api("后台登录,注册，修改密码，注销账户")
@CrossOrigin
public class BackUserController {

    @Resource
    private IBackUserService backUserService;

    @ApiOperation(value = "注册后台用户", notes = "")
    @ApiImplicitParams({})
    @PostMapping(value = "/registAccount")
    public ResponseModel<String> registAccount(@Validated @RequestBody BackUserModel model){
        try {
            return ResponseModel.sucess("",backUserService.registBackUser(model));
        } catch (BackUserException e) {
            return ResponseModel.fail("",e.getMessage());
        }

    }

    @ApiOperation(value = "后台登录", notes = "")
    @ApiImplicitParams({})
    @PostMapping(value = "/login")
    public ResponseModel<String> backStageLogin(@RequestBody BackUserModel model){
        try {
            return ResponseModel.sucess("",backUserService.loginBackUser(model));
        } catch (BackUserException e) {
            return ResponseModel.fail("",e.getMessage());
        }
    }

    @ApiOperation(value = "根据id获得后台角色", notes = "")
    @ApiImplicitParams({})
    @GetMapping(value = "/getBackUserById")
    public ResponseModel<BackUserModel> getBackUserById(@RequestParam("id") String id){
        try {
            return ResponseModel.sucess("",backUserService.getBackUserById(id));
        } catch (BackUserException e) {
            return ResponseModel.fail("",e.getMessage());
        }
    }

    @ApiOperation(value = "后台注销账户", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String")
    })
    @DeleteMapping(value = "/deleteAccount")
    public ResponseModel deleteAccount(@ModelAttribute UserInfoForToken userInfo,@RequestBody BackUserModel model){
        try {
            backUserService.deleteBackUser(userInfo, model);
        } catch (BackUserException e) {
            return ResponseModel.fail("",e.getMessage());
        }
        return ResponseModel.sucessWithEmptyData("");
    }

    @ApiOperation(value = "后台本人修改账号密码", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String")
    })
    @PostMapping(value = "/updatePwd")
    public ResponseModel updatePwd(@ModelAttribute UserInfoForToken userInfo, @Validated @RequestBody UpdatePwdModel model){
        try {
            backUserService.updateBackUserPwd(userInfo, model);
        } catch (BackUserException e) {
            return ResponseModel.fail("",e.getMessage());
        }
        return ResponseModel.sucessWithEmptyData("");
    }

    @ApiOperation(value = "查询后台所有登记内非删除学校", notes = "")
    @ApiImplicitParams({})
    @GetMapping(value = "/getBackSchools")
    public ResponseModel<List<SchoolModel>> getBackSchools(){
        try {
            return ResponseModel.sucess("",backUserService.getBackSchools());
        } catch (BackUserException e) {
            return ResponseModel.fail("",e.getMessage());
        }
    }

    @ApiOperation(value = "查询本校所有的用户", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String")
    })
    @GetMapping(value = "/getSchoolUserInfo")
    public ResponseModel<List<BackUserModel>>  getSchoolUserInfo(@ModelAttribute UserInfoForToken userInfo){
        try {
            return ResponseModel.sucess("",backUserService.getSchoolUserInfo(userInfo));
        } catch (BackUserException e) {
            return ResponseModel.fail("",e.getMessage());
        }
    }

    @ApiOperation(value = "返回本人信息及可访问路径", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String")
    })
    @GetMapping(value = "/getUserInfoAndPromised")
    public ResponseModel<BackUserModel> getUserInfoAndPromised(@ModelAttribute UserInfoForToken userInfo){
        try {
            return ResponseModel.sucess("",backUserService.getUserInfoAndPromised(userInfo));
        } catch (BackUserException e) {
            return ResponseModel.fail("",e.getMessage());
        }
    }

    @ApiOperation(value = "非本人修改账号密码", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String")
    })
    @PostMapping(value = "/updateOtherPwd/{userId}")
    public ResponseModel updateOtherPwd(@ModelAttribute UserInfoForToken userInfo,@Validated @RequestBody UpdatePwdModel model,@PathVariable String userId){
        try {
            backUserService.updateOtherPwd(userInfo, model, userId);
        } catch (BackUserException e) {
            return ResponseModel.fail("",e.getMessage());
        }
        return ResponseModel.sucessWithEmptyData("");
    }
}
