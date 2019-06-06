package com.jichuangsi.school.courseservice.feign.service.impl;

import com.jichuangsi.microservice.common.model.ResponseModel;
import com.jichuangsi.school.courseservice.feign.service.IStatisticsFeignService;
import com.jichuangsi.school.courseservice.model.transfer.TransferStudent;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StatisticsFallBackFeignServiceImpl implements IStatisticsFeignService {

    private final String ERR_MSG = "调用微服务失败";


    @Override
    public ResponseModel<List<TransferStudent>> getSignStudents(String courseId, String classId) {
        return ResponseModel.fail("",ERR_MSG);
    }
}
