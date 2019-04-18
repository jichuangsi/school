package com.jichuangsi.school.user.controller.backstage;

import com.jichuangsi.microservice.common.model.ResponseModel;
import com.jichuangsi.microservice.common.model.UserInfoForToken;
import com.jichuangsi.school.user.exception.BackUserException;
import com.jichuangsi.school.user.model.SchoolMessageModel;
import com.jichuangsi.school.user.model.backstage.TimeTableModel;
import com.jichuangsi.school.user.model.file.UserFile;
import com.jichuangsi.school.user.service.IBackSchoolService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/back/school")
@Api("后台学校操作")
@CrossOrigin
public class BackSchoolController {

    @Resource
    private IBackSchoolService backSchoolService;

    @ApiOperation(value = "对班级查询课程表", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String")
    })
    @GetMapping(value = "/getTimeTableByClass/{classId}")
    public ResponseModel<TimeTableModel> getTimeTableByClass(@ModelAttribute UserInfoForToken userInfo, @PathVariable String classId){
        try {
            return ResponseModel.sucess("",backSchoolService.findByClassId(classId));
        } catch (BackUserException e) {
            return ResponseModel.fail("",e.getMessage());
        }
    }

    @ApiOperation(value = "excel添加班级课程表", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String")
    })
    @PostMapping(value = "/saveTimeTableByClass/{classId}")
    public ResponseModel saveTimeTableByClass(@RequestParam MultipartFile file,@ModelAttribute UserInfoForToken userInfo, @PathVariable String classId){
        try {
            backSchoolService.saveTimeTableByExcel(file, classId, userInfo);
        } catch (BackUserException e) {
            return ResponseModel.fail("",e.getMessage());
        }
        return ResponseModel.sucessWithEmptyData("");
    }

    @ApiOperation(value = "删除班级课程表", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String")
    })
    @DeleteMapping(value = "/deleteClassTimeTable/{tableId}/{classId}")
    public ResponseModel deleteClassTimeTable(@ModelAttribute UserInfoForToken userInfo,@PathVariable String tableId,@PathVariable String classId){
        try {
            backSchoolService.delTimeTable(tableId, classId, userInfo);
        } catch (BackUserException e) {
            return ResponseModel.fail("",e.getMessage());
        }
        return ResponseModel.sucessWithEmptyData("");
    }

    @ApiOperation(value = "获取附件列表", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String")
    })
    @GetMapping(value = "getAttachments")
    public ResponseModel<List<UserFile>> getAttachments(@ModelAttribute UserInfoForToken userInfo){
        try {
            return ResponseModel.sucess("",backSchoolService.getAttachments(userInfo));
        } catch (BackUserException e) {
            return ResponseModel.fail("",e.getMessage());
        }
    }

    @ApiOperation(value = "附件下载", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String")
    })
    @PostMapping(value = "downAttachment/{subName}")
    public ResponseModel<UserFile> downAttachment(@ModelAttribute UserInfoForToken userInfo, @PathVariable String subName){
        try {
            return ResponseModel.sucess("",backSchoolService.downAttachment(userInfo, subName));
        } catch (Exception e) {
            return ResponseModel.fail("",e.getMessage());
        }
    }

    @ApiOperation(value = "校园信息发布", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String")
    })
    @PostMapping(value = "/sendSchoolMessage/{schoolId}")
    public ResponseModel sendSchoolMessage(@ModelAttribute UserInfoForToken userInfo, @PathVariable String schoolId, @RequestBody SchoolMessageModel messageModel){
        try {
            backSchoolService.sendSchoolMessage(userInfo, schoolId, messageModel);
        } catch (BackUserException e) {
            return ResponseModel.fail("",e.getMessage());
        }
        return ResponseModel.sucessWithEmptyData("");
    }

}
