package com.jichuangsi.school.parents.feign.impl;

import com.jichuangsi.microservice.common.model.ResponseModel;
import com.jichuangsi.microservice.common.model.UserInfoForToken;
import com.jichuangsi.school.parents.feign.ICourseFeignService;
import com.jichuangsi.school.parents.feign.model.CourseForStudent;
import com.jichuangsi.school.parents.feign.model.CourseForStudentId;
import com.jichuangsi.school.parents.feign.model.CourseForStudentIdTime;
import com.jichuangsi.school.parents.model.common.PageHolder;
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

    @Override
    public ResponseModel<PageHolder<CourseForStudent>> getHistory(CourseForStudentId pageInform) {
        return ResponseModel.fail("",ERR_MSG);
    }


    @Override
    public ResponseModel<PageHolder<CourseForStudent>> getHistoryTime(CourseForStudentIdTime pageInform) {
        return ResponseModel.fail("",ERR_MSG);
    }

}
