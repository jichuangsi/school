package com.jichuangsi.school.testservice.service.impl;

import com.jichuangsi.microservice.common.model.UserInfoForToken;
import com.jichuangsi.school.testservice.constant.QuestionType;
import com.jichuangsi.school.testservice.constant.Result;
import com.jichuangsi.school.testservice.constant.ResultCode;
import com.jichuangsi.school.testservice.constant.Status;
import com.jichuangsi.school.testservice.entity.*;
import com.jichuangsi.school.testservice.exception.StudentTestServiceException;
import com.jichuangsi.school.testservice.model.AnswerModelForStudent;
import com.jichuangsi.school.testservice.model.TestModelForStudent;
import com.jichuangsi.school.testservice.model.QuestionModelForStudent;
import com.jichuangsi.school.testservice.model.SearchTestModel;
import com.jichuangsi.school.testservice.model.common.PageHolder;
import com.jichuangsi.school.testservice.repository.TestRepository;
import com.jichuangsi.school.testservice.repository.QuestionRepository;
import com.jichuangsi.school.testservice.repository.StudentAnswerRepository;
import com.jichuangsi.school.testservice.repository.TeacherAnswerRepository;
import com.jichuangsi.school.testservice.service.IAutoVerifyAnswerService;
import com.jichuangsi.school.testservice.service.IStudentTestService;
import com.jichuangsi.school.testservice.utils.MappingEntity2ModelConverter;
import com.jichuangsi.school.testservice.utils.MappingModel2EntityConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.*;

@Service
public class StudentTestServiceImpl implements IStudentTestService {

    @Resource
    private IAutoVerifyAnswerService autoVerifyAnswerService;

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
    public List<TestModelForStudent> getTestsList(UserInfoForToken userInfo) throws StudentTestServiceException{
        if(StringUtils.isEmpty(userInfo.getUserId())) throw new StudentTestServiceException(ResultCode.PARAM_MISS_MSG);
        List<TestModelForStudent> tests = convertTestList(testRepository.findProgressTestByStudentId(userInfo.getUserId()));
        checkTestCompleted(userInfo, tests);
        return tests;
    }

    @Override
    public PageHolder<TestModelForStudent> getHistoryTestsList(UserInfoForToken userInfo, SearchTestModel searchTestModel) throws StudentTestServiceException{
        if(StringUtils.isEmpty(userInfo.getUserId())) throw new StudentTestServiceException(ResultCode.PARAM_MISS_MSG);
        if(searchTestModel.getPageSize() == 0) searchTestModel.setPageSize(defaultPageSize);
        PageHolder<TestModelForStudent> pageHolder = new PageHolder<TestModelForStudent>();
        pageHolder.setTotal(testRepository.countFinishedTestByStudentId(userInfo.getUserId()));
        pageHolder.setPageNum(searchTestModel.getPageIndex());
        pageHolder.setPageSize(searchTestModel.getPageSize());
        List<TestModelForStudent> tests = convertTestList(testRepository.findFinishedTestByStudentId(userInfo.getUserId(), searchTestModel.getPageIndex(), searchTestModel.getPageSize()));
        checkTestCompleted(userInfo, tests);
        pageHolder.setContent(tests);
        return pageHolder;
    }

    @Override
    public TestModelForStudent getParticularTest(UserInfoForToken userInfo, String testId) throws StudentTestServiceException{
        if(StringUtils.isEmpty(testId)) throw new StudentTestServiceException(ResultCode.PARAM_MISS_MSG);
        Test test = testRepository.findFirstByIdOrderByUpdateTimeDesc(testId);
        if(test==null) throw new StudentTestServiceException(ResultCode.TEST_NOT_EXISTED);
        List<Question> questions = questionRepository.findQuestionsByTestId(testId);
        TestModelForStudent testModelForStudent = MappingEntity2ModelConverter.ConvertStudentTest(test);
        testModelForStudent.getQuestions().addAll(convertQuestionList(questions));
        testModelForStudent.getQuestions().forEach(question ->{
            question.setFavor(mongoTemplate.exists(new Query(Criteria.where("studentId").is(userInfo.getUserId()).and("questionIds").is(question.getQuestionId())), StudentFavorQuestion.class));
            Optional<StudentAnswer> result = Optional.ofNullable(studentAnswerRepository.findFirstByQuestionIdAndStudentIdOrderByUpdateTimeDesc(question.getQuestionId(), userInfo.getUserId()));
            if(result.isPresent()){
                question.setAnswerModelForStudent(MappingEntity2ModelConverter.ConvertStudentAnswer(result.get()));
                if(QuestionType.SUBJECTIVE.getName().equalsIgnoreCase(question.getQuestionType())){
                    if(Result.PASS.getName().equalsIgnoreCase(result.get().getResult())){
                        question.setAnswerModelForTeacher(MappingEntity2ModelConverter.ConvertTeacherAnswer(
                                teacherAnswerRepository.findFirstByTeacherIdAndQuestionIdAndStudentAnswerIdOrderByUpdateTimeDesc(test.getTeacherId(), question.getQuestionId(), result.get().getId())
                        ));
                    }
                }
            }
            /*if(question.getAnswerModelForTeacher()==null){
                TeacherAnswer shareAnswer = teacherAnswerRepository.findFirstByTeacherIdAndQuestionIdAndIsShareOrderByShareTimeDesc(test.getTeacherId(), question.getQuestionId(), true);
                if(shareAnswer !=null) question.setAnswerModelForTeacher(MappingEntity2ModelConverter.ConvertTeacherAnswer(shareAnswer));
            }*/
        });
        checkTestCompleted(userInfo, Arrays.asList(testModelForStudent));
        return testModelForStudent;
    }

