package com.jichuangsi.school.testservice.controller;

import com.jichuangsi.microservice.common.model.ResponseModel;
import com.jichuangsi.microservice.common.model.UserInfoForToken;
import com.jichuangsi.school.testservice.exception.TeacherTestServiceException;
import com.jichuangsi.school.testservice.model.TestModelForTeacher;
import com.jichuangsi.school.testservice.model.SearchTestModel;
import com.jichuangsi.school.testservice.model.common.DeleteQueryModel;
import com.jichuangsi.school.testservice.model.common.Elements;
import com.jichuangsi.school.testservice.model.common.PageHolder;
import com.jichuangsi.school.testservice.service.ITestConsoleService;
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
@Api("TestConsoleController测试开发相关的api")
public class TestConsoleController {

    @Resource
    private ITestConsoleService testConsoleService;

    @ApiOperation(value = "根据status,startTime,sortNum,keyword,page获取老师练习列表信息", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String")
    })
    @PostMapping(value = "/getSortedList", consumes = "application/json")
    public ResponseModel<PageHolder<TestModelForTeacher>> getSortedList(@ModelAttribute @NotNull UserInfoForToken userInfo, @RequestBody(required = true) @NotNull SearchTestModel searchTestModel) throws TeacherTestServiceException {
        return ResponseModel.sucess("", testConsoleService.getSortedTestsList(userInfo, searchTestModel));
    }

    @ApiOperation(value = "教师新增练习", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String")
    })
    @PostMapping(value = "/saveTest", consumes = "application/json")
    public ResponseModel saveTest(@ModelAttribute @NotNull UserInfoForToken userInfo, @RequestBody @Validated @NotNull TestModelForTeacher testModelForTeacher) throws TeacherTestServiceException {
        testConsoleService.saveNewTest(userInfo, testModelForTeacher);
        return ResponseModel.sucessWithEmptyData("");
    }

    @ApiOperation(value = "新增练习时，添加班级的数据", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String")
    })
    @GetMapping(value = "/getElements")
    public ResponseModel<Elements> getElements(@ModelAttribute @NotNull UserInfoForToken userInfo) throws TeacherTestServiceException {
        return ResponseModel.sucess("", testConsoleService.getElementsList(userInfo));
    }

    @ApiOperation(value = "删除新建没有发布的练习", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String")
    })
    @DeleteMapping(value = "/deleteTest", consumes = "application/json")
    public ResponseModel deleteTest(@ModelAttribute @NotNull UserInfoForToken userInfo, @RequestBody @NotNull DeleteQueryModel deleteQueryModel) throws TeacherTestServiceException {
        testConsoleService.deleteTestIsNotStart(userInfo, deleteQueryModel);
        return ResponseModel.sucessWithEmptyData("");
    }

    @ApiOperation(value = "修改新建没有发布的练习", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String")
    })
    @PostMapping("/updateTestContent")
    public ResponseModel updateTestContent(@ModelAttribute @NotNull UserInfoForToken userInfo, @RequestBody @NotNull TestModelForTeacher testModelForTeacher) throws TeacherTestServiceException {
        testConsoleService.updateTestIsNotStart(userInfo, testModelForTeacher);
        return ResponseModel.sucessWithEmptyData("");
    }

    @ApiOperation(value = "修改新建没有发布的练习", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String")
    })
    @PostMapping("/updateTestStatus")
    public ResponseModel updateTestStatus(@ModelAttribute @NotNull UserInfoForToken userInfo, @RequestBody @NotNull TestModelForTeacher testModelForTeacher) throws TeacherTestServiceException {
        testConsoleService.updateTest2NewStatus(userInfo, testModelForTeacher);
        return ResponseModel.sucessWithEmptyData("");
    }
}
