package com.jichuangsi.school.homeworkservice.service;

import com.jichuangsi.school.homeworkservice.model.transfer.TransferExam;
import com.jichuangsi.school.homeworkservice.service.impl.ExamInfoServiceFallBack;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "examservice", fallback = ExamInfoServiceFallBack.class)
public interface IExamInfoService {

    @RequestMapping("/getExamInfoForTeacher")
    public List<TransferExam> getExamForTeacherById(@RequestParam(value = "teacherId") String teacherId);
}
