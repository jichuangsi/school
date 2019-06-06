package com.jichuangsi.school.parents.feign.impl;

import com.jichuangsi.microservice.common.model.ResponseModel;
import com.jichuangsi.school.parents.feign.ITestFeignService;
import com.jichuangsi.school.parents.feign.model.SearchTestModelId;
import com.jichuangsi.school.parents.feign.model.TestModelForStudent;
import com.jichuangsi.school.parents.model.common.PageHolder;
import com.jichuangsi.school.parents.model.statistics.KnowledgeStatisticsModel;
import com.jichuangsi.school.parents.model.statistics.ParentStatisticsModel;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@Service
public class TestFallBackFrignServiceImpl implements ITestFeignService {
    private final String ERR_MSG ="调用微服务失败";
    @Override
    public ResponseModel<PageHolder<TestModelForStudent>> getHistory(SearchTestModelId searchTestModel) {
        return ResponseModel.fail("",ERR_MSG);
    }
    @Override
    public ResponseModel<List<KnowledgeStatisticsModel>> getParentTestStatistics(@RequestBody ParentStatisticsModel model){
        return ResponseModel.fail("",ERR_MSG);
    }

}
