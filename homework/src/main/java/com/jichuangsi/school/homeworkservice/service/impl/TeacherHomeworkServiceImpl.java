package com.jichuangsi.school.homeworkservice.service.impl;

import com.jichuangsi.microservice.common.model.UserInfoForToken;
import com.jichuangsi.school.homeworkservice.constant.Result;
import com.jichuangsi.school.homeworkservice.constant.ResultCode;
import com.jichuangsi.school.homeworkservice.constant.Status;
import com.jichuangsi.school.homeworkservice.entity.*;
import com.jichuangsi.school.homeworkservice.exception.TeacherHomeworkServiceException;
import com.jichuangsi.school.homeworkservice.model.*;
import com.jichuangsi.school.homeworkservice.model.common.PageHolder;
import com.jichuangsi.school.homeworkservice.repository.HomeworkRepository;
import com.jichuangsi.school.homeworkservice.repository.QuestionRepository;
import com.jichuangsi.school.homeworkservice.repository.StudentAnswerRepository;
import com.jichuangsi.school.homeworkservice.repository.TeacherAnswerRepository;
import com.jichuangsi.school.homeworkservice.service.IHomeworkConsoleService;
import com.jichuangsi.school.homeworkservice.service.ITeacherHomeworkService;
import com.jichuangsi.school.homeworkservice.service.IUserInfoService;
import com.jichuangsi.school.homeworkservice.utils.MappingEntity2ModelConverter;
import com.jichuangsi.school.homeworkservice.utils.MappingModel2EntityConverter;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class TeacherHomeworkServiceImpl implements ITeacherHomeworkService {

    @Resource
    private IUserInfoService userInfoService;

    @Resource
    private IHomeworkConsoleService homeworkConsoleService;

    @Resource
    private HomeworkRepository homeworkRepository;

    @Resource
    private QuestionRepository questionRepository;

    @Resource
    private StudentAnswerRepository studentAnswerRepository;

    @Resource
    private TeacherAnswerRepository teacherAnswerRepository;

    @Resource
    private MongoTemplate mongoTemplate;

    @Override
    public List<HomeworkModelForTeacher> getHomeworksList(UserInfoForToken userInfo) throws TeacherHomeworkServiceException{
        if(StringUtils.isEmpty(userInfo.getUserId())) throw new TeacherHomeworkServiceException(ResultCode.PARAM_MISS_MSG);
        List<HomeworkModelForTeacher> homeworks = convertHomeworkList(mongoTemplate.find(
                new Query(
                        Criteria.where("teacherId").is(userInfo.getUserId()).and("status").ne(Status.COMPLETED.getName()))
                        .with(new Sort(Sort.Direction.DESC,"publishTime")),Homework.class));
        countSubmittedHomework(homeworks);
        return homeworks;
    }

    @Override
    public PageHolder<HomeworkModelForTeacher> getHistoryHomeworksList(UserInfoForToken userInfo, SearchHomeworkModel searchHomeworkModel) throws TeacherHomeworkServiceException{
        if(StringUtils.isEmpty(userInfo.getUserId())) throw new TeacherHomeworkServiceException(ResultCode.PARAM_MISS_MSG);
        PageHolder<HomeworkModelForTeacher> pageHolder = new PageHolder<HomeworkModelForTeacher>();
        pageHolder.setTotal((int)
                mongoTemplate.count(
                        new Query(
                                Criteria.where("teacherId").is(userInfo.getUserId()).and("status").is(Status.COMPLETED.getName()))
                                .with(new Sort(Sort.Direction.DESC,"publishTime")),Homework.class)
        );
        pageHolder.setPageNum(searchHomeworkModel.getPageIndex());
        pageHolder.setPageSize(searchHomeworkModel.getPageSize());
        List<HomeworkModelForTeacher> homeworks = convertHomeworkList(mongoTemplate.find(
                new Query(
                        Criteria.where("teacherId").is(userInfo.getUserId()).and("status").is(Status.COMPLETED.getName()))
                        .with(new Sort(Sort.Direction.DESC,"publishTime"))
                        .skip((long)((searchHomeworkModel.getPageIndex()-1)*searchHomeworkModel.getPageSize()))
                        .limit(searchHomeworkModel.getPageSize()),Homework.class));
        countSubmittedHomework(homeworks);
        pageHolder.setContent(homeworks);
        return pageHolder;
    }

    @Override
    public HomeworkModelForTeacher getParticularHomework(UserInfoForToken userInfo, String homeworkId) throws TeacherHomeworkServiceException{
        if(StringUtils.isEmpty(userInfo.getUserId()) || StringUtils.isEmpty(homeworkId)) throw new TeacherHomeworkServiceException(ResultCode.PARAM_MISS_MSG);
        Homework homework = homeworkRepository.findFirstByIdOrderByUpdateTimeDesc(homeworkId);
        if(homework == null) throw new TeacherHomeworkServiceException(ResultCode.HOMEWORK_NOT_EXISTED);
        List<Question> questions = questionRepository.findQuestionsByHomeworkId(homeworkId);
        HomeworkModelForTeacher homeworkModelForTeacher = MappingEntity2ModelConverter.ConvertTeacherHomework(homework);
        List<QuestionModelForTeacher> questionModelForTeachers = new ArrayList<QuestionModelForTeacher>();
        questions.forEach(q ->{
            questionModelForTeachers.add(convertAndFetchAnswerForQuestion(userInfo.getUserId(), q));
        });
        homeworkModelForTeacher.getQuestions().addAll(questionModelForTeachers);
        homeworkModelForTeacher.getStudents().addAll(userInfoService.getStudentsForClassById(homework.getClassId()));
        return homeworkModelForTeacher;
    }

    @Override
    public QuestionModelForTeacher getParticularQuestion(UserInfoForToken userInfo, String questionId) throws TeacherHomeworkServiceException{
        if(StringUtils.isEmpty(userInfo.getUserId()) || StringUtils.isEmpty(questionId)) throw new TeacherHomeworkServiceException(ResultCode.PARAM_MISS_MSG);
        Optional<Question> result = questionRepository.findById(questionId);
        if(result.isPresent()){
            return convertAndFetchAnswerForQuestion(userInfo.getUserId(), result.get());
        }
        throw new TeacherHomeworkServiceException(ResultCode.QUESTION_NOT_EXISTED);
    }

    @Override
    public AnswerModelForStudent getParticularAnswer(String questionId, String studentId) throws TeacherHomeworkServiceException{
        if(StringUtils.isEmpty(questionId) || StringUtils.isEmpty(studentId)) throw new TeacherHomeworkServiceException(ResultCode.PARAM_MISS_MSG);
        StudentAnswer studentAnswer = studentAnswerRepository.findFirstByQuestionIdAndStudentIdOrderByUpdateTimeDesc(questionId, studentId);
        return MappingEntity2ModelConverter.ConvertStudentAnswer(studentAnswer);
    }

    @Override
    public void saveTeacherAnswer(UserInfoForToken userInfo, String questionId, String studentAnswerId, AnswerModelForTeacher revise) throws TeacherHomeworkServiceException{
        if(StringUtils.isEmpty(userInfo.getUserId())
                || StringUtils.isEmpty(questionId)
                || StringUtils.isEmpty(studentAnswerId)
                || StringUtils.isEmpty(revise.getStubForSubjective())) throw new TeacherHomeworkServiceException(ResultCode.PARAM_MISS_MSG);
        synchronized (questionId.intern()){//需要实现分布式锁
            Optional<TeacherAnswer> resultForTeacherAnswer = Optional.ofNullable(teacherAnswerRepository.findFirstByTeacherIdAndQuestionIdAndStudentAnswerIdOrderByUpdateTimeDesc(userInfo.getUserId(), questionId, studentAnswerId));
            if(resultForTeacherAnswer.isPresent()){
                TeacherAnswer answer2Update = resultForTeacherAnswer.get();
                answer2Update.setSubjectivePic(revise.getPicForSubjective());
                answer2Update.setSubjectivePicStub(revise.getStubForSubjective());
                answer2Update.setSubjectiveScore(revise.getScore());
                answer2Update.setUpdateTime(new Date().getTime());
                teacherAnswerRepository.save(answer2Update);
            }else{
                teacherAnswerRepository.save(MappingModel2EntityConverter.ConvertTeacherAnswer(userInfo, questionId, studentAnswerId, revise));
            }

            Optional<StudentAnswer> resultForStudentAnswer = studentAnswerRepository.findById(studentAnswerId);
            if(resultForStudentAnswer.isPresent()){
                StudentAnswer answer2Update = resultForStudentAnswer.get();
                answer2Update.setResult(Result.PASS.getName());
                answer2Update.setSubjectiveScore(revise.getScore());
                answer2Update.setReviseTime(new Date().getTime());
                studentAnswerRepository.save(answer2Update);
            }

            //Course course = courseRepository.findCourseByTeacherIdAndQuestionId(userInfo.getUserId(), questionId);
            //mqService.sendMsg4ShareAnswer(MappingEntity2MessageConverter.ConvertShareAnswer(course==null?"":course.getId(), questionId, revise.getStubForSubjective()));
        }
    }

    @Override
    public void updateParticularHomeworkStatus(UserInfoForToken userInfo, HomeworkModelForTeacher homeworkModelForTeacher) throws TeacherHomeworkServiceException{
        homeworkConsoleService.updateHomeWork2NewStatus(userInfo, homeworkModelForTeacher);
    }

    private void countSubmittedHomework(List<HomeworkModelForTeacher> homeworks){
        homeworks.forEach(h -> {
            h.setSubmitted((int)mongoTemplate.count(new Query(
                    Criteria.where("homeworks").elemMatch(Criteria.where("homeworkId").is(h.getHomeworkId())
                            .and("completedTime").ne(0))), StudentHomeworkCollection.class));
            h.getStudents().addAll(userInfoService.getStudentsForClassById(h.getClassId()));
        });
    }

    private QuestionModelForTeacher convertAndFetchAnswerForQuestion(String teacherId, Question question){
        List<StudentAnswer> answers = studentAnswerRepository.findAllByQuestionId(question.getId());
        QuestionModelForTeacher questionForTeacher =  MappingEntity2ModelConverter.ConvertTeacherQuestion(question);
        questionForTeacher.getAnswerModelForStudent().addAll(convertStudentAnswerList(answers));
        questionForTeacher.getAnswerModelForStudent().forEach(a -> {
            TeacherAnswer teacherAnswer = teacherAnswerRepository.findFirstByTeacherIdAndQuestionIdAndStudentAnswerIdOrderByUpdateTimeDesc(teacherId, question.getId(), a.getAnswerId());
            if(teacherAnswer!=null) a.setReviseForSubjective(teacherAnswer.getSubjectivePicStub());
        });
        return questionForTeacher;
    }

    private List<HomeworkModelForTeacher> convertHomeworkList(List<Homework> homeworks){
        List<HomeworkModelForTeacher> homeworkModelForTeachers = new ArrayList<HomeworkModelForTeacher>();
        homeworks.forEach(homework -> {
            homeworkModelForTeachers.add(MappingEntity2ModelConverter.ConvertTeacherHomework(homework));
        });
        return homeworkModelForTeachers;
    }

    private List<QuestionModelForTeacher> convertQuestionList(List<Question> questions){
        List<QuestionModelForTeacher> questionModelForTeachers = new ArrayList<QuestionModelForTeacher>();
        questions.forEach(question -> {
            questionModelForTeachers.add(MappingEntity2ModelConverter.ConvertTeacherQuestion(question));
        });
        return questionModelForTeachers;
    }

    private List<AnswerModelForStudent> convertStudentAnswerList(List<StudentAnswer> answers){
        List<AnswerModelForStudent> AnswerForStudents = new ArrayList<AnswerModelForStudent>();
        answers.forEach(answer -> {
            AnswerForStudents.add(MappingEntity2ModelConverter.ConvertStudentAnswer(answer));
        });
        return AnswerForStudents;
    }
}
