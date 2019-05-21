package com.jichuangsi.school.courseservice.feign.service;

import com.jichuangsi.microservice.common.model.ResponseModel;
import com.jichuangsi.school.courseservice.feign.model.CourseSignModel;
import com.jichuangsi.school.courseservice.feign.service.impl.ParentFallBackFeignServiceImpl;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;



@FeignClient(value = "parentservice",fallback = ParentFallBackFeignServiceImpl.class)
public interface IParentFeignService {

    @RequestMapping("/feign/sendParentStudentMsg")
    ResponseModel sendParentStudentMsg(@RequestBody CourseSignModel model);
}
