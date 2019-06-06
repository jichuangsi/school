package com.jichuangsi.school.testservice.service.impl;

import com.jichuangsi.microservice.common.model.UserInfoForToken;
import com.jichuangsi.school.testservice.constant.TestSort;
import com.jichuangsi.school.testservice.constant.ResultCode;
import com.jichuangsi.school.testservice.constant.Status;
import com.jichuangsi.school.testservice.entity.Test;
import com.jichuangsi.school.testservice.entity.TestSummary;
import com.jichuangsi.school.testservice.entity.Question;
import com.jichuangsi.school.testservice.entity.StudentTestCollection;
import com.jichuangsi.school.testservice.exception.TeacherTestServiceException;
import com.jichuangsi.school.testservice.model.TestModelForTeacher;
import com.jichuangsi.school.testservice.model.QuestionModelForTeacher;
import com.jichuangsi.school.testservice.model.SearchTestModel;
import com.jichuangsi.school.testservice.model.common.DeleteQueryModel;
import com.jichuangsi.school.testservice.model.common.Elements;
import com.jichuangsi.school.testservice.model.common.PageHolder;
import com.jichuangsi.school.testservice.model.transfer.TransferStudent;
import com.jichuangsi.school.testservice.model.transfer.TransferTeacher;
import com.jichuangsi.school.testservice.repository.TestConsoleRepository;
import com.jichuangsi.school.testservice.repository.QuestionRepository;
import com.jichuangsi.school.testservice.service.IExamInfoService;
import com.jichuangsi.school.testservice.service.ITestConsoleService;
import com.jichuangsi.school.testservice.service.IUserInfoService;
import com.jichuangsi.school.testservice.utils.MappingEntity2ModelConverter;
import com.jichuangsi.school.testservice.utils.MappingModel2EntityConverter;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

@Service
public class TestConsoleServiceImpl implements ITestConsoleService {

    @Resource
    private IUserInfoService userInfoService;

    @Resource
    private IExamInfoService examInfoService;

    @Resource
    private MongoTemplate mongoTemplate;

    @Resource
    private TestConsoleRepository testConsoleRepository;

    @Resource
    private QuestionRepository questionRepository;

    @Value("${com.jichuangsi.school.result.page-size}")
    private int defaultPageSize;

    @Override
    public void saveNewTest(UserInfoForToken userInfo, TestModelForTeacher testModelForTeacher) throws TeacherTestServiceException {
        if(StringUtils.isEmpty(userInfo.getUserId())) throw new TeacherTestServiceException(ResultCode.PARAM_MISS_MSG);
        TransferTeacher transferTeacher = userInfoService.getUserForTeacherById(userInfo.getUserId());
        if(transferTeacher==null) throw new TeacherTestServiceException(ResultCode.TEACHER_INFO_NOT_EXISTED);
        testModelForTeacher.setSubjectId(transferTeacher.getSubjectId());
        testModelForTeacher.setSubjectName(transferTeacher.getSubjectName());
        Test test = MappingModel2EntityConverter.ConvertTeacherTest(userInfo,testModelForTeacher);

        test = testConsoleRepository.save(test);
        if(test==null){
            throw new TeacherTestServiceException(ResultCode.TEST_FAIL_SAVE);
        }
        if(testModelForTeacher.getQuestions()!=null) {
            List<String> questionIds = new ArrayList<String>();
            Update update = new Update();
            for (QuestionModelForTeacher questionModelForTeacher : testModelForTeacher.getQuestions()) {
                questionModelForTeacher.setGradeId(transferTeacher.getGradeId());
                questionModelForTeacher.setSubjectId(transferTeacher.getSubjectId());
                Question question = MappingModel2EntityConverter.ConvertTeacherQuestion(questionModelForTeacher);
                question = questionRepository.save(question, testModelForTeacher.getPoints());
                /*if (question == null) {
                    throw new TeacherCourseServiceException(ResultCode.QUESTION_FAIL_SAVE);
                }*/
                questionIds.add(question.getId());
            }
            //test.setQuestionIds(questionIds);
            update.set("questionIds",questionIds);
            UpdateResult result = mongoTemplate.upsert(new Query(Criteria.where("_id").is(test.getId())),update,Test.class);
        }
    }

    @Override
    public Elements getElementsList(UserInfoForToken userInfo) throws TeacherTestServiceException {
        Elements elements = new Elements();
        elements.setTransferExams(examInfoService.getExamForTeacherById(userInfo.getUserId()));
        elements.setTransferClasses(userInfoService.getClassForTeacherById(userInfo.getUserId()));

        return elements;
    }

