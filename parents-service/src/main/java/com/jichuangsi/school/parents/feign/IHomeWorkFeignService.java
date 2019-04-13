package com.jichuangsi.school.parents.feign;

import com.jichuangsi.microservice.common.model.ResponseModel;
import com.jichuangsi.school.parents.feign.impl.HomeWorkFallBackFeignServiceImpl;
import com.jichuangsi.school.parents.feign.model.HomeWorkParentModel;
import com.jichuangsi.school.parents.model.statistics.KnowledgeStatisticsModel;
import com.jichuangsi.school.parents.model.statistics.ParentStatisticsModel;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(value = "homeworkservice",fallbackFactory = HomeWorkFallBackFeignServiceImpl.class)
public interface IHomeWorkFeignService {

    @RequestMapping("/feign/getParentHomeWork")
    ResponseModel<List<HomeWorkParentModel>> getParentHomeWork(@RequestParam("classId") String classId, @RequestParam("studentId") String studentId);

    @RequestMapping("/feign/getParentStudentHomeworkStatistics")
    ResponseModel<List<KnowledgeStatisticsModel>> getParentStudentHomeworkStatistics(@RequestBody ParentStatisticsModel model);
}
