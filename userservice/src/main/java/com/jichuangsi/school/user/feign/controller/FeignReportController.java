package com.jichuangsi.school.user.feign.controller;

import com.jichuangsi.microservice.common.model.ResponseModel;
import com.jichuangsi.school.user.entity.UserInfo;
import com.jichuangsi.school.user.feign.model.ClassStudentModel;
import com.jichuangsi.school.user.feign.service.FeignReportService;
import com.jichuangsi.school.user.model.System.User;
import com.jichuangsi.school.user.model.school.GradeModel;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
@RestController
@RequestMapping("/feign")
@Api("user的feign")
public class FeignReportController {
    @Resource
    private FeignReportService feignReportService;

    @ApiOperation(value = "根据年级和班级查询学生", notes = "")
    @PostMapping("/getStudentByClassAndGradeId")
    public ResponseModel<List<ClassStudentModel>> getStudentByGradeId(@RequestParam("gradeId")String gradeId){

        return ResponseModel.sucess("",feignReportService.getStudentByGradeId(gradeId));

    }
    @ApiOperation(value = "根据学校id查询年级详情", notes = "")
    @GetMapping("/getGradeBySchoolId")
    public ResponseModel<List<GradeModel>> getStudentBySchoolId(@RequestParam("schoolId") String schoolId){
        return ResponseModel.sucess("",feignReportService.getGradeBySchoolId(schoolId));
    }

   /* @ApiOperation(value = "根据年级Id和科目查询老师", notes = "")
    @PostMapping("/getTeacherByGradeIdAndSubjectName")
    public ResponseModel<List<UserInfo>> getTeacherByGradeIdAndSubjectName(@RequestParam("gradeId") String gradeId, @RequestParam("subjectName") String subjectName){
        return ResponseModel.sucess("",feignReportService.getTestByGradeIdAndSubjectName(gradeId,subjectName));
    }*/

}
