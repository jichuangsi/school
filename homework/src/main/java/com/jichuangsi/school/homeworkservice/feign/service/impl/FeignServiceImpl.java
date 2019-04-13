package com.jichuangsi.school.homeworkservice.feign.service.impl;

import com.jichuangsi.school.homeworkservice.constant.QuestionType;
import com.jichuangsi.school.homeworkservice.constant.Result;
import com.jichuangsi.school.homeworkservice.constant.ResultCode;
import com.jichuangsi.school.homeworkservice.constant.Status;
import com.jichuangsi.school.homeworkservice.entity.*;
import com.jichuangsi.school.homeworkservice.exception.FeignControllerException;
import com.jichuangsi.school.homeworkservice.exception.StudentHomeworkServiceException;
import com.jichuangsi.school.homeworkservice.feign.model.*;
import com.jichuangsi.school.homeworkservice.feign.model.statistics.KnowledgeStatisticsModel;
import com.jichuangsi.school.homeworkservice.feign.model.statistics.ParentStatisticsModel;
import com.jichuangsi.school.homeworkservice.feign.service.IFeignService;
import com.jichuangsi.school.homeworkservice.model.HomeworkModelForStudent;
import com.jichuangsi.school.homeworkservice.model.HomeworkModelForTeacher;
import com.jichuangsi.school.homeworkservice.model.ResultKnowledgeModel;
import com.jichuangsi.school.homeworkservice.model.transfer.TransferKnowledge;
import com.jichuangsi.school.homeworkservice.repository.HomeworkRepository;
import com.jichuangsi.school.homeworkservice.repository.IStudentHomeworkCollectionRepository;
import com.jichuangsi.school.homeworkservice.repository.QuestionRepository;
import com.jichuangsi.school.homeworkservice.repository.StudentAnswerRepository;
import com.jichuangsi.school.homeworkservice.service.IStudentHomeworkService;
import com.jichuangsi.school.homeworkservice.utils.MappingEntity2ModelConverter;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.*;

@Service
public class FeignServiceImpl implements IFeignService {

    @Resource
    private MongoTemplate mongoTemplate;
    @Resource
    private IStudentHomeworkService studentHomeworkService;
    @Resource
    private StudentAnswerRepository studentAnswerRepository;
    @Resource
    private HomeworkRepository homeworkRepository;
    @Resource
    private QuestionRepository questionRepository;
    @Resource
    private IStudentHomeworkCollectionRepository studentHomeworkCollectionRepository;

    @Override
    public double getStudentQuestionRate(QuestionRateModel model) throws FeignControllerException {
        List<String> questionIds = model.getQuestionIds();
        String studentId = model.getStudentId();
        if (null == questionIds || !(questionIds.size() > 0) || StringUtils.isEmpty(studentId)) {
            throw new FeignControllerException(ResultCode.PARAM_ERR_MSG);
        }
        List<StudentAnswer> studentAnswers = studentAnswerRepository.findByQuestionIdInAndStudentId(questionIds, studentId);
        if (!(studentAnswers.size() > 0)) {
            throw new FeignControllerException(ResultCode.HOMEWORK_NOT_EXISTED);
        }
        double count = 0;
        for (StudentAnswer studentAnswer : studentAnswers) {
            if (StringUtils.isEmpty(studentAnswer)) {
                continue;
            }
            if (Result.CORRECT.getName().equals(studentAnswer.getResult())) {
                count = count + 1;
            } else if (Result.PASS.getName().equals(studentAnswer.getResult())) {

            }
        }
        return count / studentAnswers.size();
    }

    @Override
    public List<HomeWorkRateModel> getStudentQuestionOnWeek(String studentId, String subject) throws FeignControllerException {
        try {
            List<HomeworkModelForStudent> homeworkModelForStudents = studentHomeworkService.getHomeworksListOnWeek(studentId, subject);
            List<HomeWorkRateModel> questionIds = new ArrayList<HomeWorkRateModel>();
            for (HomeworkModelForStudent modelForStudent : homeworkModelForStudents) {
                for (String questionId : modelForStudent.getQuestionIds()) {
                    HomeWorkRateModel model = new HomeWorkRateModel();
                    model.setQuestionId(questionId);
                    model.setHomeworkId(modelForStudent.getHomeworkId());
                    questionIds.add(model);
                }
            }
            return questionIds;
        } catch (StudentHomeworkServiceException e) {
            throw new FeignControllerException(e.getMessage());
        }
    }

