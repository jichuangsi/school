package com.jichuangsi.school.statistics.service.impl;

import com.jichuangsi.microservice.common.model.ResponseModel;
import com.jichuangsi.microservice.common.model.UserInfoForToken;
import com.jichuangsi.school.statistics.constant.ResultCode;
import com.jichuangsi.school.statistics.exception.QuestionResultException;
import com.jichuangsi.school.statistics.feign.ICourseFeignService;
import com.jichuangsi.school.statistics.feign.IHomeWorkFeignService;
import com.jichuangsi.school.statistics.feign.IUserFeignService;
import com.jichuangsi.school.statistics.feign.model.*;
import com.jichuangsi.school.statistics.model.QuestionStatisticsInfoModel;
import com.jichuangsi.school.statistics.model.homework.HomeworkModelForTeacher;
import com.jichuangsi.school.statistics.model.result.QuestionResultModel;
import com.jichuangsi.school.statistics.model.result.StudentResultModel;
import com.jichuangsi.school.statistics.model.result.TeacherHomeResultModel;
import com.jichuangsi.school.statistics.model.result.TeacherResultModel;
import com.jichuangsi.school.statistics.service.ICourseStatisticsService;
import com.jichuangsi.school.statistics.service.IQuestionResultService;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.*;

@Service
@CacheConfig(cacheNames = {"statisticsCache"})
public class QuestionResultServiceImpl implements IQuestionResultService {

    @Resource
    private ICourseFeignService courseFeignService;
    @Resource
    private ICourseStatisticsService courseStatisticsService;
    @Resource
    private IHomeWorkFeignService homeWorkFeignService;
    @Resource
    private IUserFeignService userFeignService;

    //获取课堂检测统计
    @Override
    @Cacheable(unless = "#result.empty",keyGenerator = "getStudentSubjectResultKeyGenerator")
    public List<StudentResultModel> getStudentSubjectResult(UserInfoForToken userInfo, String subject) throws QuestionResultException {
        if (StringUtils.isEmpty(userInfo.getClassId())) {
            throw new QuestionResultException(ResultCode.CLASS_NOT_FOUND);
        }
        if (StringUtils.isEmpty(subject)) {
            throw new QuestionResultException(ResultCode.SUBJECT_NOT_FOUND);
        }
        ResponseModel<List<ResultKnowledgeModel>> response = courseFeignService.getCourseQuestionOnWeek(userInfo.getClassId(), subject);
        if (!response.getCode().equals(ResultCode.SUCESS)) {
            throw new QuestionResultException(response.getMsg());
        }
        List<ResultKnowledgeModel> resultKnowledgeModels = response.getData();
        Map<String, Object> knowledges = new HashMap<String, Object>();
        Map<String, List<String>> results = new HashMap<String, List<String>>();
        Map<String, String> courses = new HashMap<String, String>();
        int count = 0;
        for (ResultKnowledgeModel model : resultKnowledgeModels) {
            if (model.getTransferKnowledge().getKnowledges().size() == 0) {
                Object object = knowledges.get("综合分类");
                if (null == object) {
                    knowledges.put("综合分类", 1);
                } else {
                    knowledges.put("综合分类", (int) object + 1);
                }
                //共计知识点
                count++;
                Object obj = results.get("综合分类");
                List<String> resultList = new ArrayList<String>();
                if (null != obj) {
                    resultList = (ArrayList<String>) obj;
                }
                resultList.add(model.getQuestionId());
                results.put("综合分类", resultList);
                //获取题目的courseId
                courses.put(model.getQuestionId(), model.getCourseId());
            }
            for (Knowledge knowledge : model.getTransferKnowledge().getKnowledges()) {
                //获取知识点占比
                Object object = knowledges.get(knowledge.getKnowledge());
                if (null == object) {
                    knowledges.put(knowledge.getKnowledge(), 1);
                } else {
                    knowledges.put(knowledge.getKnowledge(), (int) object + 1);
                }
                //共计知识点
                count++;
                //获取每个知识点的问题占比
                Object obj = results.get(knowledge.getKnowledge());
                List<String> resultList = new ArrayList<String>();
                if (null != obj) {
                    resultList = (ArrayList<String>) obj;
                }
                resultList.add(model.getQuestionId());
                results.put(knowledge.getKnowledge(), resultList);
                //获取题目的courseId
                courses.put(model.getQuestionId(), model.getCourseId());
            }
        }
        Set<Map.Entry<String, Object>> entrySet = knowledges.entrySet();
        List<StudentResultModel> studentResultModels = new ArrayList<StudentResultModel>();
        for (Map.Entry<String, Object> entry : entrySet) {
            StudentResultModel model = new StudentResultModel();
            model.setKnowledgeName(entry.getKey());
            int knowledge = (int) entry.getValue();
            double knowledgeRate = (double) knowledge / count;
            model.setKnowledgeRate(knowledgeRate);
            model.setResultRate(getQuestionRate(results.get(entry.getKey()), userInfo.getUserId()));
            model.setClassResultRate(getClassRate(results.get(entry.getKey()), courses));
            studentResultModels.add(model);
        }
        return studentResultModels;
    }

