package com.jichuangsi.school.testservice.controller;

import com.jichuangsi.microservice.common.model.ResponseModel;
import com.jichuangsi.microservice.common.model.UserInfoForToken;
import com.jichuangsi.school.testservice.exception.StudentTestServiceException;
import com.jichuangsi.school.testservice.model.KnowledgeModel;
import com.jichuangsi.school.testservice.model.SearchTestModel;
import com.jichuangsi.school.testservice.model.TestModelForStudent;
import com.jichuangsi.school.testservice.model.common.PageHolder;
import com.jichuangsi.school.testservice.model.feign.SearchTestModelId;
import com.jichuangsi.school.testservice.model.statistics.KnowledgeStatisticsModel;
import com.jichuangsi.school.testservice.model.statistics.ParentStatisticsModel;
import com.jichuangsi.school.testservice.service.IStudentTestService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/Feign")
@Api("Feign相关的api")
public class FeignController {

    @Resource
    private IStudentTestService studentTestService;

    //获取学生历史习题列表
    @ApiOperation(value = "根据学生id获取历史习题列表信息", notes = "")
    @ApiImplicitParams({})
    @PostMapping("/getHistory")
    public ResponseModel<PageHolder<TestModelForStudent>> getHistory(@RequestBody SearchTestModelId searchTestModel) throws StudentTestServiceException {
        PageHolder<TestModelForStudent> historyTestsListFeign = studentTestService.getHistoryTestsListFeign(searchTestModel.getStudentId(), searchTestModel);

        return ResponseModel.sucess("",historyTestsListFeign);
    }
    //获取学生历史考试列表
    @ApiOperation(value = "家长端查询学生知识点统计", notes = "")
    @ApiImplicitParams({})
    @PostMapping("/getParentTestStatistics")
public ResponseModel<List<KnowledgeStatisticsModel>> getParentTestStatistics(@RequestBody ParentStatisticsModel model)throws StudentTestServiceException{
    try {
        return ResponseModel.sucess("",studentTestService.getParentStatistics(model));
    } catch (StudentTestServiceException e) {
        return ResponseModel.fail("",e.getMessage());
    }
}

}
