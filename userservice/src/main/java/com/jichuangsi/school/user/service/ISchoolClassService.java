package com.jichuangsi.school.user.service;

import com.jichuangsi.school.user.exception.ClassServiceException;
import com.jichuangsi.school.user.feign.model.ClassDetailModel;
import com.jichuangsi.school.user.model.org.Class;

public interface ISchoolClassService {

    void saveOrUpClass(String schoolId, String gradeId, Class classModel) throws ClassServiceException;

    void deleteClass(String schoolId, String gradeId, String classId) throws ClassServiceException;

    Class getClassInfo(String schoolId, String gradeId, String classId) throws ClassServiceException;

    ClassDetailModel getClassDetail(String classId) throws ClassServiceException;
}
