package com.jichuangsi.school.examservice.controller;

import com.jichuangsi.school.courseservice.model.transfer.TransferExam;
import com.jichuangsi.school.examservice.service.IExamService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@RestController
@Api("ICourseController")
public class ICourseController {

    @Resource
    private IExamService examService;

    @ApiOperation(value = "courseservice获取eaxm的id,name", notes = "")
    @GetMapping("/getExamInfoForTeacher")
    public List<TransferExam> getExamForTeacherById(@RequestParam(value = "teacherId") String teacherId){
        return examService.getTransferExamByTeacherId(teacherId);
    }
}
