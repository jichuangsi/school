package com.jichuangsi.school.testservice.Feign;


import com.jichuangsi.microservice.common.model.ResponseModel;
import com.jichuangsi.school.testservice.Feign.impl.UserFallBackFeignServiceImpl;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "userservice",fallback = UserFallBackFeignServiceImpl.class)
public interface IUserFeignService {
    @RequestMapping("/StudentInfo/findStudentClassId")
    public ResponseModel<String> findStudentClass(@RequestParam("studentId") String studentId);

}
