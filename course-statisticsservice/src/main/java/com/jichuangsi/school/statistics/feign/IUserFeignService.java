package com.jichuangsi.school.statistics.feign;

import com.jichuangsi.microservice.common.model.ResponseModel;
import com.jichuangsi.school.statistics.feign.impl.UserFallBackFeignServiceImpl;
import com.jichuangsi.school.statistics.feign.model.ClassDetailModel;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "userservice",fallback = UserFallBackFeignServiceImpl.class)
public interface IUserFeignService {

    @RequestMapping("/feign/getClassDetail")
    ResponseModel<ClassDetailModel> getClassDetail(@RequestParam("classId") String classId);
}
