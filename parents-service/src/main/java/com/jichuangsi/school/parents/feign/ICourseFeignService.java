package com.jichuangsi.school.parents.feign;

import com.jichuangsi.microservice.common.model.ResponseModel;
import com.jichuangsi.school.parents.feign.impl.CourseFallBackFeignServiceImpl;
import com.jichuangsi.school.parents.model.statistics.KnowledgeStatisticsModel;
import com.jichuangsi.school.parents.model.statistics.ParentStatisticsModel;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@FeignClient(value = "courseservice",fallbackFactory = CourseFallBackFeignServiceImpl.class)
public interface ICourseFeignService {

    @RequestMapping("/feign/getParentStatistics")
    ResponseModel<List<KnowledgeStatisticsModel>> getParentStatistics(@RequestBody ParentStatisticsModel model);
}
