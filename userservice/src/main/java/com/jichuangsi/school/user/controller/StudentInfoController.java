package com.jichuangsi.school.user.controller;

import com.jichuangsi.microservice.common.model.ResponseModel;
import com.jichuangsi.microservice.common.model.UserInfoForToken;
import com.jichuangsi.school.user.exception.UserServiceException;
import com.jichuangsi.school.user.service.IStudentInfoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/StudentInfo")
@Api("StudentInfo相关的api")
@CrossOrigin
public class StudentInfoController {
    @Resource
    private IStudentInfoService studentInfoService;

    //获取所有用户
    @ApiOperation(value = "根据学生id查班级id", notes = "")
    @ApiImplicitParams({
            })
    @RequestMapping(value = "/findStudentClassId",method = RequestMethod.POST)
    public ResponseModel<String> findStudentClass(@RequestParam("studentId") String studentId) throws UserServiceException {
        return ResponseModel.sucess("", studentInfoService.findStudentClass(studentId));
    }
}