    @Override
    public PageHolder<TestModelForTeacher> getSortedTestsList(UserInfoForToken userInfo, SearchTestModel searchTestModel) throws TeacherTestServiceException {
        if(StringUtils.isEmpty(userInfo.getUserId())) throw new TeacherTestServiceException(ResultCode.PARAM_MISS_MSG);
        if(searchTestModel.getPageSize() == 0) searchTestModel.setPageSize(defaultPageSize);
        Criteria criteria = Criteria.where("teacherId").is(userInfo.getUserId());
        if(!StringUtils.isEmpty(searchTestModel.getTime())){
            criteria.and("endTime").lte(Long.valueOf(searchTestModel.getTime())+86400000l).gte(Long.valueOf(searchTestModel.getTime()));
        }
        if(!StringUtils.isEmpty(searchTestModel.getStatus())&&!Status.EMPTY.equals(searchTestModel.getStatus())){
            criteria.and("status").is(searchTestModel.getStatus().getName());
        }
        if (!StringUtils.isEmpty(searchTestModel.getKeyWord())){
            Pattern pattern= Pattern.compile("^.*"+searchTestModel.getKeyWord()+".*$", Pattern.CASE_INSENSITIVE);
            Criteria c1 = Criteria.where("info").regex(pattern);
            Criteria c2 = Criteria.where("name").regex(pattern);
            criteria.orOperator(c1,c2);
        }
        Query query = new Query(criteria);
        if(!StringUtils.isEmpty(searchTestModel.getSortNum())){
            if (TestSort.TIME.getName().equals(TestSort.getName(searchTestModel.getSortNum()))){
                query.with(new Sort(Sort.Direction.DESC,"endTime"));
            }else{
                query.with(new Sort(Sort.Direction.DESC,"createTime"));
            }
        }

        PageHolder<TestModelForTeacher> pageHolder = new PageHolder<TestModelForTeacher>();
        pageHolder.setTotal(mongoTemplate.find(query,Test.class).size());
        pageHolder.setPageNum(searchTestModel.getPageIndex());
        pageHolder.setPageSize(searchTestModel.getPageSize());
        query.skip((searchTestModel.getPageIndex()-1)*searchTestModel.getPageSize());
        query.limit(searchTestModel.getPageSize());
        List<Test> testList = mongoTemplate.find(query,Test.class);
        List<TestModelForTeacher> testModelList = new ArrayList<TestModelForTeacher>();
        testList.forEach(test -> {
            TestModelForTeacher testModelForTeacher =  MappingEntity2ModelConverter.ConvertTeacherTest(test);
            try{
                testModelForTeacher.getQuestions().addAll(this.getQuestionList(test.getQuestionIds()));
                testModelForTeacher.getStudents().addAll(userInfoService.getStudentsForClassById(test.getClassId()));
            }catch (Exception exp){}
            testModelList.add(testModelForTeacher);
        });
        pageHolder.setContent(testModelList);

        return pageHolder;
    }

    @Override
    public List<QuestionModelForTeacher> getQuestionList(List<String> qIds) throws TeacherTestServiceException {
        return convertQuestionList(mongoTemplate.find(new Query(Criteria.where("id").in(qIds)), Question.class));
    }

    @Override
    public void deleteTestIsNotStart(UserInfoForToken userInfo, DeleteQueryModel deleteQueryModel) throws TeacherTestServiceException {
        if(StringUtils.isEmpty(userInfo.getUserId())) throw new TeacherTestServiceException(ResultCode.PARAM_MISS_MSG);
        for(String id : deleteQueryModel.getIds()){
            Test test = mongoTemplate.findOne(new Query(Criteria.where("id").is(id).and("teacherId").is(userInfo.getUserId()).and("status").is(Status.NOTSTART.getName())),Test.class);
            if(test!=null){
                if(!test.getQuestionIds().isEmpty()){
                    DeleteResult result = mongoTemplate.remove(new Query(Criteria.where("id").in(test.getQuestionIds())), Question.class);
                    if(result.getDeletedCount()==0) throw new TeacherTestServiceException(ResultCode.TEST_DELETE_FAIL);
                }
                mongoTemplate.remove(test);
            }else{
                throw new TeacherTestServiceException(ResultCode.TEST_DELETE_FAIL);
            }
        }
    }

