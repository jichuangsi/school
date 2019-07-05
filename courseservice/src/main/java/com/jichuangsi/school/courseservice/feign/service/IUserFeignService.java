package com.jichuangsi.school.courseservice.feign.service;


import com.jichuangsi.microservice.common.model.ResponseModel;
import com.jichuangsi.school.courseservice.feign.service.impl.UserFallBackFeignServiceImpl;
import com.jichuangsi.school.courseservice.model.feign.report.Student;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(value = "userservice",fallback = UserFallBackFeignServiceImpl.class)
public interface IUserFeignService {
    @RequestMapping(value = "/StudentInfo/findStudentClassId",method = RequestMethod.POST)
    public ResponseModel<String> findStudentClass(@RequestParam("studentId") String studentId);

    @GetMapping("/feign/getStudentsForClass")
    ResponseModel<List<Student>> getStudentsForClass(@RequestParam(value = "classId") String classId);


}
