package com.jichuangsi.school.parents.feign.impl;

import com.jichuangsi.microservice.common.model.ResponseModel;
import com.jichuangsi.school.parents.feign.IUserFeignService;
import com.jichuangsi.school.parents.feign.model.ClassDetailModel;
import com.jichuangsi.school.parents.feign.model.ClassTeacherInfoModel;
import com.jichuangsi.school.parents.feign.model.TimeTableModel;
//import com.jichuangsi.school.parents.feign.model.TransferStudent;
import com.jichuangsi.school.parents.model.NoticeModel;
import com.jichuangsi.school.parents.model.ParentStudentModel;
import com.jichuangsi.school.parents.model.transfer.TransferStudent;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserFallBackFeignServiceImpl implements IUserFeignService {

    private final String ERR_MSG ="调用微服务失败";

    @Override
    public ResponseModel<List<ParentStudentModel>> getParentStudent(List<String> studentIds) {
        return ResponseModel.fail("",ERR_MSG);
    }

    @Override
    public ResponseModel<ClassDetailModel> getStudentClassDetail(String studentId) {
        return ResponseModel.fail("",ERR_MSG);
    }

    @Override
    public ResponseModel<List<ClassTeacherInfoModel>> getStudentTeachers(String studentId) {
        return ResponseModel.fail("",ERR_MSG);
    }

    @Override
    public ResponseModel<TransferStudent> getStudentByAccount(String account) {
        return ResponseModel.fail("",ERR_MSG);
    }

    @Override
    public ResponseModel<TimeTableModel> getStudentTimeTable(String studentId) {
        return ResponseModel.fail("",ERR_MSG);
    }

    @Override
    public ResponseModel<NoticeModel> getNoticeDetails(String noticeId) {
        return ResponseModel.fail("",ERR_MSG);
    }
    @Override
    public ResponseModel<String> findStudentClass(String studentId) {
        return ResponseModel.fail("",ERR_MSG);
    }
}
