package com.jichuangsi.school.homeworkservice.service.impl;

import com.jichuangsi.microservice.common.model.UserInfoForToken;
import com.jichuangsi.school.homeworkservice.constant.QuestionType;
import com.jichuangsi.school.homeworkservice.constant.Result;
import com.jichuangsi.school.homeworkservice.constant.ResultCode;
import com.jichuangsi.school.homeworkservice.constant.Status;
import com.jichuangsi.school.homeworkservice.entity.*;
import com.jichuangsi.school.homeworkservice.exception.StudentHomeworkServiceException;
import com.jichuangsi.school.homeworkservice.model.AnswerModelForStudent;
import com.jichuangsi.school.homeworkservice.model.HomeworkModelForStudent;
import com.jichuangsi.school.homeworkservice.model.QuestionModelForStudent;
import com.jichuangsi.school.homeworkservice.model.SearchHomeworkModel;
import com.jichuangsi.school.homeworkservice.model.common.PageHolder;
import com.jichuangsi.school.homeworkservice.repository.HomeworkRepository;
import com.jichuangsi.school.homeworkservice.repository.QuestionRepository;
import com.jichuangsi.school.homeworkservice.repository.StudentAnswerRepository;
import com.jichuangsi.school.homeworkservice.repository.TeacherAnswerRepository;
import com.jichuangsi.school.homeworkservice.service.IAutoVerifyAnswerService;
import com.jichuangsi.school.homeworkservice.service.IStudentHomeworkService;
import com.jichuangsi.school.homeworkservice.utils.MappingEntity2ModelConverter;
import com.jichuangsi.school.homeworkservice.utils.MappingModel2EntityConverter;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.*;

@Service
public class StudentHomeworkServiceImpl implements IStudentHomeworkService {

    @Resource
    private IAutoVerifyAnswerService autoVerifyAnswerService;

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
    public List<HomeworkModelForStudent> getHomeworksList(UserInfoForToken userInfo) throws StudentHomeworkServiceException{
        if(StringUtils.isEmpty(userInfo.getUserId())) throw new StudentHomeworkServiceException(ResultCode.PARAM_MISS_MSG);
        List<HomeworkModelForStudent> homeworks = convertHomeworkList(homeworkRepository.findProgressHomeworkByStudentId(userInfo.getUserId()));
        checkHomeworkCompleted(userInfo, homeworks);
        return homeworks;
    }

    @Override
    public PageHolder<HomeworkModelForStudent> getHistoryHomeworksList(UserInfoForToken userInfo, SearchHomeworkModel searchHomeworkModel) throws StudentHomeworkServiceException{
        if(StringUtils.isEmpty(userInfo.getUserId())) throw new StudentHomeworkServiceException(ResultCode.PARAM_MISS_MSG);
        PageHolder<HomeworkModelForStudent> pageHolder = new PageHolder<HomeworkModelForStudent>();
        pageHolder.setTotal(homeworkRepository.countFinishedHomeworkByStudentId(userInfo.getUserId()));
        pageHolder.setPageNum(searchHomeworkModel.getPageIndex());
        pageHolder.setPageSize(searchHomeworkModel.getPageSize());
        List<HomeworkModelForStudent> homeworks = convertHomeworkList(homeworkRepository.findFinishedHomeworkByStudentId(userInfo.getUserId(), searchHomeworkModel.getPageIndex(), searchHomeworkModel.getPageSize()));
        checkHomeworkCompleted(userInfo, homeworks);
        pageHolder.setContent(homeworks);
        return pageHolder;
    }

