package com.jichuangsi.school.statistics.service;

import com.jichuangsi.microservice.common.model.UserInfoForToken;
import com.jichuangsi.school.statistics.exception.QuestionResultException;
import com.jichuangsi.school.statistics.model.classType.ClassStatisticsModel;

import java.util.List;

public interface IClassStatisticsService {

    List<ClassStatisticsModel> getTeachClassStatistics(UserInfoForToken userInfo) throws QuestionResultException;
}
