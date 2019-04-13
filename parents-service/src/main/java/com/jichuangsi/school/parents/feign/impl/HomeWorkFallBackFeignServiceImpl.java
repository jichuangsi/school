package com.jichuangsi.school.parents.feign.impl;

import com.jichuangsi.microservice.common.model.ResponseModel;
import com.jichuangsi.school.parents.feign.IHomeWorkFeignService;
import com.jichuangsi.school.parents.feign.model.HomeWorkParentModel;
import com.jichuangsi.school.parents.model.statistics.KnowledgeStatisticsModel;
import com.jichuangsi.school.parents.model.statistics.ParentStatisticsModel;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HomeWorkFallBackFeignServiceImpl implements IHomeWorkFeignService {

    private final String ERR_MSG ="调用微服务失败";

    @Override
    public ResponseModel<List<HomeWorkParentModel>> getParentHomeWork(String classId, String studentId) {
        return ResponseModel.fail("",ERR_MSG);
    }

    @Override
    public ResponseModel<List<KnowledgeStatisticsModel>> getParentStudentHomeworkStatistics(ParentStatisticsModel model) {
        return ResponseModel.fail("",ERR_MSG);
    }
}
