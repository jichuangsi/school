package com.jichuangsi.school.parents.feign.impl;

import com.jichuangsi.microservice.common.model.ResponseModel;
import com.jichuangsi.school.parents.feign.ICourseFeignService;
import com.jichuangsi.school.parents.model.statistics.KnowledgeStatisticsModel;
import com.jichuangsi.school.parents.model.statistics.ParentStatisticsModel;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CourseFallBackFeignServiceImpl implements ICourseFeignService {

    private final String ERR_MSG ="调用微服务失败";

    @Override
    public ResponseModel<List<KnowledgeStatisticsModel>> getParentStatistics(ParentStatisticsModel model) {
        return ResponseModel.fail("",ERR_MSG);
    }
}
