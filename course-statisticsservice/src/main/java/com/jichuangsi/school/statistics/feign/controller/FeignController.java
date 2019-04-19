package com.jichuangsi.school.statistics.feign.controller;

import com.jichuangsi.microservice.common.model.ResponseModel;
import com.jichuangsi.school.statistics.exception.FeignClientException;
import com.jichuangsi.school.statistics.feign.model.TransferStudent;
import com.jichuangsi.school.statistics.service.IFeignService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/feign")
@Api("statistics的feignController")
public class FeignController {

    @Resource
    private IFeignService feignService;


    @ApiOperation(value = "根据courseId和classId获取学生签到", notes = "")
    @ApiImplicitParams({ })
    @GetMapping("/getSignStudents")
    public ResponseModel<List<TransferStudent>> getSignStudents(@RequestParam("courseId") String courseId,@RequestParam("classId") String classId){
        try {
            return ResponseModel.sucess("",feignService.getSignStudents(courseId, classId));
        } catch (FeignClientException e) {
            return ResponseModel.fail("",e.getMessage());
        }
    }

}