    @Override
    public double getStudentClassQuestionRate(List<String> questionIds) throws FeignControllerException {
        if (null == questionIds || !(questionIds.size() > 0)) {
            throw new FeignControllerException(ResultCode.HOMEWORK_NOT_EXISTED);
        }
        List<StudentAnswer> studentAnswers = studentAnswerRepository.findByQuestionIdIn(questionIds);
        if (!(studentAnswers.size() > 0)) {
            throw new FeignControllerException(ResultCode.HOMEWORK_NOT_EXISTED);
        }
        double count = 0;
        for (StudentAnswer studentAnswer : studentAnswers) {
            if (StringUtils.isEmpty(studentAnswer)) {
                continue;
            }
            if (Result.CORRECT.getName().equals(studentAnswer.getResult())) {
                count = count + 1;
            } else if (Result.PASS.getName().equals(studentAnswer.getResult())) {

            }
        }
        return count / studentAnswers.size();
    }

    @Override
    public List<HomeworkModelForTeacher> getHomeWorkByTeacherIdAndclassId(String teacherId, String classId) throws FeignControllerException {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.WEEK_OF_MONTH, -1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        List<HomeworkModelForTeacher> homeworks = convertHomeworkList(mongoTemplate.find(
                new Query(
                        Criteria.where("teacherId").is(teacherId).and("status").is(Status.FINISH.getName()).
                                and("classId").is(classId).and("endTime").gte(calendar.getTimeInMillis())
                ).with(new Sort(Sort.Direction.DESC, "publishTime")), Homework.class));
        return homeworks;
    }

    private List<HomeworkModelForTeacher> convertHomeworkList(List<Homework> homeworks) {
        List<HomeworkModelForTeacher> homeworkModelForTeachers = new ArrayList<HomeworkModelForTeacher>();
        homeworks.forEach(homework -> {
            homeworkModelForTeachers.add(MappingEntity2ModelConverter.ConvertTeacherHomework(homework));
        });
        return homeworkModelForTeachers;
    }

    @Override
    public TeacherHomeResultModel getHomeWorkRate(String teacherId, String homeId) throws FeignControllerException {
        if (StringUtils.isEmpty(teacherId) || StringUtils.isEmpty(homeId)) {
            throw new FeignControllerException(ResultCode.PARAM_MISS_MSG);
        }
        Homework homework = homeworkRepository.findByTeacherIdAndId(teacherId, homeId);
        if (null == homeId) {
            throw new FeignControllerException(ResultCode.SELECT_NULL_MSG);
        }
        int countSubjective = 0;
        int countObjective = 0;
        int countSubmit = 0;
        List<String> questionIds = homework.getQuestionIds();
        List<QuestionResultModel> objective = new ArrayList<QuestionResultModel>();
        List<QuestionResultModel> subjective = new ArrayList<QuestionResultModel>();
        for (String questionId : questionIds) {
            Question question = questionRepository.findFirstById(questionId);
            if (null == question) {
                continue;
            }
            QuestionResultModel model = new QuestionResultModel();
            List<StudentAnswer> studentAnswers = studentAnswerRepository.findAllByQuestionId(questionId);
            int count = 0;
            for (StudentAnswer studentAnswer : studentAnswers) {
                if (Result.CORRECT.getName().equals(studentAnswer.getResult())) {
                    count++;
                } else if (Result.PASS.getName().equals(studentAnswer.getResult())) {

                }
            }
            countSubmit = countSubmit < studentAnswers.size() ? studentAnswers.size() : countSubmit;
            model.setQuestionType(question.getTypeInCN());
            model.setTrueNum(count);
            if (QuestionType.OBJECTIVE.getName().equals(question.getType())) {
                countObjective++;
                objective.add(model);
            } else if (QuestionType.SUBJECTIVE.getName().equals(question.getType())) {
                countSubjective++;
                subjective.add(model);
            }
        }
        TeacherHomeResultModel model = new TeacherHomeResultModel();
        model.setSubjective(subjective);
        model.setObjective(objective);
        model.setObjectiveNum(countObjective);
        model.setSubjectiveNum(countSubjective);
        model.setSubmitNum(countSubmit);
        return model;
    }