    //获取本人的课堂测试正确率
    private double getQuestionRate(List<String> questionIds, String userId) throws QuestionResultException {
        if (null == questionIds || !(questionIds.size() > 0) || StringUtils.isEmpty(userId)) {
            throw new QuestionResultException(ResultCode.PARAM_MISS_MSG);
        }
        QuestionRateModel model = new QuestionRateModel();
        model.setQuestionIds(questionIds);
        model.setStudentId(userId);
        ResponseModel<Double> response = courseFeignService.getStudentQuestionRate(model);
        if (!ResultCode.SUCESS.equals(response.getCode())) {
            throw new QuestionResultException(response.getMsg());
        }
        return response.getData();
    }

    //获取班级课堂测试统计
    private double getClassRate(List<String> questionIds, Map<String, String> courseIds) throws QuestionResultException {
        if (null == questionIds || !(questionIds.size() > 0) || null == courseIds || !(courseIds.size() > 0)) {
            throw new QuestionResultException(ResultCode.PARAM_MISS_MSG);
        }
        double scores = 0;
        int count = 0;
        for (String questionId : questionIds) {
            QuestionStatisticsInfoModel model = courseStatisticsService.getQuestionStatisticsInfo(questionId, courseIds.get(questionId));
            if (null != model) {
                scores += model.getAcc();
                count++;
            }
        }
        if (count > 0) {
            return scores / count;
        }
        return 0;
    }

    //获取学生习题统计
    @Override
    @Cacheable(unless = "#result.empty",keyGenerator = "getStudentSubjectResultKeyGenerator")
    public List<StudentResultModel> getStudentQuestionResult(UserInfoForToken userInfo, String subject) throws QuestionResultException {
        if (StringUtils.isEmpty(userInfo.getUserId())) {
            throw new QuestionResultException(ResultCode.CLASS_NOT_FOUND);
        }
        if (StringUtils.isEmpty(subject)) {
            throw new QuestionResultException(ResultCode.SUBJECT_NOT_FOUND);
        }
        ResponseModel<List<HomeWorkRateModel>> response = homeWorkFeignService.getStudentQuestionOnWeek(userInfo.getUserId(), subject);
        if (!ResultCode.SUCESS.equals(response.getCode())) {
            throw new QuestionResultException(response.getMsg());
        }
        List<HomeWorkRateModel> homeWorkRateModels = response.getData();
        List<String> questionIds = new ArrayList<String>();
        Map<String, String> questionsMap = new HashMap<String, String>();
        for (HomeWorkRateModel model : homeWorkRateModels) {
            questionsMap.put(model.getQuestionId(), model.getHomeworkId());
            questionIds.add(model.getQuestionId());
        }
        ResponseModel<List<ResultKnowledgeModel>> responseModel = courseFeignService.getQuestionKnowledges(questionIds);
        if (!ResultCode.SUCESS.equals(responseModel.getCode())) {
            throw new QuestionResultException(responseModel.getMsg());
        }
        List<ResultKnowledgeModel> resultKnowledgeModels = responseModel.getData();
        Map<String, Object> knowledges = new HashMap<String, Object>();
        Map<String, List<String>> results = new HashMap<String, List<String>>();
        int count = 0;
        for (ResultKnowledgeModel model : resultKnowledgeModels) {
            if (model.getTransferKnowledge().getKnowledges().size() == 0) {
                Object object = knowledges.get("综合分类");
                if (null == object) {
                    knowledges.put("综合分类", 1);
                } else {
                    knowledges.put("综合分类", (int) object + 1);
                }
                //共计知识点
                count++;
                Object obj = results.get("综合分类");
                List<String> resultList = new ArrayList<String>();
                if (null != obj) {
                    resultList = (ArrayList<String>) obj;
                }
                resultList.add(model.getQuestionId());
                results.put("综合分类", resultList);
            }
            for (Knowledge knowledge : model.getTransferKnowledge().getKnowledges()) {
                //获取知识点占比
                Object object = knowledges.get(knowledge.getKnowledge());
                if (null == object) {
                    knowledges.put(knowledge.getKnowledge(), 1);
                } else {
                    knowledges.put(knowledge.getKnowledge(), (int) object + 1);
                }
                //共计知识点
                count++;
                //获取每个知识点的问题占比
                Object obj = results.get(knowledge.getKnowledge());
                List<String> resultList = new ArrayList<String>();
                if (null != obj) {
                    resultList = (ArrayList<String>) obj;
                }
                resultList.add(model.getQuestionId());
                results.put(knowledge.getKnowledge(), resultList);
            }
        }
        Set<Map.Entry<String, Object>> entrySet = knowledges.entrySet();
        List<StudentResultModel> studentResultModels = new ArrayList<StudentResultModel>();
        for (Map.Entry<String, Object> entry : entrySet) {
            StudentResultModel model = new StudentResultModel();
            model.setKnowledgeName(entry.getKey());
            int knowledge = (int) entry.getValue();
            double knowledgeRate = (double) knowledge / count;
            model.setKnowledgeRate(knowledgeRate);
            model.setResultRate(getQuestionRateByStudentId(results.get(entry.getKey()), userInfo.getUserId()));
            model.setClassResultRate(getClassQuestionRate(results.get(entry.getKey())));
            studentResultModels.add(model);
        }
        return studentResultModels;
    }

