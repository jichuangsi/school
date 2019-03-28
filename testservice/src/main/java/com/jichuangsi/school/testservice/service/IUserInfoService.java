package com.jichuangsi.school.testservice.service;

import com.jichuangsi.school.testservice.model.transfer.TransferClass;
import com.jichuangsi.school.testservice.model.transfer.TransferStudent;
import com.jichuangsi.school.testservice.model.transfer.TransferTeacher;
import com.jichuangsi.school.testservice.service.impl.UserInfoServiceFallBack;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "userservice", fallback = UserInfoServiceFallBack.class)
public interface IUserInfoService {

    @RequestMapping("/getClassInfoForTeacher")
    public List<TransferClass> getClassForTeacherById(@RequestParam(value = "teacherId") String teacherId);

    @RequestMapping("/getUserInfoForTeacher")
    public TransferTeacher getUserForTeacherById(@RequestParam(value = "teacherId") String teacherId);

    @RequestMapping("/getStudentsForClass")
    public List<TransferStudent> getStudentsForClassById(@RequestParam(value = "classId") String classId);
}
