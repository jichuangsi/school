package com.jichuangsi.school.user.service;

import com.jichuangsi.school.user.exception.SchoolServiceException;
import com.jichuangsi.school.user.model.school.GradeModel;
import com.jichuangsi.school.user.model.school.SchoolModel;

public interface ISchoolService {

    void insertSchool(SchoolModel model) throws SchoolServiceException;

    void updateSchool(SchoolModel model) throws SchoolServiceException;

    void insertGrade(GradeModel model) throws SchoolServiceException;

    void updateGrade(GradeModel model) throws  SchoolServiceException;
}
