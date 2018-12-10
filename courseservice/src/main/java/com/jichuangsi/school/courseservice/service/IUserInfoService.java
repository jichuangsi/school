package com.jichuangsi.school.courseservice.service;

import com.jichuangsi.school.courseservice.model.transfer.TransferClass;
import com.jichuangsi.school.courseservice.model.transfer.TransferStudent;
import com.jichuangsi.school.courseservice.model.transfer.TransferTeacher;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "userservice")
public interface IUserInfoService {

    @RequestMapping("/getClassInfoForTeacher")
    public List<TransferClass> getClassForTeacherById(@RequestParam(value = "teacherId") String teacherId);

    @RequestMapping("/getUserInfoForTeacher")
    public TransferTeacher getUserForTeacherById(@RequestParam(value = "teacherId") String teacherId);

    @RequestMapping("/getStudentsForClass")
    public List<TransferStudent> getStudentsForClassById(@RequestParam(value = "classId") String classId);
}
