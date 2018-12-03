package com.jichuangsi.school.user.controller;

import com.jichuangsi.microservice.common.model.ResponseModel;
import com.jichuangsi.microservice.common.model.UserInfoForToken;
import com.jichuangsi.school.user.constant.MyResultCode;
import com.jichuangsi.school.user.exception.ClassServiceException;
import com.jichuangsi.school.user.model.org.Class;
import com.jichuangsi.school.user.service.ISchoolClassService;
import com.jichuangsi.school.user.service.UserInfoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@Api("关于班级的crud")
@RequestMapping("/class")
public class ClassCRUDController {

    @Resource
    private ISchoolClassService schoolClassService;

    @Resource
    private UserInfoService userInfoService;

    @ApiOperation(value = "班级的增加，修改", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String")
    })
    @PostMapping(value = "/saveClass/{gradeId}",consumes =  "application/json")
    public ResponseModel saveClass(@ModelAttribute UserInfoForToken userInfo, @PathVariable String gradeId, @RequestBody Class classModel) throws ClassServiceException{
        if(userInfo==null||classModel==null){
            throw new ClassServiceException(MyResultCode.PARAM_NOT_EXIST);
        }
        schoolClassService.saveOrUpClass(userInfoService.getSchoolInfoById(userInfo.getUserId()).getSchoolId(), gradeId, classModel);
        return ResponseModel.sucessWithEmptyData("");
    }


    @ApiOperation(value = "班级的删除", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String")
    })
    @DeleteMapping(value = "/deleteClass/{gradeId}/{classId}",consumes =  "application/json")
    public ResponseModel deleteClass(@ModelAttribute UserInfoForToken userInfo, @PathVariable String gradeId, @PathVariable String classId) throws ClassServiceException {
        if(userInfo==null|| StringUtils.isEmpty(gradeId)|| StringUtils.isEmpty(classId)){
            throw new ClassServiceException(MyResultCode.PARAM_NOT_EXIST);
        }
        schoolClassService.deleteClass(userInfoService.getSchoolInfoById(userInfo.getUserId()).getSchoolId(), gradeId, classId);
        return ResponseModel.sucessWithEmptyData("");
    }

    @ApiOperation(value = "班级的查询", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String")
    })
    @GetMapping(value = "/findClass/{gradeId}/{classId}",consumes =  "application/json")
    public ResponseModel<Class> findClass(@ModelAttribute UserInfoForToken userInfo, @PathVariable String gradeId, @PathVariable String classId) throws ClassServiceException{
        if(userInfo==null|| StringUtils.isEmpty(gradeId)|| StringUtils.isEmpty(classId)){
            throw new ClassServiceException(MyResultCode.PARAM_NOT_EXIST);
        }
        return ResponseModel.sucess("",schoolClassService.getClassInfo(userInfoService.getSchoolInfoById(userInfo.getUserId()).getSchoolId(), gradeId, classId));
    }
}