    @Override
    public HomeworkModelForStudent getParticularHomework(UserInfoForToken userInfo, String homeworkId) throws StudentHomeworkServiceException{
        if(StringUtils.isEmpty(homeworkId)) throw new StudentHomeworkServiceException(ResultCode.PARAM_MISS_MSG);
        Homework homework = homeworkRepository.findFirstByIdOrderByUpdateTimeDesc(homeworkId);
        if(homework==null) throw new StudentHomeworkServiceException(ResultCode.HOMEWORK_NOT_EXISTED);
        List<Question> questions = questionRepository.findQuestionsByHomeworkId(homeworkId);
        HomeworkModelForStudent homeworkModelForStudent = MappingEntity2ModelConverter.ConvertStudentHomework(homework);
        homeworkModelForStudent.getQuestions().addAll(convertQuestionList(questions));
        homeworkModelForStudent.getQuestions().forEach(question ->{
            question.setFavor(mongoTemplate.exists(new Query(Criteria.where("questionIds").is(question.getQuestionId())), StudentFavorQuestion.class));
            Optional<StudentAnswer> result = Optional.ofNullable(studentAnswerRepository.findFirstByQuestionIdAndStudentIdOrderByUpdateTimeDesc(question.getQuestionId(), userInfo.getUserId()));
            if(result.isPresent()){
                question.setAnswerModelForStudent(MappingEntity2ModelConverter.ConvertStudentAnswer(result.get()));
                if(QuestionType.SUBJECTIVE.getName().equalsIgnoreCase(question.getQuestionType())){
                    if(Result.PASS.getName().equalsIgnoreCase(result.get().getResult())){
                        question.setAnswerModelForTeacher(MappingEntity2ModelConverter.ConvertTeacherAnswer(
                                teacherAnswerRepository.findFirstByTeacherIdAndQuestionIdAndStudentAnswerIdOrderByUpdateTimeDesc(homework.getTeacherId(), question.getQuestionId(), result.get().getId())
                        ));
                    }
                }
            }
            /*if(question.getAnswerModelForTeacher()==null){
                TeacherAnswer shareAnswer = teacherAnswerRepository.findFirstByTeacherIdAndQuestionIdAndIsShareOrderByShareTimeDesc(homework.getTeacherId(), question.getQuestionId(), true);
                if(shareAnswer !=null) question.setAnswerModelForTeacher(MappingEntity2ModelConverter.ConvertTeacherAnswer(shareAnswer));
            }*/
        });
        checkHomeworkCompleted(userInfo, Arrays.asList(homeworkModelForStudent));
        return homeworkModelForStudent;
    }

    @Override
    public void saveStudentAnswer(UserInfoForToken userInfo, String homeworkId, String questionId, AnswerModelForStudent answer) throws StudentHomeworkServiceException {
        if(StringUtils.isEmpty(userInfo.getUserId())
                || StringUtils.isEmpty(homeworkId)
                || StringUtils.isEmpty(questionId)
                || (StringUtils.isEmpty(answer.getAnswerForObjective()) && StringUtils.isEmpty(answer.getStubForSubjective())))
            throw new StudentHomeworkServiceException(ResultCode.PARAM_MISS_MSG);
        String userId = userInfo.getUserId();
        synchronized (userId.intern()){//需要实现分布式锁
            Homework homework = homeworkRepository.findFirstByIdOrderByUpdateTimeDesc(homeworkId);
            if(Status.NOTSTART.getName().equalsIgnoreCase(homework.getStatus())) throw new StudentHomeworkServiceException(ResultCode.HOMEWORK_NOTSTART);
            if(Status.FINISH.getName().equalsIgnoreCase(homework.getStatus())
                    ||Status.COMPLETED.getName().equalsIgnoreCase(homework.getStatus())) throw new StudentHomeworkServiceException(ResultCode.HOMEWORK_FINISHED);
            Optional<Question> question = questionRepository.findById(questionId);
            if(!question.isPresent()) throw new StudentHomeworkServiceException(ResultCode.QUESTION_NOT_EXISTED);
            if(!StringUtils.isEmpty(answer.getAnswerForObjective())){
                answer.setResult(autoVerifyAnswerService.verifyObjectiveAnswer(question.get(), answer.getAnswerForObjective()));
            }
            Optional<StudentAnswer> result = Optional.ofNullable(studentAnswerRepository.findFirstByQuestionIdAndStudentIdOrderByUpdateTimeDesc(questionId, userId));
            if(result.isPresent()){
                StudentAnswer answer2Update = result.get();
                //answer2Update.setSubjectivePic(answer.getReviseForSubjective());
                answer2Update.setSubjectivePicStub(answer.getStubForSubjective());
                answer2Update.setObjectiveAnswer(answer.getAnswerForObjective());
                answer2Update.setResult(StringUtils.isEmpty(answer.getResult())?null:answer.getResult().getName());
                answer2Update.setUpdateTime(new Date().getTime());
                studentAnswerRepository.save(answer2Update);
            }else{
                studentAnswerRepository.save(MappingModel2EntityConverter.ConvertStudentAnswer(userInfo, questionId, answer));
            }
            mongoTemplate.updateFirst(
                    new Query(Criteria.where("studentId").is(userInfo.getUserId()).and("homeworks").elemMatch(Criteria.where("homeworkId").is(homeworkId))),
                    new Update().set("homeworks.$.updateTime", new Date().getTime()), StudentHomeworkCollection.class);
        }
    }

