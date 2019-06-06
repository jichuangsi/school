package com.jichuangsi.school.testservice.Feign.impl;

import com.jichuangsi.microservice.common.model.ResponseModel;
import com.jichuangsi.school.testservice.Feign.IUserFeignService;

public class UserFallBackFeignServiceImpl implements IUserFeignService {
    private final String ERR_MSG = "调用微服务失败";

    @Override
    public ResponseModel<String> findStudentClass(String studentId) {
        return ResponseModel.fail("",ERR_MSG);
    }
}