    @Override
    public List<HomeWorkParentModel> getParentHomeWork(String classId, String studentId) throws FeignControllerException {
        if (StringUtils.isEmpty(classId) || StringUtils.isEmpty(studentId)) {
            throw new FeignControllerException(ResultCode.PARAM_MISS_MSG);
        }
        List<HomeWorkParentModel> homeWorkParentModels = new ArrayList<HomeWorkParentModel>();
        List<Homework> homeworks = homeworkRepository.findByClassIdAndEndTimeGreaterThan(classId, new Date().getTime());
        StudentHomeworkCollection collection = studentHomeworkCollectionRepository.findFirstByStudentId(studentId);
        if (null == collection) {
            throw new FeignControllerException(ResultCode.SELECT_NULL_MSG);
        }
        for (HomeworkSummary summary : collection.getHomeworks()) {
            for (Homework homework : homeworks) {
                if (summary.getHomeworkId().equals(homework.getId())) {
                    HomeWorkParentModel model = new HomeWorkParentModel();
                    model.setEndTime(homework.getEndTime());
                    model.setHomeWorkId(homework.getId());
                    model.setHomeWorkName(homework.getName());
                    model.setPulishTime(homework.getPublishTime());
                    model.setStudentId(studentId);
                    model.setSubjectName(homework.getSubjectName());
                    model.setStudentName(collection.getStudentName());
                    if (summary.getCompletedTime() > 0) {
                        model.setStatus("已提交");
                    } else {
                        model.setStatus("未提交");
                    }
                    homeWorkParentModels.add(model);
                }
            }
        }
        return homeWorkParentModels;
    }

