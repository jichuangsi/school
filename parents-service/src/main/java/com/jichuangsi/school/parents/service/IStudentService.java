package com.jichuangsi.school.parents.service;

import com.jichuangsi.microservice.common.model.UserInfoForToken;
import com.jichuangsi.school.parents.exception.ParentsException;
import com.jichuangsi.school.parents.feign.model.ClassTeacherInfoModel;
import com.jichuangsi.school.parents.feign.model.HomeWorkParentModel;
import com.jichuangsi.school.parents.model.GrowthModel;
import com.jichuangsi.school.parents.model.ParentStudentModel;
import com.jichuangsi.school.parents.model.statistics.KnowledgeStatisticsModel;
import com.jichuangsi.school.parents.model.statistics.ParentStatisticsModel;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

public interface IStudentService {

    List<ParentStudentModel> getStudentByStudentId(UserInfoForToken userInfo) throws ParentsException;

    void updateAttention(UserInfoForToken userInfo,String studentId) throws ParentsException;

    void uploadGrowth(MultipartFile file, UserInfoForToken userInfo, GrowthModel model) throws ParentsException;

    void deleteGrowth(UserInfoForToken userInfo,String studentId,String growthId) throws ParentsException;

    List<GrowthModel> getGrowths(UserInfoForToken userInfo,String studentId) throws ParentsException;

    Map<String,List<HomeWorkParentModel>> getStudentHomeWork(UserInfoForToken userInfo, String studentId) throws ParentsException;

    List<ClassTeacherInfoModel> getStudentTeachers(UserInfoForToken userInfo,String studentId) throws ParentsException;

    List<KnowledgeStatisticsModel> getParentCourseStatistics(UserInfoForToken userInfo, ParentStatisticsModel model) throws ParentsException;

    List<KnowledgeStatisticsModel> getParentHomeworkStatistics(UserInfoForToken userInfo, ParentStatisticsModel model) throws ParentsException;
}
