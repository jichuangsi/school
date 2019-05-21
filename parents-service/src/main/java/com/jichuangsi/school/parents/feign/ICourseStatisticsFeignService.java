package com.jichuangsi.school.parents.feign;

import com.jichuangsi.microservice.common.model.ResponseModel;
import com.jichuangsi.school.parents.feign.impl.CourseFallBackFeignServiceImpl;
import com.jichuangsi.school.parents.feign.impl.CourseStatisticsFallBackFeignServiceImpl;
import com.jichuangsi.school.parents.feign.model.TransferStudent;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(value = "COURSESTATISTICS",fallbackFactory = CourseStatisticsFallBackFeignServiceImpl.class)
public interface ICourseStatisticsFeignService {
    @RequestMapping(value = "/class/getCourseSignFeign",method = RequestMethod.POST)
    public ResponseModel<List<TransferStudent>> getCourseSignFeign(@RequestParam("courseId") String courseId, @RequestParam("classId") String classId) ;
}
