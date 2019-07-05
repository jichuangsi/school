package com.jichuangsi.school.courseservice.feign.service.impl;

import com.jichuangsi.microservice.common.model.ResponseModel;
import com.jichuangsi.school.courseservice.feign.service.IUserFeignService;
import com.jichuangsi.school.courseservice.model.feign.report.Student;
import com.jichuangsi.school.courseservice.model.transfer.TransferStudent;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
@Service
public class UserFallBackFeignServiceImpl implements IUserFeignService{
    private final String ERR_MSG = "调用微服务失败";

    @Override
    public ResponseModel<String> findStudentClass(String studentId) {
        return ResponseModel.fail("",ERR_MSG);
    }

    @Override
    public ResponseModel<List<Student>> getStudentsForClass(String classId) {
        return ResponseModel.fail("",ERR_MSG);
    }
}
