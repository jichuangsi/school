package com.jichuangsi.school.questionsrepository.service;

import com.jichuangsi.school.questionsrepository.model.transfer.TransferSchool;
import com.jichuangsi.school.questionsrepository.model.transfer.TransferTeacher;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "userservice")
public interface IUserInfoService {

    @RequestMapping("/getUserInfoForTeacher")
    public TransferTeacher getUserForTeacherById(@RequestParam(value = "teacherId") String teacherId);

    @RequestMapping("/getSchoolInfoForTeacher")
    public TransferSchool getSchoolInfoById(@RequestParam(value = "userId") String userId);
}
