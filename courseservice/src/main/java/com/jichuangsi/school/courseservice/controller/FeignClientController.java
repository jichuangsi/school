package com.jichuangsi.school.courseservice.controller;

import com.jichuangsi.microservice.common.model.ResponseModel;
import com.jichuangsi.microservice.common.model.UserInfoForToken;
import com.jichuangsi.school.courseservice.Exception.FeignControllerException;
import com.jichuangsi.school.courseservice.Exception.StudentCourseServiceException;
import com.jichuangsi.school.courseservice.feign.service.IUserFeignService;
import com.jichuangsi.school.courseservice.model.CourseForStudent;
import com.jichuangsi.school.courseservice.model.PageHolder;
import com.jichuangsi.school.courseservice.model.feign.CourseForStudentId;
import com.jichuangsi.school.courseservice.model.feign.CourseForStudentIdTime;
import com.jichuangsi.school.courseservice.model.feign.QuestionRateModel;
import com.jichuangsi.school.courseservice.model.feign.classType.ClassDetailModel;
import com.jichuangsi.school.courseservice.model.feign.classType.ClassStatisticsModel;
import com.jichuangsi.school.courseservice.model.feign.classType.SearchStudentKnowledgeModel;
import com.jichuangsi.school.courseservice.model.feign.classType.StudentKnowledgeModel;
import com.jichuangsi.school.courseservice.model.feign.statistics.KnowledgeStatisticsModel;
import com.jichuangsi.school.courseservice.model.feign.statistics.ParentStatisticsModel;
import com.jichuangsi.school.courseservice.model.result.ResultKnowledgeModel;
import com.jichuangsi.school.courseservice.model.transfer.TransferKnowledge;
import com.jichuangsi.school.courseservice.service.IFeignClientService;
import com.jichuangsi.school.courseservice.service.IStudentCourseService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/feign")
@Api("FeignClientController")
public class FeignClientController {

    @Resource
    private IFeignClientService iFeignClientService;

    @Resource
    private IUserFeignService userFeignService;

    @Resource
    private IStudentCourseService studentCourseService;