    @Override
    public List<KnowledgeStatisticsModel> getParentStudentStistics(ParentStatisticsModel model) throws FeignControllerException {
        if (StringUtils.isEmpty(model.getClassId()) || StringUtils.isEmpty(model.getStudentId())
                || StringUtils.isEmpty(model.getSubjectName())) {
            throw new FeignControllerException(ResultCode.PARAM_MISS_MSG);
        }
        List<Homework> homeworks = new ArrayList<Homework>();
        if (model.getBeignTime() > 0) {
            homeworks.addAll(homeworkRepository.findByClassIdAndStatusAndEndTimeGreaterThan(model.getClassId(), Status.FINISH.getName(), model.getBeignTime()));
        } else {
            if (!(model.getStatisticsTimes().size() > 0)) {
                throw new FeignControllerException(ResultCode.PARAM_MISS_MSG);
            }
            for (Long beginTime : model.getStatisticsTimes()) {
                if (null == beginTime) {
                    continue;
                }
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(new Date(beginTime));
                calendar.set(Calendar.HOUR_OF_DAY, 23);
                calendar.set(Calendar.MINUTE, 59);
                calendar.set(Calendar.SECOND, 59);
                homeworks.addAll(homeworkRepository.findByClassIdAndStatusAndEndTimeGreaterThanAndEndTimeLessThan(model.getClassId(), Status.FINISH.getName(), beginTime, calendar.getTimeInMillis()));
            }
        }
        List<String> questionIds = new ArrayList<String>();
        for (Homework homework : homeworks) {
            questionIds.addAll(homework.getQuestionIds());
        }
        Map<String, Integer> selfTrue = new HashMap<String, Integer>();
        Map<String, Integer> classTrue = new HashMap<String, Integer>();
        for (String qid : questionIds) {
            int trueNum = 0;
            int selfNum = 0;
            List<StudentAnswer> answers = studentAnswerRepository.findAllByQuestionId(qid);
            for (StudentAnswer answer : answers) {
                if (Result.CORRECT.getName().equals(answer.getResult())) {
                    trueNum++;
                } else if (Result.PASS.getName().equals(answer.getResult())) {
                    //todo 主观题统计
                }
                if (model.getStudentId().equals(answer.getStudentId())) {
                    selfNum = 1;
                }
            }
            selfTrue.put(qid, selfNum);
            classTrue.put(qid, trueNum);
        }
        List<ResultKnowledgeModel> resultKnowledgeModels = getQuestionKnowledges(questionIds);
        Map<String, List<String>> questionMap = new HashMap<String, List<String>>();
        for (ResultKnowledgeModel knowledgeModel : resultKnowledgeModels) {
            for (Knowledge knowledge : knowledgeModel.getTransferKnowledge().getKnowledges()) {
                if (StringUtils.isEmpty(knowledge.getKnowledge())) {
                    knowledge.setKnowledge("综合分类");
                }
                List<String> qIds = new ArrayList<String>();
                if (questionMap.containsKey(knowledge.getKnowledge())) {
                    qIds.addAll(questionMap.get(knowledge.getKnowledge()));
                }
                questionIds.add(knowledgeModel.getQuestionId());
                questionMap.put(knowledge.getKnowledge(), qIds);
            }
        }
        int questionNum = 0;
        for (String key : questionMap.keySet()) {
            questionNum = questionNum + questionMap.get(key).size();
        }
        List<KnowledgeStatisticsModel> models = new ArrayList<KnowledgeStatisticsModel>();
        for (String key : questionMap.keySet()) {
            int trueNum = 0;
            int classNum = 0;
            for (String questionId : questionMap.get(key)) {
                trueNum = trueNum + selfTrue.get(questionId);
                classNum = classNum + classTrue.get(questionId);
            }
            KnowledgeStatisticsModel statisticsModel = new KnowledgeStatisticsModel();
            statisticsModel.setClassRightAvgRate(classNum / (model.getStudentNum() * questionMap.get(key).size()));
            statisticsModel.setKnowledgeName(key);
            if (0 != questionNum) {
                statisticsModel.setKnowledgeRate(questionMap.get(key).size() / questionNum);
            }
            statisticsModel.setStudentRightRate(trueNum / questionMap.get(key).size());
            models.add(statisticsModel);
        }
        return models;
    }

    private TransferKnowledge getKnowledgeOfParticularQuestion(String questionId) throws FeignControllerException {
        if (StringUtils.isEmpty(questionId)) throw new FeignControllerException(ResultCode.PARAM_MISS_MSG);
        Optional<Question> result = questionRepository.findById(questionId);
        if (result.isPresent()) {
            TransferKnowledge transferKnowledge = new TransferKnowledge();
            result.get().getKnowledges().forEach(knowledge -> {
                transferKnowledge.getKnowledges().add(
                        new Knowledge(knowledge.getKnowledgeId(), knowledge.getKnowledge(),
                                knowledge.getCapabilityId(), knowledge.getCapability()));
            });
            return transferKnowledge;
        }
        throw new FeignControllerException(ResultCode.QUESTION_NOT_EXISTED);
    }

    private List<ResultKnowledgeModel> getQuestionKnowledges(List<String> questionIds) throws FeignControllerException {
        if (null == questionIds) {
            throw new FeignControllerException(ResultCode.SELECT_NULL_MSG);
        }
        List<ResultKnowledgeModel> resultKnowledgeModels = new ArrayList<ResultKnowledgeModel>();
        for (String questionId : questionIds) {
            ResultKnowledgeModel resultKnowledgeModel = new ResultKnowledgeModel();
            resultKnowledgeModel.setQuestionId(questionId);
            resultKnowledgeModel.setTransferKnowledge(getKnowledgeOfParticularQuestion(questionId));
            resultKnowledgeModels.add(resultKnowledgeModel);
        }
        return resultKnowledgeModels;
    }
}
