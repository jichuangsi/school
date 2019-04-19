package com.jichuangsi.school.user.feign.service;

import com.jichuangsi.school.user.exception.FeignControllerException;
import com.jichuangsi.school.user.feign.model.*;
import com.jichuangsi.school.user.model.backstage.TimeTableModel;
import com.jichuangsi.school.user.model.school.SchoolModel;
import com.jichuangsi.school.user.model.transfer.TransferStudent;

import java.util.List;

public interface IFeignService {

    ClassDetailModel findClassDetailByClassId(String classId) throws FeignControllerException;

    List<String> getClassIdsByTeacherId(String teacherId) throws FeignControllerException;

    List<ClassDetailModel> findClassDetailByClassIds(List<String> classIds) throws FeignControllerException;

    List<TransferStudent> findStudentsByClassId(String classId) throws FeignControllerException;

    SchoolModel findSchoolBySchoolId(String schoolId) throws FeignControllerException;

    List<SchoolModel> findBackSchools() throws FeignControllerException;

    List<ParentStudentModel> getParentStudent(List<String> studentIds) throws FeignControllerException;

    ClassDetailModel getStudentClassDetail(String studentId) throws FeignControllerException;

    List<ClassTeacherInfoModel> getStudentTeachers(String studentId) throws FeignControllerException;

    TransferStudent getStudentByAccount(String account) throws FeignControllerException;

    TimeTableModel getStudentTimeTable(String studentId) throws FeignControllerException;

    NoticeModel getNoticeDetailByNoticeId(String noticeId) throws FeignControllerException;

    void sendParentStudentMsg(CourseSignModel model) throws FeignControllerException;
}