    //获取学生历史课程列表
    @ApiOperation(value = "根据学生id获取历史学生课堂列表信息", notes = "")
    @ApiImplicitParams({
            })
    @PostMapping("/getHistory")
    public ResponseModel<PageHolder<CourseForStudent>> getHistory(@RequestBody CourseForStudentId pageInform) throws StudentCourseServiceException {
        ResponseModel<String> studentClass = userFeignService.findStudentClass(pageInform.getStudentId().toString());
        PageHolder<CourseForStudent> historyCoursesListFeign = studentCourseService.getHistoryCoursesListFeign(studentClass.getData().toString(), pageInform);
        return ResponseModel.sucess("",historyCoursesListFeign);
    }
   //获取学生历史课程列表
   @ApiOperation(value = "根据学生id获取历史学生课堂列表信息", notes = "")
   @ApiImplicitParams({
   })
   @PostMapping("/getHistoryTime")
   public ResponseModel<PageHolder<CourseForStudent>> getHistoryTime(@RequestBody CourseForStudentIdTime pageInform) throws StudentCourseServiceException {
       ResponseModel<String> studentClass = userFeignService.findStudentClass(pageInform.getStudentId().toString());
      // PageHolder<CourseForStudent> historyCoursesListFeign = studentCourseService.getHistoryCoursesListFeign(studentClass.getData().toString(),endTime,pageInform);
       PageHolder<CourseForStudent> historyCoursesListFeign = studentCourseService.getHistoryCoursesListFeignTime(studentClass.getData().toString(),pageInform.getStatisticsTimes(), pageInform);
       return ResponseModel.sucess("",historyCoursesListFeign);
   }
    //获取指定课堂题目知识点
    @ApiOperation(value = "根据问题id查询问题知识点", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "path", name = "questionId", value = "问题ID", required = true, dataType = "String")})
    @GetMapping("/getQuestionKnowledge/{questionId}")
    public ResponseModel<TransferKnowledge> getQuestionKnowledge(@PathVariable String questionId) throws StudentCourseServiceException {

        return ResponseModel.sucess("", iFeignClientService.getKnowledgeOfParticularQuestion(questionId));
    }

    @ApiOperation(value = "根据班级id获取近一周的题目及知识点", notes = "")
    @ApiImplicitParams({})
    @GetMapping("/getCourseQuestionOnWeek")
    public ResponseModel<List<ResultKnowledgeModel>> getCourseQuestionOnWeek(@RequestParam("classId") String classId, @RequestParam("subject") String subject) {
        try {
            return ResponseModel.sucess("", iFeignClientService.getStudentQuestionOnWeek(classId, subject));
        } catch (StudentCourseServiceException e) {
            return ResponseModel.fail("", e.getMessage());
        }
    }

    @ApiOperation(value = "根据questionIds和studentId获取学生的正确率", notes = "")
    @ApiImplicitParams({ })
    @PostMapping("/getStudentQuestionRate")
    public ResponseModel<Double> getStudentQuestionRate(@RequestBody QuestionRateModel model){
        try {
            return ResponseModel.sucess("",iFeignClientService.getStudentQuestionRate(model));
        } catch (FeignControllerException e) {
            return ResponseModel.fail("",e.getMessage());
        }
    }

    @ApiOperation(value = "根据问题id集合查询问题知识点", notes = "")
    @ApiImplicitParams({})
    @PostMapping("/getQuestionKnowledges")
    public ResponseModel<List<ResultKnowledgeModel>> getQuestionKnowledges(@RequestBody List<String> questionIds){
        try {
            return ResponseModel.sucess("",iFeignClientService.getQuestionKnowledges(questionIds));
        } catch (StudentCourseServiceException e) {
            return ResponseModel.fail("",e.getMessage());
        }
    }

    @ApiOperation(value = "根据问题id集合通过MD5查询问题Id", notes = "")
    @ApiImplicitParams({})
    @PostMapping("/getQuetsionIdsCrossByMD5")
    public ResponseModel<Double> getQuetsionIdsCrossByMD5(@RequestBody QuestionRateModel model){
        try {
            return ResponseModel.sucess("",iFeignClientService.getQuetsionIdsCrossByMD5(model));
        } catch (FeignControllerException e) {
            return ResponseModel.fail("",e.getMessage());
        }
    }

    @ApiOperation(value = "根据班级ids查询班级近一个月的学习情况", notes = "")
    @ApiImplicitParams({})
    @PostMapping("/getClassStatisticsByClassIdsOnMonth")
    public ResponseModel<List<ClassStatisticsModel>> getClassStatisticsByClassIdsOnMonth(@RequestBody List<ClassDetailModel> classModels){
        try {
            return ResponseModel.sucess("",iFeignClientService.getClassStatisticsByClassIdsOnMonth(classModels));
        } catch (FeignControllerException e) {
            return ResponseModel.fail("",e.getMessage());
        }
    }

    @ApiOperation(value = "根据学生和问题ids查询各学生的掌握知识点", notes = "")
    @ApiImplicitParams({})
    @PostMapping("/getStudentKnowledges")
    public ResponseModel<List<StudentKnowledgeModel>> getStudentKnowledges(@RequestBody SearchStudentKnowledgeModel model){
        try {
            return ResponseModel.sucess("",iFeignClientService.getStudentKnowledges(model));
        } catch (FeignControllerException e) {
            return ResponseModel.fail("",e.getMessage());
        }
    }

    @ApiOperation(value = "家长端查询学生知识点统计", notes = "")
    @ApiImplicitParams({})
    @PostMapping("/getParentStatistics")
    public ResponseModel<List<KnowledgeStatisticsModel>> getParentStatistics(@RequestBody ParentStatisticsModel model){
        try {
            return ResponseModel.sucess("",iFeignClientService.getParentStatistics(model));
        } catch (FeignControllerException e) {
            return ResponseModel.fail("",e.getMessage());
        }
    }
}
