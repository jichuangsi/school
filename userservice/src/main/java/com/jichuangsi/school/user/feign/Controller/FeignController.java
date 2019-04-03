package com.jichuangsi.school.user.feign.Controller;

import com.jichuangsi.microservice.common.model.ResponseModel;
import com.jichuangsi.school.user.exception.FeignControllerException;
import com.jichuangsi.school.user.feign.model.ClassDetailModel;
import com.jichuangsi.school.user.feign.service.IFeignService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

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

    @ApiOperation(value = "根据老师id，获取所有执教班级", notes = "")
    @GetMapping("/getTeachClassIds")
    public ResponseModel<List<String>> getTeachClassIds(@RequestParam("teacherId") String teacherId){
        try {
            return ResponseModel.sucess("",feignService.getClassIdsByTeacherId(teacherId));
        } catch (FeignControllerException e) {
            return ResponseModel.fail("",e.getMessage());
        }
    }

    @ApiOperation(value = "获取班级s最新信息，包括班级人数，年级", notes = "")
    @PostMapping("/getClassDetail")
    public ResponseModel<List<ClassDetailModel>> getClassDetailByIds(@RequestBody List<String> classIds){
        try {
            return ResponseModel.sucess("",feignService.findClassDetailByClassIds(classIds));
        } catch (FeignControllerException e) {
            return ResponseModel.fail("",e.getMessage());
        }
    }
}
