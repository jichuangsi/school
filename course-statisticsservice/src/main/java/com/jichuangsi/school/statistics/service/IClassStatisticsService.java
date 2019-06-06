package com.jichuangsi.school.statistics.service;

import com.jichuangsi.microservice.common.model.UserInfoForToken;
import com.jichuangsi.school.statistics.exception.QuestionResultException;
import com.jichuangsi.school.statistics.feign.model.TransferStudent;
import com.jichuangsi.school.statistics.model.classType.ClassStatisticsModel;
import com.jichuangsi.school.statistics.model.classType.SearchStudentKnowledgeModel;
import com.jichuangsi.school.statistics.model.classType.StudentKnowledgeModel;

import java.util.List;

public interface IClassStatisticsService {

    List<ClassStatisticsModel> getTeachClassStatistics(UserInfoForToken userInfo,String subject) throws QuestionResultException;

    List<StudentKnowledgeModel> getClassStudentKnowledges(UserInfoForToken userInfo,SearchStudentKnowledgeModel model) throws QuestionResultException;

    List<TransferStudent> getCourseSign(UserInfoForToken userInfo, String courseId,String classId) throws QuestionResultException;

    public List<TransferStudent> getCourseSignFeign(String courseId,String classId) throws QuestionResultException ;

    List<TransferStudent> getSignStudents(String courseId,String classId) throws QuestionResultException;
}
