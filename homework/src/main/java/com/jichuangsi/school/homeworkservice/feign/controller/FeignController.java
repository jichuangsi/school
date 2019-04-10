package com.jichuangsi.school.homeworkservice.feign.controller;

import com.jichuangsi.microservice.common.model.ResponseModel;
import com.jichuangsi.school.homeworkservice.exception.FeignControllerException;
import com.jichuangsi.school.homeworkservice.feign.model.HomeWorkParentModel;
import com.jichuangsi.school.homeworkservice.feign.model.HomeWorkRateModel;
import com.jichuangsi.school.homeworkservice.feign.model.QuestionRateModel;
import com.jichuangsi.school.homeworkservice.feign.model.TeacherHomeResultModel;
import com.jichuangsi.school.homeworkservice.feign.service.IFeignService;
import com.jichuangsi.school.homeworkservice.model.HomeworkModelForTeacher;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/feign")
@Api("homeWork的feignController")
public class FeignController {
    @Resource
    private IFeignService feignService;

    @ApiOperation(value = "根据questionIds和studentId获取学生的正确率", notes = "")
    @ApiImplicitParams({ })
    @PostMapping("/getStudentQuestionRate")
    public ResponseModel<Double> getStudentQuestionRate(@RequestBody QuestionRateModel model){
        try {
            return ResponseModel.sucess("",feignService.getStudentQuestionRate(model));
        } catch (FeignControllerException e) {
            return ResponseModel.fail("",e.getMessage());
        }
    }

    @ApiOperation(value = "根据学生id获取一周历史习题", notes = "")
    @ApiImplicitParams({ })
    @GetMapping("/getStudentQuestionOnWeek")
    public ResponseModel<List<HomeWorkRateModel>> getStudentQuestionOnWeek(@RequestParam("studentId") String studentId, @RequestParam("subject") String subject){
        try {
            return ResponseModel.sucess("",feignService.getStudentQuestionOnWeek(studentId,subject));
        } catch (FeignControllerException e) {
            return ResponseModel.fail("",e.getMessage());
        }
    }

    @ApiOperation(value = "根据questionIds该题的正确率", notes = "")
    @ApiImplicitParams({ })
    @PostMapping("/getStudentQuestionClassRate")
    public ResponseModel<Double> getStudentQuestionClassRate(@RequestBody List<String> questionIds){
        try {
            return ResponseModel.sucess("",feignService.getStudentClassQuestionRate(questionIds));
        } catch (FeignControllerException e) {
            return ResponseModel.fail("",e.getMessage());
        }
    }

    @ApiOperation(value = "根据老师Id和classId查询近一周的习题列表", notes = "")
    @ApiImplicitParams({ })
    @GetMapping("/getHomeWorkByTeacherIdAndclassId")
    public ResponseModel<List<HomeworkModelForTeacher>> getHomeWorkByTeacherIdAndclassId(@RequestParam("teacherId") String teacherId,
                                                                                   @RequestParam("classId") String classId){
        try {
            return ResponseModel.sucess("",feignService.getHomeWorkByTeacherIdAndclassId(teacherId, classId));
        } catch (FeignControllerException e) {
            return ResponseModel.fail("",e.getMessage());
        }
    }

    @ApiOperation(value = "根据老师Id,homeId获取习题的统计", notes = "")
    @ApiImplicitParams({ })
    @GetMapping("/getHomeWorkRate")
    public ResponseModel<TeacherHomeResultModel> getHomeWorkRate(@RequestParam("teacherId") String teacherId,
                                                                 @RequestParam("homeId") String homeId){
        try {
            return ResponseModel.sucess("",feignService.getHomeWorkRate(teacherId, homeId));
        } catch (FeignControllerException e) {
            return ResponseModel.fail("",e.getMessage());
        }
    }

    @ApiOperation(value = "根据班级和学生的id获取作业的完成情况", notes = "")
    @ApiImplicitParams({ })
    @GetMapping("/getParentHomeWork")
    public ResponseModel<List<HomeWorkParentModel>> getParentHomeWork(@RequestParam String classId,@RequestParam String studentId){

        return ResponseModel.sucessWithEmptyData("");
    }
}
