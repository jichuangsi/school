package com.jichuangsi.school.parents.service;

import com.jichuangsi.microservice.common.model.ResponseModel;
import com.jichuangsi.microservice.common.model.UserInfoForToken;
import com.jichuangsi.school.parents.exception.ParentsException;
import com.jichuangsi.school.parents.feign.model.*;
import com.jichuangsi.school.parents.model.common.PageHolder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

public interface ISchoolPerformanceService {
    ResponseModel<PageHolder<CourseForStudent>> getHistory(  CourseForStudentId pageInform) throws ParentsException;
    ResponseModel<PageHolder<TestModelForStudent>> getHistory(  SearchTestModelId searchTestModel) throws ParentsException;
    ResponseModel<List<CourseForStudent>> getCourseCommend(String studentId,String pageSize,String pageNum) throws ParentsException;

}
