package com.jichuangsi.school.homeworkservice.feign.service;

import com.jichuangsi.school.homeworkservice.exception.FeignControllerException;
import com.jichuangsi.school.homeworkservice.feign.model.HomeWorkRateModel;
import com.jichuangsi.school.homeworkservice.feign.model.QuestionRateModel;
import com.jichuangsi.school.homeworkservice.feign.model.TeacherHomeResultModel;
import com.jichuangsi.school.homeworkservice.model.HomeworkModelForTeacher;

import java.util.List;

public interface IFeignService {

    double getStudentQuestionRate(QuestionRateModel model) throws FeignControllerException;

    List<HomeWorkRateModel> getStudentQuestionOnWeek(String studentId, String subject) throws FeignControllerException;

    double getStudentClassQuestionRate(List<String> questionIds) throws  FeignControllerException;

    List<HomeworkModelForTeacher> getHomeWorkByTeacherIdAndclassId(String teacherId,String classId) throws FeignControllerException;

    TeacherHomeResultModel getHomeWorkRate(String teacherId,String homeId) throws  FeignControllerException;
}