    @Override
    public void submitParticularHomework(UserInfoForToken userInfo, String homeworkId) throws StudentHomeworkServiceException{
        if(StringUtils.isEmpty(userInfo.getUserId())
                || StringUtils.isEmpty(homeworkId))
            throw new StudentHomeworkServiceException(ResultCode.PARAM_MISS_MSG);
        String userId = userInfo.getUserId();
        synchronized (userId.intern()){//需要实现分布式锁
            Homework homework = homeworkRepository.findFirstByIdOrderByUpdateTimeDesc(homeworkId);
            if(Status.NOTSTART.getName().equalsIgnoreCase(homework.getStatus())) throw new StudentHomeworkServiceException(ResultCode.HOMEWORK_NOTSTART);
            if(Status.FINISH.getName().equalsIgnoreCase(homework.getStatus())
                    ||Status.COMPLETED.getName().equalsIgnoreCase(homework.getStatus())) throw new StudentHomeworkServiceException(ResultCode.HOMEWORK_FINISHED);
            mongoTemplate.updateFirst(
                    new Query(Criteria.where("studentId").is(userInfo.getUserId()).and("homeworks").elemMatch(Criteria.where("homeworkId").is(homeworkId))),
                    new Update().set("homeworks.$.completedTime", new Date().getTime()), StudentHomeworkCollection.class);
        }
    }

    private void checkHomeworkCompleted(UserInfoForToken userInfo, List<HomeworkModelForStudent> homeworks){
        homeworks.forEach(h -> {
            h.setCompleted(mongoTemplate.exists(
                    new Query(Criteria.where("studentId").is(userInfo.getUserId())
                            .and("homeworks").elemMatch(Criteria.where("homeworkId").is(h.getHomeworkId()).and("completedTime").ne(0))),
                    StudentHomeworkCollection.class));
        });
    }

    private List<HomeworkModelForStudent> convertHomeworkList(List<Homework> homeworks){
        List<HomeworkModelForStudent> homeworkModelForStudents = new ArrayList<HomeworkModelForStudent>();
        homeworks.forEach(homework -> {
            homeworkModelForStudents.add(MappingEntity2ModelConverter.ConvertStudentHomework(homework));
        });
        return homeworkModelForStudents;
    }

    private List<QuestionModelForStudent> convertQuestionList(List<Question> questions){
        List<QuestionModelForStudent> questionForStudents = new ArrayList<QuestionModelForStudent>();
        questions.forEach(question -> {
            questionForStudents.add(MappingEntity2ModelConverter.ConvertStudentQuestion(question));
        });
        return questionForStudents;
    }
}
