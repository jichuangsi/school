package com.jichuangsi.school.user.feign.service;

import com.jichuangsi.school.user.entity.UserInfo;
import com.jichuangsi.school.user.feign.model.ClassStudentModel;
import com.jichuangsi.school.user.model.school.GradeModel;

import java.util.List;

public interface FeignReportService {
    //根据年级班级查询学生
    List<ClassStudentModel> getStudentByGradeId(String gradeId);
    //根据学校查询年级
    List<GradeModel> getGradeBySchoolId(String schoolId);

    //根据年级和科目查询老师
    List<UserInfo> getTestByGradeIdAndSubjectName(String gradeId, String subjectName);
}
