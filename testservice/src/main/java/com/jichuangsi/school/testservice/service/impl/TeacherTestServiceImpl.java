package com.jichuangsi.school.testservice.service.impl;

import com.jichuangsi.microservice.common.model.UserInfoForToken;
import com.jichuangsi.school.testservice.constant.Result;
import com.jichuangsi.school.testservice.constant.ResultCode;
import com.jichuangsi.school.testservice.constant.Status;
import com.jichuangsi.school.testservice.entity.*;
import com.jichuangsi.school.testservice.exception.TeacherTestServiceException;
import com.jichuangsi.school.testservice.model.*;
import com.jichuangsi.school.testservice.model.common.PageHolder;
import com.jichuangsi.school.testservice.model.transfer.TransferStudent;
import com.jichuangsi.school.testservice.repository.TestRepository;
import com.jichuangsi.school.testservice.repository.QuestionRepository;
import com.jichuangsi.school.testservice.repository.StudentAnswerRepository;
import com.jichuangsi.school.testservice.repository.TeacherAnswerRepository;
import com.jichuangsi.school.testservice.service.ITestConsoleService;
import com.jichuangsi.school.testservice.service.ITeacherTestService;
import com.jichuangsi.school.testservice.service.IUserInfoService;
import com.jichuangsi.school.testservice.utils.MappingEntity2ModelConverter;
import com.jichuangsi.school.testservice.utils.MappingModel2EntityConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class TeacherTestServiceImpl implements ITeacherTestService {

    @Resource
    private IUserInfoService userInfoService;

    @Resource
    private ITestConsoleService testConsoleService;

    @Resource
    private TestRepository testRepository;

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
    public List<TestModelForTeacher> getTestsList(UserInfoForToken userInfo) throws TeacherTestServiceException {
        if(StringUtils.isEmpty(userInfo.getUserId())) throw new TeacherTestServiceException(ResultCode.PARAM_MISS_MSG);
        List<TestModelForTeacher> tests = convertTestList(mongoTemplate.find(
                new Query(
                        Criteria.where("teacherId").is(userInfo.getUserId()).and("status").ne(Status.COMPLETED.getName()))
                        .with(new Sort(Sort.Direction.DESC,"publishTime")),Test.class));
        countSubmittedAndTotalTest(tests);
        return tests;
    }

    @Override
    public PageHolder<TestModelForTeacher> getHistoryTestsList(UserInfoForToken userInfo, SearchTestModel searchTestModel) throws TeacherTestServiceException {
        if(StringUtils.isEmpty(userInfo.getUserId())) throw new TeacherTestServiceException(ResultCode.PARAM_MISS_MSG);
        if(searchTestModel.getPageSize() == 0) searchTestModel.setPageSize(defaultPageSize);
        PageHolder<TestModelForTeacher> pageHolder = new PageHolder<TestModelForTeacher>();
        pageHolder.setTotal((int)
                mongoTemplate.count(
                        new Query(
                                Criteria.where("teacherId").is(userInfo.getUserId()).and("status").is(Status.COMPLETED.getName()))
                                .with(new Sort(Sort.Direction.DESC,"publishTime")),Test.class)
        );
        pageHolder.setPageNum(searchTestModel.getPageIndex());
        pageHolder.setPageSize(searchTestModel.getPageSize());
        List<TestModelForTeacher> tests = convertTestList(mongoTemplate.find(
                new Query(
                        Criteria.where("teacherId").is(userInfo.getUserId()).and("status").is(Status.COMPLETED.getName()))
                        .with(new Sort(Sort.Direction.DESC,"publishTime"))
                        .skip((long)((searchTestModel.getPageIndex()-1)*searchTestModel.getPageSize()))
                        .limit(searchTestModel.getPageSize()),Test.class));
        countSubmittedAndTotalTest(tests);
        pageHolder.setContent(tests);
        return pageHolder;
    }

    @Override
    public TestModelForTeacher getParticularTest(UserInfoForToken userInfo, String testId) throws TeacherTestServiceException {
        if(StringUtils.isEmpty(userInfo.getUserId()) || StringUtils.isEmpty(testId)) throw new TeacherTestServiceException(ResultCode.PARAM_MISS_MSG);
        Test test = testRepository.findFirstByIdOrderByUpdateTimeDesc(testId);
        if(test == null) throw new TeacherTestServiceException(ResultCode.TEST_NOT_EXISTED);
        List<Question> questions = questionRepository.findQuestionsByTestId(testId);
        TestModelForTeacher testModelForTeacher = MappingEntity2ModelConverter.ConvertTeacherTest(test);
        List<QuestionModelForTeacher> questionModelForTeachers = new ArrayList<QuestionModelForTeacher>();
        questions.forEach(q ->{
            questionModelForTeachers.add(convertAndFetchAnswerForQuestion(userInfo.getUserId(), q));
        });
        testModelForTeacher.getQuestions().addAll(questionModelForTeachers);
        List<StudentTestCollection> studentHomwokrs = mongoTemplate.find(new Query(
                Criteria.where("tests").elemMatch(Criteria.where("testId")
                        .is(testModelForTeacher.getTestId())))
                , StudentTestCollection.class);
        List<TransferStudent> students = new ArrayList<TransferStudent>();
        studentHomwokrs.forEach(s -> {
            Optional<TestSummary> testSummary = s.getTests().stream().filter(h->h.getTestId().equalsIgnoreCase(testId)).findFirst();
            TransferStudent ts = new TransferStudent(s.getStudentId(), s.getStudentAccount(), s.getStudentName());
            ts.setCompletedTime(testSummary.isPresent()?testSummary.get().getCompletedTime():0);
            students.add(ts);
        });
        testModelForTeacher.getStudents().addAll(students.stream().sorted(Comparator.comparing(TransferStudent::getCompletedTime).reversed()).collect(Collectors.toList()));
        return testModelForTeacher;
    }

    @Override
    public QuestionModelForTeacher getParticularQuestion(UserInfoForToken userInfo, String questionId) throws TeacherTestServiceException {
        if(StringUtils.isEmpty(userInfo.getUserId()) || StringUtils.isEmpty(questionId)) throw new TeacherTestServiceException(ResultCode.PARAM_MISS_MSG);
        Optional<Question> result = questionRepository.findById(questionId);
        if(result.isPresent()){
            return convertAndFetchAnswerForQuestion(userInfo.getUserId(), result.get());
        }
        throw new TeacherTestServiceException(ResultCode.QUESTION_NOT_EXISTED);
    }

    @Override
    public AnswerModelForStudent getParticularAnswer(String questionId, String studentId) throws TeacherTestServiceException {
        if(StringUtils.isEmpty(questionId) || StringUtils.isEmpty(studentId)) throw new TeacherTestServiceException(ResultCode.PARAM_MISS_MSG);
        StudentAnswer studentAnswer = studentAnswerRepository.findFirstByQuestionIdAndStudentIdOrderByUpdateTimeDesc(questionId, studentId);
        return MappingEntity2ModelConverter.ConvertStudentAnswer(studentAnswer);
    }

    @Override
    public void saveTeacherAnswer(UserInfoForToken userInfo, String questionId, String studentAnswerId, AnswerModelForTeacher revise) throws TeacherTestServiceException {
        if(StringUtils.isEmpty(userInfo.getUserId())
                || StringUtils.isEmpty(questionId)
                || StringUtils.isEmpty(studentAnswerId)
                || StringUtils.isEmpty(revise.getStubForSubjective())) throw new TeacherTestServiceException(ResultCode.PARAM_MISS_MSG);
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
    public void updateParticularTestStatus(UserInfoForToken userInfo, TestModelForTeacher testModelForTeacher) throws TeacherTestServiceException {
        testConsoleService.updateTest2NewStatus(userInfo, testModelForTeacher);
    }

    private void countSubmittedAndTotalTest(List<TestModelForTeacher> tests){
        tests.forEach(h -> {
            h.setSubmitted((int)mongoTemplate.count(new Query(
                    Criteria.where("tests").elemMatch(Criteria.where("testId").is(h.getTestId())
                            .and("completedTime").ne(0))), StudentTestCollection.class));
            h.setTotal((int)mongoTemplate.count(new Query(
                    Criteria.where("tests").elemMatch(Criteria.where("testId").is(h.getTestId()))), StudentTestCollection.class));
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

    private List<TestModelForTeacher> convertTestList(List<Test> tests){
        List<TestModelForTeacher> testModelForTeachers = new ArrayList<TestModelForTeacher>();
        tests.forEach(test -> {
            testModelForTeachers.add(MappingEntity2ModelConverter.ConvertTeacherTest(test));
        });
        return testModelForTeachers;
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
