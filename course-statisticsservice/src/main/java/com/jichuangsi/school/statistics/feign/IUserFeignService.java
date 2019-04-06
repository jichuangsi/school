package com.jichuangsi.school.statistics.feign;

import com.jichuangsi.microservice.common.model.ResponseModel;
import com.jichuangsi.school.statistics.feign.impl.UserFallBackFeignServiceImpl;
import com.jichuangsi.school.statistics.feign.model.ClassDetailModel;
import com.jichuangsi.school.statistics.feign.model.TransferStudent;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(value = "userservice",fallback = UserFallBackFeignServiceImpl.class)
public interface IUserFeignService {

    @RequestMapping("/feign/getClassDetail")
    ResponseModel<ClassDetailModel> getClassDetail(@RequestParam("classId") String classId);

    @RequestMapping("/feign/getTeachClassIds")
    ResponseModel<List<String>> getTeachClassIds(@RequestParam("teacherId") String teacherId);

    @RequestMapping("/feign/getClassDetail")
    ResponseModel<List<ClassDetailModel>> getClassDetailByIds(@RequestBody List<String> classIds);

    @GetMapping("/feign/getStudentsForClass")
    ResponseModel<List<TransferStudent>> getStudentsForClass(@RequestParam(value = "classId") String classId);
}