    //获取学生习题正确率
    private double getQuestionRateByStudentId(List<String> questionIds, String studentId) throws QuestionResultException {
        QuestionRateModel model = new QuestionRateModel();
        model.setStudentId(studentId);
        model.setQuestionIds(questionIds);
        ResponseModel<Double> responseModel = homeWorkFeignService.getStudentQuestionRate(model);
        if (!ResultCode.SUCESS.equals(responseModel.getCode())) {
            throw new QuestionResultException(responseModel.getMsg());
        }
        return responseModel.getData();
    }

    //获取学生习题班级正确率
    private double getClassQuestionRate(List<String> questionIds) throws QuestionResultException {
        ResponseModel<Double> responseModel = homeWorkFeignService.getStudentQuestionClassRate(questionIds);
        if (!ResultCode.SUCESS.equals(responseModel.getCode())) {
            throw new QuestionResultException(responseModel.getMsg());
        }
        return responseModel.getData();
    }

    //获取老师课堂统计（按班级划分）
    @Override
    @Cacheable(unless = "#result == null",key = "T(String).valueOf(#classId).concat('-').concat(#subject)")
    public TeacherResultModel getTeacherCourseResult(UserInfoForToken userInfo, String classId, String subject) throws QuestionResultException {
        if (StringUtils.isEmpty(classId) || StringUtils.isEmpty(subject)) {
            throw new QuestionResultException(ResultCode.PARAM_ERR_MSG);
        }
        ResponseModel<ClassDetailModel> response = userFeignService.getClassDetail(classId);
        if (!ResultCode.SUCESS.equals(response.getCode())) {
            throw new QuestionResultException(response.getMsg());
        }
        ClassDetailModel classDetailModel = response.getData();
        ResponseModel<List<ResultKnowledgeModel>> responseModel = courseFeignService.getCourseQuestionOnWeek(classId, subject);
        if (!ResultCode.SUCESS.equals(responseModel.getCode())) {
            throw new QuestionResultException(responseModel.getMsg());
        }
        List<ResultKnowledgeModel> resultKnowledgeModels = responseModel.getData();
        Map<String, Object> knowledges = new HashMap<String, Object>();
        Map<String, List<String>> results = new HashMap<String, List<String>>();
        Map<String, String> courses = new HashMap<String, String>();
        int count = 0;
        for (ResultKnowledgeModel model : resultKnowledgeModels) {
            if (model.getTransferKnowledge().getKnowledges().size() == 0) {
                Object object = knowledges.get("综合分类");
                if (null == object) {
                    knowledges.put("综合分类", 1);
                } else {
                    knowledges.put("综合分类", (int) object + 1);
                }
                //共计知识点
                count++;
                Object obj = results.get("综合分类");
                List<String> resultList = new ArrayList<String>();
                if (null != obj) {
                    resultList = (ArrayList<String>) obj;
                }
                resultList.add(model.getQuestionId());
                results.put("综合分类", resultList);
                //获取题目的courseId
                courses.put(model.getQuestionId(), model.getCourseId());
            }
            for (Knowledge knowledge : model.getTransferKnowledge().getKnowledges()) {
                //获取知识点占比
                Object object = knowledges.get(knowledge.getKnowledge());
                if (null == object) {
                    knowledges.put(knowledge.getKnowledge(), 1);
                } else {
                    knowledges.put(knowledge.getKnowledge(), (int) object + 1);
                }
                //共计知识点
                count++;
                //获取每个知识点的问题占比
                Object obj = results.get(knowledge.getKnowledge());
                List<String> resultList = new ArrayList<String>();
                if (null != obj) {
                    resultList = (ArrayList<String>) obj;
                }
                resultList.add(model.getQuestionId());
                results.put(knowledge.getKnowledge(), resultList);
                //获取题目的courseId
                courses.put(model.getQuestionId(), model.getCourseId());
            }
        }
        Set<Map.Entry<String, Object>> entrySet = knowledges.entrySet();
        List<StudentResultModel> studentResultModels = new ArrayList<StudentResultModel>();
        for (Map.Entry<String, Object> entry : entrySet) {
            StudentResultModel model = new StudentResultModel();
            model.setKnowledgeName(entry.getKey());
            int knowledge = (int) entry.getValue();
            double knowledgeRate = (double) knowledge / count;
            model.setKnowledgeRate(knowledgeRate);
            //获取gradeId
            model.setGradeResultRate(getGradeResultRate(results.get(entry.getKey()), classDetailModel.getGradeId()));
            model.setClassResultRate(getClassRate(results.get(entry.getKey()), courses));
            studentResultModels.add(model);
        }
        TeacherResultModel teacherResultModel = new TeacherResultModel();
        teacherResultModel.setStudentNum(classDetailModel.getStudentNum());
        teacherResultModel.setStudentResultModels(studentResultModels);
        return teacherResultModel;
    }

