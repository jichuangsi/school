package cn.com.jichuangsi.school.backstage.controller;

import cn.com.jichuangsi.school.backstage.exception.BackUserException;
import cn.com.jichuangsi.school.backstage.model.BackRoleModel;
import cn.com.jichuangsi.school.backstage.service.IBackRoleService;
import com.jichuangsi.microservice.common.model.ResponseModel;
import com.jichuangsi.microservice.common.model.UserInfoForToken;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/back/role")
@Api("后台角色的更改")
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

}
