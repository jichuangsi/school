package com.jichuangsi.school.courseservice.feign.service;

import com.jichuangsi.microservice.common.model.ResponseModel;
import com.jichuangsi.school.courseservice.feign.model.CourseSignModel;
import com.jichuangsi.school.courseservice.feign.service.impl.UserFallBackFeignServiceImpl;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@FeignClient(value = "userservice",fallback = UserFallBackFeignServiceImpl.class)
public interface IUserFeignService {

    @RequestMapping("/feign/sendParentStudentMsg")
    ResponseModel sendParentStudentMsg(@RequestBody CourseSignModel model);
}
