package com.jichuangsi.school.user.controller;

import com.jichuangsi.microservice.common.model.ResponseModel;
import com.jichuangsi.microservice.common.model.UserInfoForToken;
import com.jichuangsi.school.user.entity.AdminBanUrl;
import com.jichuangsi.school.user.entity.Roleurl;
import com.jichuangsi.school.user.model.UrlModel;
import com.jichuangsi.school.user.entity.UrlRelation;
import com.jichuangsi.school.user.exception.BackUserException;
import com.jichuangsi.school.user.exception.UserServiceException;
import com.jichuangsi.school.user.model.UrlMapping;
import com.jichuangsi.school.user.entity.FreeUrl;
import com.jichuangsi.school.user.model.backstage.BackRoleModel;
import com.jichuangsi.school.user.service.IBackRoleService;
import com.jichuangsi.school.user.service.IBackRoleUrlService;
import com.jichuangsi.school.user.service.ISchoolRoleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@RestController
@Api("后台权限控制相关的api")
@CrossOrigin
public class SchoolRoleController {


    @Resource
    private ISchoolRoleService schoolRoleService;
    @Resource
    private IBackRoleUrlService backRoleUrlService;
    @Resource
    private IBackRoleService iBackRoleService;

    @ApiOperation(value = "查询权限信息", notes = "")
    @GetMapping("/urlRole")
    public ResponseModel<List<UrlMapping>> getUrl() throws UserServiceException {
        return ResponseModel.sucess("",schoolRoleService.getAllRole());
    }

    @ApiOperation(value = "查询角色相关权限信息", notes = "")
    @GetMapping("/getAllRole")
    public ResponseModel<List<UrlMapping>> getAllRole() throws UserServiceException {
        try {
            List<BackRoleModel> backRoleList=iBackRoleService.getRoles();
            List<UrlMapping> roleUrl=new ArrayList<UrlMapping>();
            List<String> urlList=null;
            for (BackRoleModel role:backRoleList) {
                urlList=schoolRoleService.getUrlByBackRoleId(role.getRoleId());
                UrlMapping urlMapping=new UrlMapping();
                urlMapping.setRoleName(role.getRoleName());
                urlMapping.setUrlList(urlList);
                roleUrl.add(urlMapping);
            }
            return ResponseModel.sucess("",roleUrl);
        }catch (BackUserException e) {
            return ResponseModel.fail("",e.getMessage());
        }
    }

