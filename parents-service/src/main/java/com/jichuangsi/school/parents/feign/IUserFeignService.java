package com.jichuangsi.school.parents.feign;

import com.jichuangsi.microservice.common.model.ResponseModel;
import com.jichuangsi.school.parents.feign.impl.UserFallBackFeignServiceImpl;
import com.jichuangsi.school.parents.feign.model.ClassDetailModel;
import com.jichuangsi.school.parents.feign.model.ClassTeacherInfoModel;
import com.jichuangsi.school.parents.feign.model.TimeTableModel;
import com.jichuangsi.school.parents.feign.model.TransferStudent;
import com.jichuangsi.school.parents.model.NoticeModel;
import com.jichuangsi.school.parents.model.ParentStudentModel;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(value = "userservice",fallbackFactory = UserFallBackFeignServiceImpl.class)
public interface IUserFeignService {

    @RequestMapping("/feign/getParentStudent")
    ResponseModel<List<ParentStudentModel>> getParentStudent(@RequestBody List<String> studentIds);

    @RequestMapping("/feign/getStudentClassDetail")
    ResponseModel<ClassDetailModel> getStudentClassDetail(@RequestParam("studentId") String studentId);

    @RequestMapping("/feign/getStudentTeachers")
    ResponseModel<List<ClassTeacherInfoModel>> getStudentTeachers(@RequestParam("studentId") String studentId);

    @RequestMapping("/feign/getStudentByAccount")
    ResponseModel<TransferStudent> getStudentByAccount(@RequestParam("account") String account);

    @RequestMapping("/feign/getStudentTimeTable")
    ResponseModel<TimeTableModel> getStudentTimeTable(@RequestParam("studentId") String studentId);

    @RequestMapping("/feign/getNoticeDetails")
    ResponseModel<NoticeModel> getNoticeDetails(@RequestParam("noticeId") String noticeId);
}