    @Override
    public void saveStudentAnswer(UserInfoForToken userInfo, String testId, String questionId, AnswerModelForStudent answer) throws StudentTestServiceException {
        if(StringUtils.isEmpty(userInfo.getUserId())
                || StringUtils.isEmpty(testId)
                || StringUtils.isEmpty(questionId)
                || (StringUtils.isEmpty(answer.getAnswerForObjective()) && StringUtils.isEmpty(answer.getStubForSubjective())))
            throw new StudentTestServiceException(ResultCode.PARAM_MISS_MSG);
        String userId = userInfo.getUserId();
        synchronized (userId.intern()){//需要实现分布式锁
            Test test = testRepository.findFirstByIdOrderByUpdateTimeDesc(testId);
            if(Status.NOTSTART.getName().equalsIgnoreCase(test.getStatus())) throw new StudentTestServiceException(ResultCode.TEST_NOTSTART);
            if(Status.FINISH.getName().equalsIgnoreCase(test.getStatus())
                    ||Status.COMPLETED.getName().equalsIgnoreCase(test.getStatus())) throw new StudentTestServiceException(ResultCode.TEST_FINISHED);
            Optional<Question> question = questionRepository.findById(questionId);
            if(!question.isPresent()) throw new StudentTestServiceException(ResultCode.QUESTION_NOT_EXISTED);
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
                    new Query(Criteria.where("studentId").is(userInfo.getUserId()).and("tests").elemMatch(Criteria.where("testId").is(testId))),
                    new Update().set("tests.$.updateTime", new Date().getTime()), StudentTestCollection.class);
        }
    }

    @Override
    public void submitParticularTest(UserInfoForToken userInfo, String testId) throws StudentTestServiceException{
        if(StringUtils.isEmpty(userInfo.getUserId())
                || StringUtils.isEmpty(testId))
            throw new StudentTestServiceException(ResultCode.PARAM_MISS_MSG);
        String userId = userInfo.getUserId();
        synchronized (userId.intern()){//需要实现分布式锁
            Test test = testRepository.findFirstByIdOrderByUpdateTimeDesc(testId);
            if(Status.NOTSTART.getName().equalsIgnoreCase(test.getStatus())) throw new StudentTestServiceException(ResultCode.TEST_NOTSTART);
            if(Status.FINISH.getName().equalsIgnoreCase(test.getStatus())
                    ||Status.COMPLETED.getName().equalsIgnoreCase(test.getStatus())) throw new StudentTestServiceException(ResultCode.TEST_FINISHED);
            mongoTemplate.updateFirst(
                    new Query(Criteria.where("studentId").is(userInfo.getUserId()).and("tests").elemMatch(Criteria.where("testId").is(testId))),
                    new Update().set("tests.$.completedTime", new Date().getTime()), StudentTestCollection.class);
        }
    }

    private void checkTestCompleted(UserInfoForToken userInfo, List<TestModelForStudent> tests){
        tests.forEach(h -> {
            h.setCompleted(mongoTemplate.exists(
                    new Query(Criteria.where("studentId").is(userInfo.getUserId())
                            .and("tests").elemMatch(Criteria.where("testId").is(h.getTestId()).and("completedTime").ne(0))),
                    StudentTestCollection.class));
        });
    }

    private List<TestModelForStudent> convertTestList(List<Test> tests){
        List<TestModelForStudent> testModelForStudents = new ArrayList<TestModelForStudent>();
        tests.forEach(test -> {
            testModelForStudents.add(MappingEntity2ModelConverter.ConvertStudentTest(test));
        });
        return testModelForStudents;
    }

    private List<QuestionModelForStudent> convertQuestionList(List<Question> questions){
        List<QuestionModelForStudent> questionForStudents = new ArrayList<QuestionModelForStudent>();
        questions.forEach(question -> {
            questionForStudents.add(MappingEntity2ModelConverter.ConvertStudentQuestion(question));
        });
        return questionForStudents;
    }
}
