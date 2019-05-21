package com.jichuangsi.school.courseservice.feign.service;


import com.jichuangsi.microservice.common.model.ResponseModel;
import com.jichuangsi.school.courseservice.feign.service.impl.UserFallBackFeignServiceImpl;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "userservice",fallback = UserFallBackFeignServiceImpl.class)
public interface IUserFeignService {
    @RequestMapping(value = "/StudentInfo/findStudentClassId",method = RequestMethod.POST)
    public ResponseModel<String> findStudentClass(@RequestParam("studentId") String studentId);

}
