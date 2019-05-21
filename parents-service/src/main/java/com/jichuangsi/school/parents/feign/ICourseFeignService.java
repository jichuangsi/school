package com.jichuangsi.school.parents.feign;

import com.jichuangsi.microservice.common.model.ResponseModel;
import com.jichuangsi.microservice.common.model.UserInfoForToken;
import com.jichuangsi.school.parents.feign.impl.CourseFallBackFeignServiceImpl;
import com.jichuangsi.school.parents.feign.model.CourseForStudent;
import com.jichuangsi.school.parents.feign.model.CourseForStudentId;
import com.jichuangsi.school.parents.model.common.PageHolder;
import com.jichuangsi.school.parents.model.statistics.KnowledgeStatisticsModel;
import com.jichuangsi.school.parents.model.statistics.ParentStatisticsModel;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(value = "courseservice",fallbackFactory = CourseFallBackFeignServiceImpl.class)
public interface ICourseFeignService {

    @RequestMapping("/feign/getParentStatistics")
    ResponseModel<List<KnowledgeStatisticsModel>> getParentStatistics(@RequestBody ParentStatisticsModel model);

    @RequestMapping("/feign/getHistory")
    ResponseModel<PageHolder<CourseForStudent>> getHistory(@RequestBody CourseForStudentId pageInform);
}
