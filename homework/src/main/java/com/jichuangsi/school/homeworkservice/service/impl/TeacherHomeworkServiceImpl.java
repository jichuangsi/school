package com.jichuangsi.school.homeworkservice.service.impl;

import com.jichuangsi.microservice.common.model.UserInfoForToken;
import com.jichuangsi.school.homeworkservice.constant.QuestionType;
import com.jichuangsi.school.homeworkservice.constant.Result;
import com.jichuangsi.school.homeworkservice.constant.ResultCode;
import com.jichuangsi.school.homeworkservice.constant.Status;
import com.jichuangsi.school.homeworkservice.entity.*;
import com.jichuangsi.school.homeworkservice.exception.TeacherHomeworkServiceException;
import com.jichuangsi.school.homeworkservice.model.*;
import com.jichuangsi.school.homeworkservice.model.common.PageHolder;
import com.jichuangsi.school.homeworkservice.model.transfer.TransferStudent;
import com.jichuangsi.school.homeworkservice.repository.HomeworkRepository;
import com.jichuangsi.school.homeworkservice.repository.QuestionRepository;
import com.jichuangsi.school.homeworkservice.repository.StudentAnswerRepository;
import com.jichuangsi.school.homeworkservice.repository.TeacherAnswerRepository;
import com.jichuangsi.school.homeworkservice.service.IHomeworkConsoleService;
import com.jichuangsi.school.homeworkservice.service.ITeacherHomeworkService;
import com.jichuangsi.school.homeworkservice.service.IUserInfoService;
import com.jichuangsi.school.homeworkservice.utils.MappingEntity2ModelConverter;
import com.jichuangsi.school.homeworkservice.utils.MappingModel2EntityConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

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

    @Value("${com.jichuangsi.school.result.page-size}")
    private int defaultPageSize;

    @Override
    public List<HomeworkModelForTeacher> getHomeworksList(UserInfoForToken userInfo) throws TeacherHomeworkServiceException{
        if(StringUtils.isEmpty(userInfo.getUserId())) throw new TeacherHomeworkServiceException(ResultCode.PARAM_MISS_MSG);
        List<HomeworkModelForTeacher> homeworks = convertHomeworkList(mongoTemplate.find(
                new Query(
                        Criteria.where("teacherId").is(userInfo.getUserId()).and("status").ne(Status.COMPLETED.getName()))
                        .with(new Sort(Sort.Direction.DESC,"publishTime")),Homework.class));
        countSubmittedAndTotalHomework(homeworks);
        return homeworks;
    }

    @Override
    public PageHolder<HomeworkModelForTeacher> getHistoryHomeworksList(UserInfoForToken userInfo, SearchHomeworkModel searchHomeworkModel) throws TeacherHomeworkServiceException{
        if(StringUtils.isEmpty(userInfo.getUserId())) throw new TeacherHomeworkServiceException(ResultCode.PARAM_MISS_MSG);
        if(searchHomeworkModel.getPageSize() == 0) searchHomeworkModel.setPageSize(defaultPageSize);
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
        countSubmittedAndTotalHomework(homeworks);
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
        List<StudentHomeworkCollection> studentHomeworks = mongoTemplate.find(new Query(
                Criteria.where("homeworks").elemMatch(Criteria.where("homeworkId")
                        .is(homeworkModelForTeacher.getHomeworkId())))
                , StudentHomeworkCollection.class);
        List<TransferStudent> students = new ArrayList<TransferStudent>();
        studentHomeworks.forEach(s -> {
            Optional<HomeworkSummary> homeworkSummary = s.getHomeworks().stream().filter(h->h.getHomeworkId().equalsIgnoreCase(homeworkId)).findFirst();
            TransferStudent ts = new TransferStudent(s.getStudentId(), s.getStudentAccount(), s.getStudentName());
            ts.setTotalScore(homeworkSummary.isPresent()?homeworkSummary.get().getTotalScore():0d);
            ts.setCompletedTime(homeworkSummary.isPresent()?homeworkSummary.get().getCompletedTime():0);
            students.add(ts);
        });
        homeworkModelForTeacher.getStudents().addAll(students.stream().sorted(Comparator.comparing(TransferStudent::getCompletedTime).reversed()).collect(Collectors.toList()));
        return homeworkModelForTeacher;
    }

    @Override
    public HomeworkModelForStudent getParticularStudentHomework(UserInfoForToken userInfo, String homeworkId, String studentId) throws TeacherHomeworkServiceException{
        if(StringUtils.isEmpty(userInfo.getUserId()) || StringUtils.isEmpty(homeworkId)  || StringUtils.isEmpty(studentId)) throw new TeacherHomeworkServiceException(ResultCode.PARAM_MISS_MSG);
        Homework homework = homeworkRepository.findFirstByIdOrderByUpdateTimeDesc(homeworkId);
        if (homework == null) throw new TeacherHomeworkServiceException(ResultCode.HOMEWORK_NOT_EXISTED);
        List<Question> questions = questionRepository.findQuestionsByHomeworkId(homeworkId);
        HomeworkModelForStudent homeworkModelForStudent = MappingEntity2ModelConverter.ConvertStudentHomework(homework);
        homeworkModelForStudent.getQuestions().addAll(convertStudentQuestionList(questions));
        homeworkModelForStudent.getQuestions().forEach(question -> {
            Optional<StudentAnswer> result = Optional.ofNullable(studentAnswerRepository.findFirstByQuestionIdAndStudentIdOrderByUpdateTimeDesc(question.getQuestionId(), studentId));
            if (result.isPresent()) {
                question.setAnswerModelForStudent(MappingEntity2ModelConverter.ConvertStudentAnswer(result.get()));
                if (QuestionType.SUBJECTIVE.getName().equalsIgnoreCase(question.getQuestionType())) {
                    if (Result.PASS.getName().equalsIgnoreCase(result.get().getResult())) {
                        question.setAnswerModelForTeacher(MappingEntity2ModelConverter.ConvertTeacherAnswer(
                                teacherAnswerRepository.findFirstByTeacherIdAndQuestionIdAndStudentAnswerIdOrderByUpdateTimeDesc(userInfo.getUserId(), question.getQuestionId(), result.get().getId())
                        ));
                    }
                }
            }
        });

        this.checkHomeworkCompleted(studentId, homeworkModelForStudent);
        this.getHomeworkTotalScore(studentId, homeworkModelForStudent);
        return homeworkModelForStudent;
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

    private void countSubmittedAndTotalHomework(List<HomeworkModelForTeacher> homeworks){
        homeworks.forEach(h -> {
            h.setSubmitted((int)mongoTemplate.count(new Query(
                    Criteria.where("homeworks").elemMatch(Criteria.where("homeworkId").is(h.getHomeworkId())
                            .and("completedTime").ne(0))), StudentHomeworkCollection.class));
            h.setTotal((int)mongoTemplate.count(new Query(
                    Criteria.where("homeworks").elemMatch(Criteria.where("homeworkId").is(h.getHomeworkId()))), StudentHomeworkCollection.class));
        });
    }

    @Override
    public List<TransferStudent> settleParticularHomework(UserInfoForToken userInfo, String homeworkId) throws TeacherHomeworkServiceException{
        if(StringUtils.isEmpty(userInfo.getUserId()) || StringUtils.isEmpty(homeworkId)) throw new TeacherHomeworkServiceException(ResultCode.PARAM_MISS_MSG);
        Homework homework = homeworkRepository.findFirstByIdOrderByUpdateTimeDesc(homeworkId);
        if(homework == null) throw new TeacherHomeworkServiceException(ResultCode.HOMEWORK_NOT_EXISTED);
        if (Status.NOTSTART.getName().equalsIgnoreCase(homework.getStatus()))
            throw new TeacherHomeworkServiceException(ResultCode.HOMEWORK_NOTSTART);
        if (Status.PROGRESS.getName().equalsIgnoreCase(homework.getStatus()))
            throw new TeacherHomeworkServiceException(ResultCode.SETTLE_HOMEWORK_WITH_PROGRESS);
        List<Question> questions = questionRepository.findQuestionsByHomeworkId(homeworkId);
        List<StudentHomeworkCollection> studentHomeworks = mongoTemplate.find(
                new Query(Criteria.where("homeworks").elemMatch(Criteria.where("homeworkId").is(homeworkId))), StudentHomeworkCollection.class);
        List<TransferStudent> students = new ArrayList<TransferStudent>();

        studentHomeworks.forEach(s -> {
            Optional<HomeworkSummary> homeworkSummary = s.getHomeworks().stream().filter(h->h.getHomeworkId().equalsIgnoreCase(homeworkId)).findFirst();
            TransferStudent ts = new TransferStudent(s.getStudentId(), s.getStudentAccount(), s.getStudentName());
            double totalScore = this.settleHomework4ParticularStudent(s.getStudentId(), questions);
            mongoTemplate.updateFirst(new Query(Criteria.where("studentId").is(s.getStudentId()).and("homeworks").elemMatch(Criteria.where("homeworkId").is(homeworkId)))
                    , new Update().set("homeworks.$.totalScore",totalScore),StudentHomeworkCollection.class);
            ts.setTotalScore(totalScore);
            ts.setCompletedTime(homeworkSummary.isPresent()?homeworkSummary.get().getCompletedTime():0);
            students.add(ts);
        });

        students.stream().sorted(Comparator.comparing(TransferStudent::getCompletedTime).reversed()).collect(Collectors.toList());
        return students;

    }

    private double settleHomework4ParticularStudent(String studentId, List<Question> questions){
        double totalScore = 0d;
        for ( Question q : questions ){
            StudentAnswer studentAnswer = studentAnswerRepository.findFirstByQuestionIdAndStudentIdOrderByUpdateTimeDesc(q.getId(), studentId);
            if(studentAnswer!=null){
                if (QuestionType.OBJECTIVE.getName().equalsIgnoreCase(q.getType())){//累加客观题
                    if(Result.CORRECT.getName().equalsIgnoreCase(studentAnswer.getResult()) && !StringUtils.isEmpty(q.getPoint())){//学生回答正确并且有题目设置分数
                        totalScore += Double.valueOf(q.getPoint());
                    }
                }else if(QuestionType.SUBJECTIVE.getName().equalsIgnoreCase(q.getType())){
                    if(Result.PASS.getName().equalsIgnoreCase(studentAnswer.getResult())
                            && !StringUtils.isEmpty(studentAnswer.getSubjectiveScore())
                            && !StringUtils.isEmpty(q.getPoint())){//老师批改并且有登记分数，和题目设置分数
                        totalScore += studentAnswer.getSubjectiveScore();
                    }
                }
            }
        }
        return totalScore;
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

    private List<QuestionModelForTeacher> convertTeacherQuestionList(List<Question> questions){
        List<QuestionModelForTeacher> questionModelForTeachers = new ArrayList<QuestionModelForTeacher>();
        questions.forEach(question -> {
            questionModelForTeachers.add(MappingEntity2ModelConverter.ConvertTeacherQuestion(question));
        });
        return questionModelForTeachers;
    }

    private List<QuestionModelForStudent> convertStudentQuestionList(List<Question> questions) {
        List<QuestionModelForStudent> questionForStudents = new ArrayList<QuestionModelForStudent>();
        questions.forEach(question -> {
            questionForStudents.add(MappingEntity2ModelConverter.ConvertStudentQuestion(question));
        });
        return questionForStudents;
    }

    private List<AnswerModelForStudent> convertStudentAnswerList(List<StudentAnswer> answers){
        List<AnswerModelForStudent> AnswerForStudents = new ArrayList<AnswerModelForStudent>();
        answers.forEach(answer -> {
            AnswerForStudents.add(MappingEntity2ModelConverter.ConvertStudentAnswer(answer));
        });
        return AnswerForStudents;
    }

    private void checkHomeworkCompleted(String studentId, HomeworkModelForStudent homeworkModelForStudent) {
        homeworkModelForStudent.setCompleted(mongoTemplate.exists(
                    new Query(Criteria.where("studentId").is(studentId)
                            .and("homeworks").elemMatch(Criteria.where("homeworkId").is(homeworkModelForStudent.getHomeworkId()).and("completedTime").ne(0))),
                    StudentHomeworkCollection.class));
    }

    private void getHomeworkTotalScore(String studentId, HomeworkModelForStudent homeworkModelForStudent){
        StudentHomeworkCollection s = mongoTemplate.findOne(
                new Query(
                        Criteria.where("studentId").is(studentId)
                                .and("homeworks").elemMatch(Criteria.where("homeworkId").is(homeworkModelForStudent.getHomeworkId()))
                        ), StudentHomeworkCollection.class);
        if(s!=null) homeworkModelForStudent.setTotalScore(s.getHomeworks().stream().filter(
                h->h.getHomeworkId().equalsIgnoreCase(homeworkModelForStudent.getHomeworkId())).findFirst().get().getTotalScore());
    }
}
