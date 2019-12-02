package com.jichuangsi.school.homeworkservice.controller;

import com.jichuangsi.microservice.common.model.ResponseModel;
import com.jichuangsi.microservice.common.model.UserInfoForToken;
import com.jichuangsi.school.homeworkservice.exception.TeacherHomeworkServiceException;
import com.jichuangsi.school.homeworkservice.model.HomeworkModelForTeacher;
import com.jichuangsi.school.homeworkservice.model.SearchHomeworkModel;
import com.jichuangsi.school.homeworkservice.model.common.DeleteQueryModel;
import com.jichuangsi.school.homeworkservice.model.common.Elements;
import com.jichuangsi.school.homeworkservice.model.common.PageHolder;
import com.jichuangsi.school.homeworkservice.model.transfer.TransferStudent;
import com.jichuangsi.school.homeworkservice.service.IHomeworkConsoleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.constraints.NotNull;
import java.util.List;

@RequestMapping("/console")
@RestController
@Api("HomeworkConsoleController测试开发相关的api")
@CrossOrigin
public class HomeworkConsoleController {

    @Resource
    private IHomeworkConsoleService homeworkConsoleService;

    @ApiOperation(value = "根据status,startTime,sortNum,keyword,page获取老师练习列表信息", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String")
    })
    @PostMapping(value = "/getSortedList", consumes = "application/json")
    public ResponseModel<PageHolder<HomeworkModelForTeacher>> getSortedList(@ModelAttribute @NotNull UserInfoForToken userInfo, @RequestBody(required = true) @NotNull SearchHomeworkModel searchHomeworkModel) throws TeacherHomeworkServiceException {
        return ResponseModel.sucess("", homeworkConsoleService.getSortedHomeworksList(userInfo, searchHomeworkModel));
    }

    @ApiOperation(value = "教师新增练习", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String")
    })
    @PostMapping(value = "/saveHomeWork", consumes = "application/json")
    public ResponseModel saveHomeWork(@ModelAttribute @NotNull UserInfoForToken userInfo, @RequestBody @Validated @NotNull HomeworkModelForTeacher homeworkModelForTeacher) throws TeacherHomeworkServiceException {
        homeworkConsoleService.saveNewHomework(userInfo, homeworkModelForTeacher);
        return ResponseModel.sucessWithEmptyData("");
    }

    @ApiOperation(value = "新增练习时，添加班级的数据", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String")
    })
    @GetMapping(value = "/getElements")
    public ResponseModel<Elements> getElements(@ModelAttribute @NotNull UserInfoForToken userInfo) throws TeacherHomeworkServiceException {
        return ResponseModel.sucess("", homeworkConsoleService.getElementsList(userInfo));
    }

    @ApiOperation(value = "删除新建没有发布的练习", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String")
    })
    @DeleteMapping(value = "/deleteHomeWork", consumes = "application/json")
    public ResponseModel deleteHomeWork(@ModelAttribute @NotNull UserInfoForToken userInfo, @RequestBody @NotNull DeleteQueryModel deleteQueryModel) throws TeacherHomeworkServiceException {
        homeworkConsoleService.deleteHomeWorkIsNotStart(userInfo, deleteQueryModel);
        return ResponseModel.sucessWithEmptyData("");
    }

    @ApiOperation(value = "修改新建没有发布的练习", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String")
    })
    @PostMapping("/updateHomeWorkContent")
    public ResponseModel updateHomeWorkContent(@ModelAttribute @NotNull UserInfoForToken userInfo, @RequestBody @NotNull HomeworkModelForTeacher homeworkModelForTeacher) throws TeacherHomeworkServiceException {
        homeworkConsoleService.updateHomeWorkIsNotStart(userInfo, homeworkModelForTeacher);
        return ResponseModel.sucessWithEmptyData("");
    }

    @ApiOperation(value = "修改新建没有发布的练习", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String")
    })
    @PostMapping("/updateHomeWorkStatus")
    public ResponseModel updateHomeWorkStatus(@ModelAttribute @NotNull UserInfoForToken userInfo, @RequestBody @NotNull HomeworkModelForTeacher homeworkModelForTeacher) throws TeacherHomeworkServiceException {
        homeworkConsoleService.updateHomeWork2NewStatus(userInfo, homeworkModelForTeacher);
        return ResponseModel.sucessWithEmptyData("");
    }


    @ApiOperation(value = "根据班级id获取学生", notes = "")
    @ApiImplicitParams({
        @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String"),
    })
    @PostMapping("/getStudentByClassId")
    public ResponseModel<List<TransferStudent>> getStudentByClassId(@ModelAttribute UserInfoForToken userInfo, @RequestParam String classId) throws TeacherHomeworkServiceException{

        return ResponseModel.sucess("",  homeworkConsoleService.getStudentByClassId(userInfo, classId));
    }

    @ApiOperation(value = "根据选择学生修改新建没有发布的练习", notes = "")
    @ApiImplicitParams({
        @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String")
    })
    @PostMapping("/updateHomeWorkStatusAndStudent")
    public ResponseModel updateHomeWorkStatusAndStudent(@ModelAttribute @NotNull UserInfoForToken userInfo, @RequestBody @NotNull HomeworkModelForTeacher homeworkModelForTeacher) throws TeacherHomeworkServiceException {
        homeworkConsoleService.updateHomeWorkStatusAndStudent(userInfo, homeworkModelForTeacher);
        return ResponseModel.sucessWithEmptyData("");
    }
}
