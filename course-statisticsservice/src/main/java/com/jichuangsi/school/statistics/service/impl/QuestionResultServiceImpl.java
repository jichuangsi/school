package com.jichuangsi.school.statistics.service.impl;

import com.jichuangsi.microservice.common.model.ResponseModel;
import com.jichuangsi.microservice.common.model.UserInfoForToken;
import com.jichuangsi.school.statistics.constant.ResultCode;
import com.jichuangsi.school.statistics.exception.QuestionResultException;
import com.jichuangsi.school.statistics.feign.ICourseFeignService;
import com.jichuangsi.school.statistics.feign.IHomeWorkFeignService;
import com.jichuangsi.school.statistics.feign.IHomeWorkStaticsService;
import com.jichuangsi.school.statistics.feign.IUserFeignService;
import com.jichuangsi.school.statistics.feign.model.*;
import com.jichuangsi.school.statistics.model.*;
import com.jichuangsi.school.statistics.model.capability.*;
import com.jichuangsi.school.statistics.model.classType.CapabilityStudentModel;
import com.jichuangsi.school.statistics.model.homework.HomeworkModelForTeacher;
import com.jichuangsi.school.statistics.model.homework.constant.Result;
import com.jichuangsi.school.statistics.model.result.QuestionResultModel;
import com.jichuangsi.school.statistics.model.result.StudentResultModel;
import com.jichuangsi.school.statistics.model.result.TeacherHomeResultModel;
import com.jichuangsi.school.statistics.model.result.TeacherResultModel;
import com.jichuangsi.school.statistics.service.ICourseStatisticsService;
import com.jichuangsi.school.statistics.service.IQuestionResultService;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.math.BigDecimal;
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

    @Resource
    private IHomeWorkStaticsService homeWorkStaticsService;

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


    @Override
    public CapabilityStudentModel getClassStudentCapability(SearchStudentCapabilityModel model)throws QuestionResultException {
        //判断classID和QuestionId是否为空

        if (StringUtils.isEmpty(model.getClassId())) {
            throw new QuestionResultException(ResultCode.PARAM_MISS_MSG);
        }
        ResponseModel<List<SearchCapabilityModel>> responseModel1 = homeWorkStaticsService.getQuestionBySubjectId(model.getSubjectId());
     if (responseModel1.getData()==null){//无这个月数据
         throw new QuestionResultException(ResultCode.SELECT_NULL_MSG);
     }
      //  System.out.println(responseModel1.getData().size());
        List<String> questionId = new ArrayList<String>();
        for (int r = 0; r < responseModel1.getData().size(); r++) {
            questionId.add(responseModel1.getData().get(r).getId());
        }
        //总
        Map<String, Double> capa = new HashMap<String, Double>();
       /* Knowledge know=null;
        Knowledge[] knowledges1=new Knowledge[5];*/
        ResponseModel<List<SearchCapabilityModel>> searchCapabilityEntity = null;
        ResponseModel<List<StudentQuestionIdsModel>> sq = null;
        Query query = new Query();
        Criteria criteria = null;
        if (responseModel1.getData() != null) {
            searchCapabilityEntity = homeWorkStaticsService.getQuestion(questionId);

            sq = homeWorkStaticsService.getQuestionResult(questionId);
        }
        Map<String, Integer> capability = new HashMap<String, Integer>();
        //Knowledge capability=new Knowledge();
        int sum = 0;
        int mem = 0;
        int compreh = 0, synthe = 0, anal = 0, appl = 0,other=0;
        if (searchCapabilityEntity != null) {//查询全部认知能力的占比
            for (SearchCapabilityModel model1 : searchCapabilityEntity.getData()) {
                if (model1.getKnowledges() != null) {

                    for (int k = 0; k < model1.getKnowledges().size(); k++) {
                        sum++;

                        if (model1.getKnowledges().get(k).getCapabilityId().equals("1")) {
                            mem++;
                            capability.put("记忆", mem);
                        } else if (model1.getKnowledges().get(k).getCapabilityId().equals("2")) {
                            compreh++;
                            capability.put("理解", compreh);
                        } else if (model1.getKnowledges().get(k).getCapabilityId().equals("3")) {
                            anal++;
                            capability.put("分析", anal);
                        } else if (model1.getKnowledges().get(k).getCapabilityId().equals("4")) {
                            appl++;
                            capability.put("应用", appl);
                        } else if (model1.getKnowledges().get(k).getCapabilityId().equals("5")) {
                            synthe++;
                            capability.put("综合", synthe);
                            System.out.println(synthe);
                        }else {
                            other++;
                            capability.put("其他",other);
                        }
                    }
                }
            }

            Set<Map.Entry<String, Integer>> entrySet = capability.entrySet();
            for (Map.Entry<String, Integer> entry : entrySet) {
                int ca = 0;
                double capabi = 0.0;

                if (entry.getKey().equals("记忆")) {
                    ca = (int) entry.getValue();
                    capabi = (double) ca / sum;
                    capa.put(entry.getKey(), capabi);
                } else if (entry.getKey().equals("理解")) {
                    ca = (int) entry.getValue();
                    capabi = (double) ca / sum;
                    capa.put(entry.getKey(), capabi);
                } else if (entry.getKey().equals("分析")) {
                    ca = (int) entry.getValue();
                    capabi = (double) ca / sum;
                    capa.put(entry.getKey(), capabi);
                } else if (entry.getKey().equals("应用")) {
                    ca = (int) entry.getValue();
                    capabi = (double) ca / sum;
                    capa.put(entry.getKey(), capabi);
                } else if (entry.getKey().equals("综合")) {
                    ca = (int) entry.getValue();
                    capabi = (double) ca / sum;
                    capa.put(entry.getKey(), capabi);
                }else if (entry.getKey().equals("其他")){
                    ca = (int) entry.getValue();
                    capabi = (double) ca / sum;
                    capa.put(entry.getKey(), capabi);
                }

            }
        }
        //记录正确问题ID
        List<String> momor = new ArrayList<String>();
        List<String> comp = new ArrayList<String>();
        List<String> ana = new ArrayList<String>();
        List<String> app = new ArrayList<String>();
        List<String> synt = new ArrayList<String>();

        Map<String, Double> capabilityPro = new HashMap<String, Double>();

        //记录个认知能力及问题id
        Map<String, List<String>> question = new HashMap<String, List<String>>();

        int momoryTrue = 0;
        int comprehendTrue = 0, synthesizeTure = 0, analyzeTure = 0, applyTure = 0,otherTure=0;
        if (searchCapabilityEntity != null && sq != null) {//查询各认知能力各自对错的占比
            for (SearchCapabilityModel model1 : searchCapabilityEntity.getData()) {//根据ID查认知
                if (model1.getKnowledges() != null) {
                    for (Knowledge knowledge : model1.getKnowledges()) {
                        for (int i = 0; i < sq.getData().size(); i++) {//（根据ID查询答案）

                            if (model1.getId().equals(sq.getData().get(i).getQuestionId())) {
                                if (knowledge.getCapabilityId().equals("1")) {
                                    if (sq.getData().get(i).getResult() != null) {
                                        if (sq.getData().get(i).getResult().equals(Result.CORRECT.getName())) {//正确
                                            momoryTrue++;
                                            momor.add(sq.getData().get(i).getQuestionId());
                                        }
                                    }
                                }
                                if (knowledge.getCapabilityId().equals("2")) {
                                    if (sq.getData().get(i).getResult() != null) {
                                        if (sq.getData().get(i).getResult().equals(Result.CORRECT.getName())) {//正确
                                            comprehendTrue++;
                                            comp.add(sq.getData().get(i).getQuestionId());
                                        }
                                    }
                                }
                                if (knowledge.getCapabilityId().equals("3")) {
                                    if (sq.getData().get(i).getResult() != null) {
                                        if (sq.getData().get(i).getResult().equals(Result.CORRECT.getName())) {//正确
                                            analyzeTure++;
                                            ana.add(sq.getData().get(i).getQuestionId());
                                        }
                                    }
                                }
                                if (knowledge.getCapabilityId().equals("4")) {
                                    if (sq.getData().get(i).getResult() != null) {
                                        if (sq.getData().get(i).getResult().equals(Result.CORRECT.getName())) {//正确
                                            applyTure++;
                                            app.add(sq.getData().get(i).getQuestionId());
                                        }
                                    }
                                }
                                if (knowledge.getCapabilityId().equals("5")) {
                                    if (sq.getData().get(i).getResult() != null) {

                                        if (sq.getData().get(i).getResult().equals(Result.CORRECT.getName())) {//正确
                                            synthesizeTure++;
                                            synt.add(sq.getData().get(i).getQuestionId());
                                        }
                                    }
                                }
                                if (knowledge.getCapabilityId()=="" ||knowledge.getCapabilityId()==null) {
                                    if (sq.getData().get(i).getResult() != null) {
                                        if (sq.getData().get(i).getResult().equals(Result.CORRECT.getName())) {//正确
                                            otherTure++;
                                            synt.add(sq.getData().get(i).getQuestionId());
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }


        //保留两位数字
        BigDecimal bg = null;
        double f1 = 0.0;
        Memory[] memory = new Memory[2];
        Comprehend[] comprehend = new Comprehend[2];
        Analy[] analy = new Analy[2];
        Apply[] apply = new Apply[2];
        Synthesize[] synthesize = new Synthesize[2];
        Other[] others=new Other[2];
        CapabilityTrueOrFalse[] capabilityTrueOrFalses = new CapabilityTrueOrFalse[1];

        if (mem != 0 && momoryTrue != 0) {
            double d = (double) momoryTrue / mem;
            bg = new BigDecimal(d);
            f1 = bg.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
            Memory m = new Memory(f1, "记忆");
            Memory m1 = new Memory(1 - f1, "错误");
            memory[0] = m;
            memory[1] = m1;
        } else if (mem != 0 && momoryTrue == 0) {

            Memory m = new Memory(0.0, "记忆");
            Memory m1 = new Memory(1.0, "错误");
            memory[0] = m;
            memory[1] = m1;
        }
        if (compreh != 0 && comprehendTrue != 0) {
            double d = (double) comprehendTrue / compreh;
            bg = new BigDecimal(d);
            f1 = bg.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
            Comprehend c1 = new Comprehend(f1, "理解");
            Comprehend c2 = new Comprehend(1 - f1, "错误");
            comprehend[0] = c1;
            comprehend[1] = c2;
        } else if (compreh != 0 && comprehendTrue == 0) {

            Comprehend c1 = new Comprehend(0.0, "理解");
            Comprehend c2 = new Comprehend(1.0, "错误");
            comprehend[0] = c1;
            comprehend[1] = c2;
        }
        if (anal != 0 && analyzeTure != 0) {
            double d = (double) analyzeTure / anal;
            bg = new BigDecimal(d);
            f1 = bg.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
            Analy an1 = new Analy(f1, "分析");
            Analy an2 = new Analy(1 - f1, "错误");
            analy[0] = an1;
            analy[1] = an2;
        } else if (anal != 0 && analyzeTure == 0) {

            Analy an1 = new Analy(0.0, "分析");
            Analy an2 = new Analy(1.0, "错误");
            analy[0] = an1;
            analy[1] = an2;
        }
        if (appl != 0 && applyTure != 0) {
            double d = (double) applyTure / appl;
            bg = new BigDecimal(d);
            f1 = bg.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
            Apply ap1 = new Apply(f1, "应用");
            Apply ap2 = new Apply(1 - f1, "错误");
            apply[0] = ap1;
            apply[1] = ap2;
        } else if (appl != 0 && applyTure == 0) {

            Apply ap1 = new Apply(0.0, "应用");
            Apply ap2 = new Apply(1.0, "错误");
            apply[0] = ap1;
            apply[1] = ap2;
        }
        if (synthe != 0 && synthesizeTure != 0) {
            double d = (double) synthesizeTure / synthe;
            bg = new BigDecimal(d);
            f1 = bg.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
            Synthesize sy1 = new Synthesize(f1, "综合");
            Synthesize sy2 = new Synthesize(1 - f1, "错误");
            synthesize[0] = sy1;
            synthesize[1] = sy2;

        } else if (synthe != 0 && synthesizeTure == 0) {

            Synthesize sy1 = new Synthesize(0.0, "综合");
            Synthesize sy2 = new Synthesize(1.0, "错误");
            synthesize[0] = sy1;
            synthesize[1] = sy2;
        }
        if (other != 0 && otherTure != 0) {
            double d = (double) otherTure / other;
            bg = new BigDecimal(d);
            f1 = bg.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
           Other sy1 = new Other(f1, "其他");
            Other sy2 = new Other(1 - f1, "错误");
            others[0] = sy1;
            others[1] = sy2;

        } else if (other != 0 && otherTure == 0) {

            Other sy1 = new Other(0.0, "其他");
            Other sy2 = new Other(1.0, "错误");
            others[0] = sy1;
            others[1] = sy2;
        }
        CapabilityTrueOrFalse capabilitytf = new CapabilityTrueOrFalse(memory, apply, analy, comprehend, synthesize,others);
        capabilityTrueOrFalses[0] = capabilitytf;

        Capability know = null;
        Capability[] knowledges1 = new Capability[5];
        //将数据加入对象(总占比)
        for (int c = 0; c < capa.size(); c++) {

            Set<String> set = capa.keySet();
            for (String s : set) {
                if (s.equals("分析")) {
                    bg = new BigDecimal(capa.get("分析"));
                    f1 = bg.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
                    know = new Capability(f1 * 100, "分析");
                    knowledges1[0] = know;
                } else if (s.equals("综合")) {
                    bg = new BigDecimal(capa.get("综合"));
                    f1 = bg.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
                    know = new Capability(f1 * 100, "综合");
                    System.out.println(f1);
                    knowledges1[1] = know;
                } else if (s.equals("理解")) {
                    bg = new BigDecimal(capa.get("理解"));
                    f1 = bg.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
                    know = new Capability(f1 * 100, "理解");
                    knowledges1[2] = know;
                } else if (s.equals("记忆")) {
                    bg = new BigDecimal(capa.get("记忆"));
                    f1 = bg.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
                    know = new Capability(f1 * 100, "记忆");
                    knowledges1[3] = know;
                } else if (s.equals("应用")) {
                    bg = new BigDecimal(capa.get("应用"));
                    f1 = bg.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
                    know = new Capability(f1 * 100, "应用");
                    knowledges1[4] = know;
                }
            }
        }
        CapabilityStudentModel testStudentModel =new CapabilityStudentModel();
        testStudentModel.setKnowledges(knowledges1);
        testStudentModel.setCapabilityTrueOrFalses(capabilityTrueOrFalses);
        //testStudentModel.setStudentKnowledges(studentCapability);
      // testStudentModel.setStudentCapability(capabilityByStudentId);
        return testStudentModel;
    }


}
