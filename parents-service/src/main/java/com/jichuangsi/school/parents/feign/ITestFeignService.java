package com.jichuangsi.school.parents.feign;

import com.jichuangsi.microservice.common.model.ResponseModel;
import com.jichuangsi.microservice.common.model.UserInfoForToken;
import com.jichuangsi.school.parents.feign.impl.TestFallBackFrignServiceImpl;
import com.jichuangsi.school.parents.feign.model.SearchTestModel;
import com.jichuangsi.school.parents.feign.model.SearchTestModelId;
import com.jichuangsi.school.parents.feign.model.TestModelForStudent;
import com.jichuangsi.school.parents.model.common.PageHolder;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "testService",fallbackFactory =TestFallBackFrignServiceImpl.class)
public interface ITestFeignService {
    @RequestMapping("/Feign/getHistory")
    ResponseModel<PageHolder<TestModelForStudent>> getHistory( @RequestBody SearchTestModelId searchTestModel);
}
