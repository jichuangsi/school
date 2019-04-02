package com.jichuangsi.school.homeworkservice.feign.service.impl;

import com.jichuangsi.school.homeworkservice.constant.QuestionType;
import com.jichuangsi.school.homeworkservice.constant.Result;
import com.jichuangsi.school.homeworkservice.constant.ResultCode;
import com.jichuangsi.school.homeworkservice.constant.Status;
import com.jichuangsi.school.homeworkservice.entity.Homework;
import com.jichuangsi.school.homeworkservice.entity.Question;
import com.jichuangsi.school.homeworkservice.entity.StudentAnswer;
import com.jichuangsi.school.homeworkservice.exception.FeignControllerException;
import com.jichuangsi.school.homeworkservice.exception.StudentHomeworkServiceException;
import com.jichuangsi.school.homeworkservice.feign.model.HomeWorkRateModel;
import com.jichuangsi.school.homeworkservice.feign.model.QuestionRateModel;
import com.jichuangsi.school.homeworkservice.feign.model.QuestionResultModel;
import com.jichuangsi.school.homeworkservice.feign.model.TeacherHomeResultModel;
import com.jichuangsi.school.homeworkservice.feign.service.IFeignService;
import com.jichuangsi.school.homeworkservice.model.HomeworkModelForStudent;
import com.jichuangsi.school.homeworkservice.model.HomeworkModelForTeacher;
import com.jichuangsi.school.homeworkservice.repository.HomeworkRepository;
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
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

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

    @Override
    public double getStudentQuestionRate(QuestionRateModel model) throws FeignControllerException {
        List<String> questionIds = model.getQuestionIds();
        String studentId = model.getStudentId();
        if (null == questionIds || !(questionIds.size() > 0) || StringUtils.isEmpty(studentId)) {
            throw new FeignControllerException(ResultCode.PARAM_ERR_MSG);
        }
        List<StudentAnswer> studentAnswers = studentAnswerRepository.findByQuestionIdInAndStudentId(questionIds,studentId);
        if (!(studentAnswers.size() > 0)) {
            throw new FeignControllerException(ResultCode.HOMEWORK_NOT_EXISTED);
        }
        double count = 0;
        for (StudentAnswer studentAnswer : studentAnswers) {
            if (StringUtils.isEmpty(studentAnswer)){
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
        if(null == questionIds || !(questionIds.size() > 0)){
            throw new FeignControllerException(ResultCode.HOMEWORK_NOT_EXISTED);
        }
        List<StudentAnswer> studentAnswers = studentAnswerRepository.findByQuestionIdIn(questionIds);
        if (!(studentAnswers.size() > 0)) {
            throw new FeignControllerException(ResultCode.HOMEWORK_NOT_EXISTED);
        }
        double count = 0;
        for (StudentAnswer studentAnswer : studentAnswers) {
            if (StringUtils.isEmpty(studentAnswer)){
                continue;
            }
            if (Result.CORRECT.getName().equals(studentAnswer.getResult())) {
                count = count + 1 ;
            } else if (Result.PASS.getName().equals(studentAnswer.getResult())) {

            }
        }
        return count / studentAnswers.size();
    }

    @Override
    public List<HomeworkModelForTeacher> getHomeWorkByTeacherIdAndclassId(String teacherId, String classId) throws FeignControllerException {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.WEEK_OF_MONTH,-1);
        calendar.set(Calendar.HOUR_OF_DAY,0);
        calendar.set(Calendar.MINUTE,0);
        calendar.set(Calendar.SECOND,0);
        List<HomeworkModelForTeacher> homeworks = convertHomeworkList(mongoTemplate.find(
                new Query(
                        Criteria.where("teacherId").is(teacherId).and("status").is(Status.FINISH.getName()).
                                and("classId").is(classId).and("endTime").gte(calendar.getTimeInMillis())
                ).with(new Sort(Sort.Direction.DESC,"publishTime")),Homework.class));
        return homeworks;
    }

    private List<HomeworkModelForTeacher> convertHomeworkList(List<Homework> homeworks){
        List<HomeworkModelForTeacher> homeworkModelForTeachers = new ArrayList<HomeworkModelForTeacher>();
        homeworks.forEach(homework -> {
            homeworkModelForTeachers.add(MappingEntity2ModelConverter.ConvertTeacherHomework(homework));
        });
        return homeworkModelForTeachers;
    }

    @Override
    public TeacherHomeResultModel getHomeWorkRate(String teacherId, String homeId) throws FeignControllerException {
        if (StringUtils.isEmpty(teacherId) || StringUtils.isEmpty(homeId)){
            throw new FeignControllerException(ResultCode.PARAM_MISS_MSG);
        }
        Homework homework = homeworkRepository.findByTeacherIdAndId(teacherId,homeId);
        if (null == homeId){
            throw new FeignControllerException(ResultCode.SELECT_NULL_MSG);
        }
        int countSubjective = 0 ;
        int countObjective = 0 ;
        int countSubmit = 0 ;
        List<String> questionIds = homework.getQuestionIds();
        List<QuestionResultModel> objective = new ArrayList<QuestionResultModel>();
        List<QuestionResultModel> subjective = new ArrayList<QuestionResultModel>();
        for (String questionId : questionIds){
            Question question = questionRepository.findFirstById(questionId);
            if (null == question){
                continue;
            }
            QuestionResultModel model = new QuestionResultModel();
            List<StudentAnswer> studentAnswers = studentAnswerRepository.findAllByQuestionId(questionId);
            int count = 0 ;
            for (StudentAnswer studentAnswer : studentAnswers){
                if (Result.CORRECT.getName().equals(studentAnswer.getResult())){
                    count ++ ;
                }else if (Result.PASS.getName().equals(studentAnswer.getResult())){

                }
            }
            countSubmit = countSubmit < studentAnswers.size() ? studentAnswers.size() : countSubmit ;
            model.setQuestionType(question.getTypeInCN());
            model.setTrueNum(count);
            if (QuestionType.OBJECTIVE.getName().equals(question.getType())){
                countObjective ++ ;
                objective.add(model);
            }else if (QuestionType.SUBJECTIVE.getName().equals(question.getType())){
                countSubjective ++ ;
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
}
