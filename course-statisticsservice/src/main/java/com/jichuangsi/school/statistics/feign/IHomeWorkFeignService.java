package com.jichuangsi.school.statistics.feign;

import com.jichuangsi.microservice.common.model.ResponseModel;
import com.jichuangsi.school.statistics.feign.impl.HomeWorkFallBackFeignServiceImpl;
import com.jichuangsi.school.statistics.feign.model.HomeWorkRateModel;
import com.jichuangsi.school.statistics.feign.model.QuestionRateModel;
import com.jichuangsi.school.statistics.model.homework.HomeworkModelForTeacher;
import com.jichuangsi.school.statistics.model.result.TeacherHomeResultModel;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(value = "homeworkservice",fallback = HomeWorkFallBackFeignServiceImpl.class)
public interface IHomeWorkFeignService {


    @RequestMapping("/feign/getStudentQuestionOnWeek")
    ResponseModel<List<HomeWorkRateModel>> getStudentQuestionOnWeek(@RequestParam("studentId") String studentId, @RequestParam("subject") String subject);

    @RequestMapping("/feign/getStudentQuestionRate")
    ResponseModel<Double> getStudentQuestionRate(@RequestBody QuestionRateModel model);

    @RequestMapping("/feign/getStudentQuestionClassRate")
    ResponseModel<Double> getStudentQuestionClassRate(@RequestBody List<String> questionIds);

    @RequestMapping("/feign/getHomeWorkByTeacherIdAndclassId")
    ResponseModel<List<HomeworkModelForTeacher>> getHomeWorkByTeacherIdAndclassId(@RequestParam("teacherId") String teacherId,
                                                                                         @RequestParam("classId") String classId);

    @RequestMapping("/feign/getHomeWorkRate")
    ResponseModel<TeacherHomeResultModel> getHomeWorkRate(@RequestParam("teacherId") String teacherId,
                                                                 @RequestParam("homeId") String homeId);
}