    //获取年级统计
    private double getGradeResultRate(List<String> questionIds, String gradeId) throws QuestionResultException {
        QuestionRateModel model = new QuestionRateModel();
        model.setQuestionIds(questionIds);
        model.setGradeId(gradeId);
        ResponseModel<Double> responseModel = courseFeignService.getQuetsionIdsCrossByMD5(model);
        if (!ResultCode.SUCESS.equals(responseModel.getCode())) {
            throw new QuestionResultException(responseModel.getMsg());
        }
        return responseModel.getData();
    }

    //获取近一周该老师的习题列表
    @Override
    @Cacheable(unless = "#result.empty",keyGenerator = "getSubjectQuestionKeyGenerator")
    public List<HomeworkModelForTeacher> getSubjectQuestion(UserInfoForToken userInfo, String classId) throws QuestionResultException {
        if (StringUtils.isEmpty(userInfo.getUserId()) || StringUtils.isEmpty(classId))
            throw new QuestionResultException(ResultCode.PARAM_MISS_MSG);
        ResponseModel<List<HomeworkModelForTeacher>> responseModel = homeWorkFeignService.getHomeWorkByTeacherIdAndclassId(userInfo.getUserId(), classId);
        if (!ResultCode.SUCESS.equals(responseModel.getCode())) {
            throw new QuestionResultException(responseModel.getMsg());
        }
        return responseModel.getData();
    }

    //获取该次习题测验的统计结果
    @Override
    @Cacheable(unless = "#result == null",key = "T(String).valueOf(#homeId).concat('-').concat(#classId)")
    public TeacherHomeResultModel getSubjectQuestionRate(UserInfoForToken userInfoForToken, String homeId, String classId) throws QuestionResultException {
        if (StringUtils.isEmpty(userInfoForToken.getUserId()) || StringUtils.isEmpty(homeId) || StringUtils.isEmpty(classId)) {
            throw new QuestionResultException(ResultCode.PARAM_MISS_MSG);
        }
        ResponseModel<TeacherHomeResultModel> response = homeWorkFeignService.getHomeWorkRate(userInfoForToken.getUserId(), homeId);
        if (!ResultCode.SUCESS.equals(response.getCode())) {
            throw new QuestionResultException(response.getMsg());
        }
        TeacherHomeResultModel model = response.getData();
        ResponseModel<ClassDetailModel> responseModel = userFeignService.getClassDetail(classId);
        if (!ResultCode.SUCESS.equals(responseModel.getCode())) {
            throw new QuestionResultException(responseModel.getMsg());
        }
        ClassDetailModel classDetailModel = responseModel.getData();
        model.setStudentNum(classDetailModel.getStudentNum());
        for (QuestionResultModel questionResultModel : model.getObjective()) {
            questionResultModel.setWrongNum(classDetailModel.getStudentNum() - questionResultModel.getTrueNum());
        }
        for (QuestionResultModel questionResultModel : model.getSubjective()) {
            questionResultModel.setWrongNum(classDetailModel.getStudentNum() - questionResultModel.getTrueNum());
        }
        return model;
    }
}
