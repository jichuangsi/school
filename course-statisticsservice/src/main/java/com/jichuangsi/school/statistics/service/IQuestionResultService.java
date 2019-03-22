package com.jichuangsi.school.statistics.service;

import com.jichuangsi.microservice.common.model.UserInfoForToken;
import com.jichuangsi.school.statistics.exception.QuestionResultException;
import com.jichuangsi.school.statistics.model.homework.HomeworkModelForTeacher;
import com.jichuangsi.school.statistics.model.result.StudentResultModel;
import com.jichuangsi.school.statistics.model.result.TeacherHomeResultModel;
import com.jichuangsi.school.statistics.model.result.TeacherResultModel;

import java.util.List;

public interface IQuestionResultService {

    //获取学生课堂检测统计
    List<StudentResultModel> getStudentSubjectResult(UserInfoForToken userInfo, String subject) throws QuestionResultException;

    //获取学生习题检测统计
    List<StudentResultModel> getStudentQuestionResult(UserInfoForToken userInfo, String subject) throws QuestionResultException;

    //获取老师课堂检测统计
    TeacherResultModel getTeacherCourseResult(UserInfoForToken userInfo, String classId, String subject) throws QuestionResultException;

    //获取近一周练习题
    List<HomeworkModelForTeacher> getSubjectQuestion(UserInfoForToken userInfo, String classId) throws QuestionResultException;

    //获取该次习题测验的统计结果
    TeacherHomeResultModel getSubjectQuestionRate(UserInfoForToken userInfoForToken, String homeId,String classId) throws QuestionResultException;
}
