package com.jichuangsi.school.parents.feign.impl;

import com.jichuangsi.microservice.common.model.ResponseModel;
import com.jichuangsi.microservice.common.model.UserInfoForToken;
import com.jichuangsi.school.parents.feign.ITestFeignService;
import com.jichuangsi.school.parents.feign.model.SearchTestModel;
import com.jichuangsi.school.parents.feign.model.SearchTestModelId;
import com.jichuangsi.school.parents.feign.model.TestModelForStudent;
import com.jichuangsi.school.parents.model.common.PageHolder;
import org.springframework.stereotype.Service;

@Service
public class TestFallBackFrignServiceImpl implements ITestFeignService {
    private final String ERR_MSG ="调用微服务失败";
    @Override
    public ResponseModel<PageHolder<TestModelForStudent>> getHistory(SearchTestModelId searchTestModel) {
        return ResponseModel.fail("",ERR_MSG);
    }
}
