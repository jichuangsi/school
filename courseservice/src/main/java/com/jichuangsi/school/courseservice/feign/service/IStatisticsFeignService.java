package com.jichuangsi.school.courseservice.feign.service;

import com.jichuangsi.microservice.common.model.ResponseModel;
import com.jichuangsi.school.courseservice.feign.service.impl.StatisticsFallBackFeignServiceImpl;
import com.jichuangsi.school.courseservice.model.transfer.TransferStudent;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(value = "coursestatistics",fallback = StatisticsFallBackFeignServiceImpl.class )
public interface IStatisticsFeignService {

    @RequestMapping("/feign/getSignStudents")
    ResponseModel<List<TransferStudent>> getSignStudents(@RequestParam("courseId") String courseId, @RequestParam("classId") String classId);
}
