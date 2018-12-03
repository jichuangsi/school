package com.jichuangsi.school.courseservice.service;

import com.jichuangsi.school.courseservice.model.transfer.TransferExam;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "examservice")
public interface IExamInfoService {

    @RequestMapping("/getExamInfoForTeacher")
    public List<TransferExam> getExamForTeacherById(@RequestParam(value = "teacherId") String teacherId);
}