    @Override
    public void updateTestIsNotStart(UserInfoForToken userInfo, TestModelForTeacher testModelForTeacher) throws TeacherTestServiceException {
        if(StringUtils.isEmpty(userInfo.getUserId())) throw new TeacherTestServiceException(ResultCode.PARAM_MISS_MSG);
        Test test = mongoTemplate.findOne(new Query(Criteria.where("id").is(testModelForTeacher.getTestId()).and("teacherId").is(userInfo.getUserId()).and("status").is(Status.NOTSTART.getName())),Test.class);
        if(test!=null){
            Test updatedTest = MappingModel2EntityConverter.ConvertTeacherTest(userInfo, testModelForTeacher);
            Criteria criteria = Criteria.where("id").is(updatedTest.getId());
            Update update = new Update();
            if(!StringUtils.isEmpty(updatedTest.getName()))
                update.set("name",updatedTest.getName());
            if(!StringUtils.isEmpty(updatedTest.getClassId()))
                update.set("classId",updatedTest.getClassId());
            if(!StringUtils.isEmpty(updatedTest.getClassName()))
                update.set("className",updatedTest.getClassName());
            if(updatedTest.getEndTime()!=0)
                update.set("endTime",updatedTest.getEndTime());
            if(!StringUtils.isEmpty(updatedTest.getInfo()))
                update.set("info",updatedTest.getInfo());
            update.set("updateTime",new Date().getTime());
            mongoTemplate.updateFirst(new Query(criteria),update,Test.class);
        }else{
            throw new TeacherTestServiceException(ResultCode.TEST_ALREADY_BEGIN);
        }
    }

    @Override
    public void updateTest2NewStatus(UserInfoForToken userInfo, TestModelForTeacher testModelForTeacher) throws TeacherTestServiceException {
        if(StringUtils.isEmpty(userInfo.getUserId()) || Status.EMPTY.equals(testModelForTeacher.getTestStatus())) throw new TeacherTestServiceException(ResultCode.PARAM_MISS_MSG);
        String hwId = testModelForTeacher.getTestId();
        synchronized (hwId.intern()){
            Test test = mongoTemplate.findOne(new Query(Criteria.where("id").is(hwId).and("teacherId").is(userInfo.getUserId())),Test.class);
            if(test!=null){
                Test updatedTest = MappingModel2EntityConverter.ConvertTeacherTest(userInfo, testModelForTeacher);
                Criteria criteria = Criteria.where("id").is(updatedTest.getId());
                Update update = new Update();
                if(!StringUtils.isEmpty(updatedTest.getStatus())){
                    update.set("status",updatedTest.getStatus());
                    if(Status.PROGRESS.getName().equalsIgnoreCase(updatedTest.getStatus())){
                        update.set("publishTime",new Date().getTime());
                    }
                }
                update.set("updateTime",new Date().getTime());
                UpdateResult updateResult = mongoTemplate.updateFirst(new Query(criteria),update,Test.class);
                if(Status.PROGRESS.getName().equalsIgnoreCase(updatedTest.getStatus())&&updateResult.getModifiedCount()>0){
                    this.assignTest2Student(test);
                }else if(Status.COMPLETED.getName().equalsIgnoreCase(updatedTest.getStatus())&&updateResult.getModifiedCount()>0){
                    this.finishQuestionInTest(test);
                }
            }else{
                throw new TeacherTestServiceException(ResultCode.TEST_STATUS_ERROR);
            }
        }
    }

    private void assignTest2Student(Test test) throws TeacherTestServiceException {
        if(test == null) throw new TeacherTestServiceException(ResultCode.TEST_NOT_EXISTED);
        List<TransferStudent> students = userInfoService.getStudentsForClassById(test.getClassId());
        if(students.isEmpty()) throw new TeacherTestServiceException(ResultCode.STUDENT_INFO_NOT_EXISTED);
        students.forEach(s ->{
            if(!mongoTemplate.exists(
                    new Query(Criteria.where("studentId").is(s.getStudentId()).and("tests").elemMatch(Criteria.where("testId").is(test.getId()))),
                    StudentTestCollection.class)){
                Update update = new Update();
                update.set("studentId", s.getStudentId());
                update.set("studentAccount", s.getStudentAccount());
                update.set("studentName", s.getStudentName());
                update.addToSet("tests", new TestSummary(test.getId(), test.getName()));
                mongoTemplate.upsert(new Query(Criteria.where("studentId").is(s.getStudentId())),update, StudentTestCollection.class);
            }
        });
    }

    private void finishQuestionInTest(Test test) throws TeacherTestServiceException {
        if(test == null) throw new TeacherTestServiceException(ResultCode.TEST_NOT_EXISTED);
        Update update = new Update();
        update.set("status", Status.FINISH.getName());
        mongoTemplate.updateMulti(new Query(Criteria.where("id").in(test.getQuestionIds())), update, Question.class);
    }

    private List<TestModelForTeacher> convertTestList(List<Test> tests){
        List<TestModelForTeacher> testModelForTeacher = new ArrayList<TestModelForTeacher>();
        tests.forEach(test -> {
            testModelForTeacher.add(MappingEntity2ModelConverter.ConvertTeacherTest(test));
        });
        return testModelForTeacher;
    }

    private List<QuestionModelForTeacher> convertQuestionList(List<Question> questions){
        List<QuestionModelForTeacher> questionForTeachers = new ArrayList<QuestionModelForTeacher>();
        questions.forEach(question -> {
            questionForTeachers.add(MappingEntity2ModelConverter.ConvertTeacherQuestion(question));
        });
        return questionForTeachers;
    }
}
