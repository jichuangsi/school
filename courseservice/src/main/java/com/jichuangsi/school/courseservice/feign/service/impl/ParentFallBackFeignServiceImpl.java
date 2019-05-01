package com.jichuangsi.school.courseservice.feign.service.impl;

import com.jichuangsi.microservice.common.model.ResponseModel;
import com.jichuangsi.school.courseservice.feign.model.CourseSignModel;
import com.jichuangsi.school.courseservice.feign.service.IParentFeignService;
import org.springframework.stereotype.Service;

@Service
public class ParentFallBackFeignServiceImpl implements IParentFeignService {

    private final String ERR_MSG = "调用微服务失败";

    @Override
    public ResponseModel sendParentStudentMsg(CourseSignModel model) {
        return ResponseModel.fail("",ERR_MSG);
    }
}