    @ApiOperation(value = "查询免检查url",notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String")
    })

    @GetMapping("/getAllFreeUrl")
    public ResponseModel<List<FreeUrl>> getAllFreeUrl()throws UserServiceException{
        return ResponseModel.sucess("",schoolRoleService.getFreeUrl());
    }

    @ApiOperation(value = "查询超级管理员不可访问url",notes = "")
    @ApiImplicitParams({
    })
    @GetMapping("/getBanUrlBySuperAdmin")
    public ResponseModel<List<AdminBanUrl>> getBanUrlBySuperAdmin()throws UserServiceException{
        return ResponseModel.sucess("",schoolRoleService.getAdminBanUrl());
    }

    @ApiOperation(value = "添加url", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String")
    })
    @PostMapping("/addUrl")
    public ResponseModel addUrl(@ModelAttribute UserInfoForToken userInfoForToken, @RequestBody Roleurl roleurl) throws UserServiceException {
       try {
           backRoleUrlService.insertRoleUrl(userInfoForToken,roleurl);
       }catch (Exception e){
            return ResponseModel.fail("",e.getMessage());
       }
       return ResponseModel.sucessWithEmptyData("");
    }

    @ApiOperation(value = "添加修改权限url",notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String")
    })
    @PostMapping("/updateUrl")
    public ResponseModel updateUrl(@ModelAttribute UserInfoForToken userInfoForToken,@RequestBody Roleurl roleurl) throws UserServiceException{
        try{
            backRoleUrlService.updateRoleUr(userInfoForToken,roleurl);
        }catch (Exception e){
            return ResponseModel.fail("",e.getMessage());
        }
        return ResponseModel.sucessWithEmptyData("");
    }

    @ApiOperation(value = "根据UrlId查询url",notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String")
    })
    @PostMapping("/selectUrl/{urlId}")
    public ResponseModel<Roleurl> getRoleUrl(@ModelAttribute UserInfoForToken userInfoForToken,@PathVariable String urlId) throws UserServiceException{
        return ResponseModel.sucess("",backRoleUrlService.getUrlByUrlId(userInfoForToken,urlId));
    }

    @ApiOperation(value = "根据id删除url",notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String")
    })
    @PostMapping("/deleteUrl/{urlId}")
    public ResponseModel deleteUrl(@ModelAttribute UserInfoForToken userInfoForToken,@PathVariable String urlId){
        try {
            backRoleUrlService.deleteUrl(userInfoForToken,urlId);
        }catch (Exception e){
            return ResponseModel.fail("",e.getMessage());
        }
        return ResponseModel.sucessWithEmptyData("");
    }

    @ApiOperation(value = "查询全部url",notes = "")
    @PostMapping("/selectAllUrl")
    public ResponseModel select(@ModelAttribute UserInfoForToken userInfoForToken){
        return ResponseModel.sucess("",backRoleUrlService.getAllRoleUrl(userInfoForToken));
    }

    @ApiOperation(value = "分页查询url",notes = "")
    @PostMapping("/selectUrlByPage")
    public ResponseModel selectUrlByPage(@ModelAttribute UserInfoForToken userInfoForToken,@RequestParam int pageNum,@RequestParam int pageSize){
        return ResponseModel.sucess("",  backRoleUrlService.getAllRoleUrlByPage(userInfoForToken,pageNum,pageSize));
    }

    @ApiOperation(value = "根据角色id查询url",notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String")
    })
    @PostMapping("/getUrlByRoleId/{roleId}")
    public ResponseModel getUrlByRoleId(@ModelAttribute UserInfoForToken userInfoForToken,@PathVariable String roleId){
        return ResponseModel.sucess("",schoolRoleService.getUrlByRoleId(roleId));
    }

    @ApiOperation(value = "添加角色可以访问的url", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String")
    })
    @PostMapping("/addRoleUrl")
    public ResponseModel addUrlRelation(@ModelAttribute UserInfoForToken userInfoForToken,@RequestBody UrlRelation urlRelation) throws UserServiceException {
        try {
            backRoleUrlService.insertUrlRelation(userInfoForToken,urlRelation);
        }catch (Exception e){
            return ResponseModel.fail("",e.getMessage());
        }
        return ResponseModel.sucessWithEmptyData("");
    }

    @ApiOperation(value = "批量添加角色可以访问的url", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String")
    })
    @PostMapping("/batchAddRoleUrl")
    public ResponseModel batchAddUrlRelation(@ModelAttribute UserInfoForToken userInfoForToken,@RequestBody UrlModel urlModel) throws UserServiceException {
        try {
            //List<UrlRelation> relations=urlModel.getUrlRelations();
            backRoleUrlService.batchInsertUrlRelation(userInfoForToken,urlModel.getUrlRelations());
        }catch (Exception e){
            return ResponseModel.fail("",e.getMessage());
        }
        return ResponseModel.sucessWithEmptyData("");
    }

    @ApiOperation(value = "根据id删除角色相关url",notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String")
    })
    @PostMapping("/deleteRoleUrl/{roleId}")
    public ResponseModel deleteRoleUrl(@ModelAttribute UserInfoForToken userInfoForToken,@PathVariable String roleId){
        try {
            backRoleUrlService.deleteRoleUrl(userInfoForToken,roleId);
        }catch (Exception e){
            return ResponseModel.fail("",e.getMessage());
        }
        return ResponseModel.sucessWithEmptyData("");
    }

    @ApiOperation(value = "根据id批量删除角色相关url",notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String")
    })
    @PostMapping("/batchDeleteRoleUrl")
    public ResponseModel batchDeleteRoleUrl(@ModelAttribute UserInfoForToken userInfoForToken,@RequestBody UrlModel urlModel){
        try {
            backRoleUrlService.batchDeleteRoleUrl(userInfoForToken,urlModel.getUrlRelations());
        }catch (Exception e){
            return ResponseModel.fail("",e.getMessage());
        }
        return ResponseModel.sucessWithEmptyData("");
    }
}
