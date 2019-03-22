package com.jichuangsi.school.user.feign.Controller;

import com.jichuangsi.microservice.common.model.ResponseModel;
import com.jichuangsi.school.user.exception.FeignControllerException;
import com.jichuangsi.school.user.feign.model.ClassDetailModel;
import com.jichuangsi.school.user.feign.service.IFeignService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/feign")
@Api("user的feign")
public class FeignController {

    @Resource
    private IFeignService feignService;

    @ApiOperation(value = "获取班级最新信息，包括班级人数，年级", notes = "")
    @GetMapping("/getClassDetail")
    public ResponseModel<ClassDetailModel>  getClassDetail(@RequestParam("classId") String classId){
        try {
            return ResponseModel.sucess("",feignService.findClassDetailByClassId(classId));
        } catch (FeignControllerException e) {
            return ResponseModel.fail("",e.getMessage());
        }
    }
}
