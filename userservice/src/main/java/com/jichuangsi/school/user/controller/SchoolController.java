package com.jichuangsi.school.user.controller;

import com.jichuangsi.microservice.common.model.ResponseModel;
import com.jichuangsi.microservice.common.model.UserInfoForToken;
import com.jichuangsi.school.user.exception.SchoolServiceException;
import com.jichuangsi.school.user.model.school.GradeModel;
import com.jichuangsi.school.user.model.school.SchoolModel;
import com.jichuangsi.school.user.service.ISchoolService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/school")
@Api("关于学校和年级的contoller")
public class SchoolController {

    @Resource
    private ISchoolService schoolService;

    @ApiOperation(value = "学校的增加", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String")
    })
    @PostMapping(value = "/insertSchool",consumes =  "application/json")
    public ResponseModel insertSchool(@ModelAttribute UserInfoForToken userInfo, @RequestBody SchoolModel model){
        try {
            schoolService.insertSchool(model);
        } catch (SchoolServiceException e) {
            return ResponseModel.fail("",e.getMessage());
        }
        return ResponseModel.sucessWithEmptyData("");
    }

    @ApiOperation(value = "学校的修改", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String")
    })
    @PostMapping(value = "/updateSchool",consumes =  "application/json")
    public ResponseModel updateSchool(@ModelAttribute UserInfoForToken userInfo,@RequestBody SchoolModel model){
        try {
            schoolService.updateSchool(model);
        } catch (SchoolServiceException e) {
            return ResponseModel.fail("",e.getMessage());
        }
        return ResponseModel.sucessWithEmptyData("");
    }

    @ApiOperation(value = "年级的添加", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String")
    })
    @PostMapping(value = "/grade/insertGrade",consumes =  "application/json")
    public ResponseModel insertGrade(@ModelAttribute UserInfoForToken userInfo, @RequestBody GradeModel model){
        try {
            schoolService.insertGrade(model);
        } catch (SchoolServiceException e) {
            return ResponseModel.fail("",e.getMessage());
        }
        return  ResponseModel.sucessWithEmptyData("");
    }

    @ApiOperation(value = "年级的添加", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String")
    })
    @PostMapping(value = "/grade/updateGrade",consumes =  "application/json")
    public ResponseModel updateGrade(@ModelAttribute UserInfoForToken userInfo,@RequestBody GradeModel model){
        return ResponseModel.sucessWithEmptyData("");
    }
}
