package com.jichuangsi.school.parents.feign.impl;

import com.jichuangsi.microservice.common.model.ResponseModel;
import com.jichuangsi.school.parents.feign.ICourseStatisticsFeignService;
import com.jichuangsi.school.parents.feign.model.TransferStudent;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

public class CourseStatisticsFallBackFeignServiceImpl  implements ICourseStatisticsFeignService{
    private final String ERR_MSG ="调用微服务失败";

    public ResponseModel<List<TransferStudent>> getCourseSignFeign(@RequestParam("courseId") String courseId, @RequestParam("classId") String classId){
        return ResponseModel.fail("",ERR_MSG);
    }
}
