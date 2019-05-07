package com.jichuangsi.school.user.controller;

import com.github.pagehelper.PageInfo;
import com.jichuangsi.microservice.common.model.ResponseModel;
import com.jichuangsi.microservice.common.model.UserInfoForToken;
import com.jichuangsi.school.user.constant.MyResultCode;
import com.jichuangsi.school.user.exception.ClassServiceException;
import com.jichuangsi.school.user.exception.SchoolServiceException;
import com.jichuangsi.school.user.model.org.ClassModel;
import com.jichuangsi.school.user.model.school.TeacherInsertModel;
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
@CrossOrigin
public class ClassCRUDController {

    @Resource
    private ISchoolClassService schoolClassService;

    @Resource
    private UserInfoService userInfoService;

    @ApiOperation(value = "班级的增加，修改", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String")
    })
    @PostMapping(value = "/saveClass", consumes = "application/json")
    public ResponseModel saveClass(@ModelAttribute UserInfoForToken userInfo, @RequestBody ClassModel classModel) throws ClassServiceException {
        if (userInfo == null || classModel == null) {
            throw new ClassServiceException(MyResultCode.PARAM_NOT_EXIST);
        }
        schoolClassService.saveOrUpClass(classModel.getSchoolId(), classModel.getGradeId(), classModel);
        return ResponseModel.sucessWithEmptyData("");
    }

    @ApiOperation(value = "班级的删除", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String")
    })
    @GetMapping(value = "/deleteClass/{gradeId}/{classId}")
    public ResponseModel deleteClass(@ModelAttribute UserInfoForToken userInfo, @PathVariable String gradeId, @PathVariable String classId) {
        try {
            schoolClassService.deleteClass( gradeId, classId);
        } catch (ClassServiceException e) {
            return ResponseModel.fail("",e.getMessage());
        }
        return ResponseModel.sucessWithEmptyData("");
    }

    @ApiOperation(value = "班级的查询", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String")
    })
    @GetMapping(value = "/findClass/{gradeId}/{classId}")
    public ResponseModel<ClassModel> findClass(@ModelAttribute UserInfoForToken userInfo, @PathVariable String gradeId, @PathVariable String classId) throws ClassServiceException {
        if (userInfo == null || StringUtils.isEmpty(gradeId) || StringUtils.isEmpty(classId)) {
            throw new ClassServiceException(MyResultCode.PARAM_NOT_EXIST);
        }
        return ResponseModel.sucess("", schoolClassService.getClassInfo(userInfoService.getSchoolInfoById(userInfo.getUserId()).getSchoolId(), gradeId, classId));
    }

    @ApiOperation(value = "查询年级内所有班级", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String")
    })
    @GetMapping(value = "/findClasses/{gradeId}")
    public ResponseModel<List<ClassModel>> findClasses(@ModelAttribute UserInfoForToken userInfo, @PathVariable String gradeId){
        try {
            return ResponseModel.sucess("",schoolClassService.getClassesByGradeId(gradeId)) ;
        } catch (SchoolServiceException e) {
            return ResponseModel.fail("",e.getMessage());
        }
    }

    @ApiOperation(value = "查询年级内所有班级", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String")
    })
    @GetMapping(value = "/findClassesInPage/{gradeId}")
    public ResponseModel<PageInfo<ClassModel>> findClassesInPage(@ModelAttribute UserInfoForToken userInfo, @PathVariable String gradeId, @RequestParam("pageIndex") int pageIndex, @RequestParam("pageSize") int pageSize){
        try {
           return ResponseModel.sucess("",schoolClassService.getClassesByGradeIdInPage(gradeId, pageIndex, pageSize)) ;
        } catch (SchoolServiceException e) {
            return ResponseModel.fail("",e.getMessage());
        }
    }

    @ApiOperation(value = "移除班级内教师", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String")
    })
    @GetMapping(value = "/classRemoveTeacher/{classId}/{teacherId}")
    public ResponseModel classRemoveTeacher(@ModelAttribute UserInfoForToken userInfo,@PathVariable("classId") String classId,
                                            @PathVariable String teacherId){
        try {
            schoolClassService.classRemoveTeacher(userInfo, classId, teacherId);
        } catch (SchoolServiceException e) {
            return ResponseModel.fail("",e.getMessage());
        }
        return ResponseModel.sucessWithEmptyData("");
    }

    @ApiOperation(value = "添加班级内教师", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String")
    })
    @PostMapping(value = "/classInsertTeacher/{teacherId}")
    public ResponseModel classInsertTeacher(@ModelAttribute UserInfoForToken userInfo, @RequestBody TeacherInsertModel model,@PathVariable String teacherId){
        try {
            schoolClassService.classInsertTeacher(userInfo, model, teacherId);
        } catch (SchoolServiceException e) {
            return ResponseModel.fail("",e.getMessage());
        }
        return ResponseModel.sucessWithEmptyData("");
    }

    @ApiOperation(value = "修改班级添加学科", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String")
    })
    @GetMapping(value = "/updateClassInsertSubject/{subjectId}/{classId}")
    public ResponseModel updateClassInsertSubject(@ModelAttribute UserInfoForToken userInfo,@PathVariable String subjectId,@PathVariable String classId){
        try {
            schoolClassService.updateClassInsertSubject(userInfo, subjectId, classId);
        } catch (SchoolServiceException e) {
            return ResponseModel.fail("",e.getMessage());
        }
        return ResponseModel.sucessWithEmptyData("");
    }

    @ApiOperation(value = "修改班级删除学科", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String")
    })
    @GetMapping(value = "/updateClassDeleteSubject/{subjectId}/{classId}")
    public ResponseModel updateClassDeleteSubject(@ModelAttribute UserInfoForToken userInfo,@PathVariable String subjectId,@PathVariable String classId){
        try {
            schoolClassService.updateClassDelSubject(userInfo, subjectId, classId);
        } catch (SchoolServiceException e) {
            return ResponseModel.fail("",e.getMessage());
        }
        return ResponseModel.sucessWithEmptyData("");
    }
}
