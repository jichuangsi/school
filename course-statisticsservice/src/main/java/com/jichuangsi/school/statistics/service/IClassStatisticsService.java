package com.jichuangsi.school.statistics.service;

import com.jichuangsi.microservice.common.model.UserInfoForToken;
import com.jichuangsi.school.statistics.exception.QuestionResultException;
import com.jichuangsi.school.statistics.model.classType.ClassStatisticsModel;
import com.jichuangsi.school.statistics.model.classType.SearchStudentKnowledgeModel;
import com.jichuangsi.school.statistics.model.classType.StudentKnowledgeModel;

import java.util.List;

public interface IClassStatisticsService {

    List<ClassStatisticsModel> getTeachClassStatistics(UserInfoForToken userInfo) throws QuestionResultException;

    List<StudentKnowledgeModel> getClassStudentKnowledges(UserInfoForToken userInfo,SearchStudentKnowledgeModel model) throws QuestionResultException;
}
