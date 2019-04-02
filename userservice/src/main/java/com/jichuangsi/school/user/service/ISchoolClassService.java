package com.jichuangsi.school.user.service;

import com.jichuangsi.school.user.exception.ClassServiceException;
import com.jichuangsi.school.user.exception.SchoolServiceException;
import com.jichuangsi.school.user.feign.model.ClassDetailModel;
import com.jichuangsi.school.user.model.org.ClassModel;

import java.util.List;

public interface ISchoolClassService {

    void saveOrUpClass(String schoolId, String gradeId, ClassModel classModel) throws ClassServiceException;

    void deleteClass(String schoolId, String gradeId, String classId) throws ClassServiceException;

    ClassModel getClassInfo(String schoolId, String gradeId, String classId) throws ClassServiceException;

    ClassDetailModel getClassDetail(String classId) throws ClassServiceException;

    List<ClassModel> getClassesByGradeId(String gradeId) throws SchoolServiceException;
}
