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
import com.jichuangsi.school.testservice.model.feign.SearchTestModelId;
import com.jichuangsi.school.testservice.model.statistics.*;
import com.jichuangsi.school.testservice.model.transfer.TransferKnowledge;
import com.jichuangsi.school.testservice.repository.*;
import com.jichuangsi.school.testservice.service.IAutoVerifyAnswerService;
import com.jichuangsi.school.testservice.service.IStudentTestService;
import com.jichuangsi.school.testservice.utils.MappingEntity2ModelConverter;
import com.jichuangsi.school.testservice.utils.MappingModel2EntityConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestParam;

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
    private TestTestStatisticsRepository testTestStatisticsRepository;
    @Resource
    private MongoTemplate mongoTemplate;

    @Value("${com.jichuangsi.school.result.page-size}")
    private int defaultPageSize;

    @Override
    public List<TestModelForStudent> getTestsList(UserInfoForToken userInfo) throws StudentTestServiceException{
        if(StringUtils.isEmpty(userInfo.getUserId())) throw new StudentTestServiceException(ResultCode.PARAM_MISS_MSG);
        List<TestModelForStudent> tests = convertTestList(testRepository.findProgressTestByStudentId(userInfo.getUserId()));
        this.getTestStuff(userInfo, tests);
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
        this.getTestStuff(userInfo, tests);
        pageHolder.setContent(tests);
        return pageHolder;
    }
    @Override
    public PageHolder<TestModelForStudent> getHistoryTestsListFeign(String studentId, SearchTestModelId searchTestModel) throws StudentTestServiceException{
        if(StringUtils.isEmpty(studentId)) throw new StudentTestServiceException(ResultCode.PARAM_MISS_MSG);
        if(searchTestModel.getPageSize() == 0) searchTestModel.setPageSize(defaultPageSize);
        PageHolder<TestModelForStudent> pageHolder = new PageHolder<TestModelForStudent>();
        pageHolder.setTotal(testRepository.countFinishedTestByStudentId(studentId));
        pageHolder.setPageNum(searchTestModel.getPageIndex());
        pageHolder.setPageSize(searchTestModel.getPageSize());
        List<TestModelForStudent> tests = convertTestList(testRepository.findFinishedTestByStudentId(studentId, searchTestModel.getPageIndex(), searchTestModel.getPageSize()));
        this.getTestStuff(studentId, tests);
        pageHolder.setContent(tests);
        return pageHolder;
    }

    @Override
    public TestModelForStudent getParticularTest(UserInfoForToken userInfo, String testId) throws StudentTestServiceException{
        if(StringUtils.isEmpty(userInfo.getUserId())
                || StringUtils.isEmpty(testId)) throw new StudentTestServiceException(ResultCode.PARAM_MISS_MSG);
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
        this.getTestStuff(userInfo, Arrays.asList(testModelForStudent));
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

    private void getTestStuff(UserInfoForToken userInfo, List<TestModelForStudent> tests){
        tests.forEach(h -> {
            //检查是否完成
            h.setCompleted(mongoTemplate.exists(
                    new Query(Criteria.where("studentId").is(userInfo.getUserId())
                            .and("tests").elemMatch(Criteria.where("testId").is(h.getTestId()).and("completedTime").ne(0))),
                    StudentTestCollection.class));
            //获取总分
            StudentTestCollection s = mongoTemplate.findOne(
                    new Query(
                            Criteria.where("studentId").is(userInfo.getUserId())
                                    .and("tests").elemMatch(Criteria.where("testId").is(h.getTestId()))
                    ), StudentTestCollection.class);
            if(s!=null) h.setTotalScore(s.getTests().stream().filter(
                    t->t.getTestId().equalsIgnoreCase(h.getTestId())).findFirst().get().getTotalScore());
        });
    }

    private void getTestStuff(String studentId, List<TestModelForStudent> tests){
        //检查是否完成
        tests.forEach(h -> {
            h.setCompleted(mongoTemplate.exists(
                    new Query(Criteria.where("studentId").is(studentId)
                            .and("tests").elemMatch(Criteria.where("testId").is(h.getTestId()).and("completedTime").ne(0))),
                    StudentTestCollection.class));
            //获取总分
            StudentTestCollection s = mongoTemplate.findOne(
                    new Query(
                            Criteria.where("studentId").is(studentId)
                                    .and("tests").elemMatch(Criteria.where("testId").is(h.getTestId()))
                    ), StudentTestCollection.class);
            if(s!=null) h.setTotalScore(s.getTests().stream().filter(
                    t->t.getTestId().equalsIgnoreCase(h.getTestId())).findFirst().get().getTotalScore());
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

    @Override
    public TransferKnowledge getKnowledgeOfParticularQuestion(String questionId) throws StudentTestServiceException {
        if (StringUtils.isEmpty(questionId)) throw new StudentTestServiceException(ResultCode.PARAM_MISS_MSG);
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
        throw new StudentTestServiceException(ResultCode.QUESTION_NOT_EXISTED);
    }
    @Override
    public List<ResultKnowledgeModel> getQuestionKnowledges(List<String> questionIds) throws StudentTestServiceException {
        if (null == questionIds) {
            throw new StudentTestServiceException(ResultCode.SELECT_NULL_MSG);
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
    @Override
    public List<KnowledgeStatisticsModel> getParentStatistics(ParentStatisticsModel model) throws StudentTestServiceException{
        if (StringUtils.isEmpty(model.getStudentId())|| StringUtils.isEmpty(model.getSubjectName()) || 0 == model.getStudentNum()){
            throw new StudentTestServiceException(ResultCode.PARAM_MISS_MSG);
        }
        List<Test> test =new ArrayList<Test>();
        if (model.getBeignTime()>0){
           test.addAll(testRepository.findByClassIdAndStatusAndEndTimeIsGreaterThanAndSubjectNameLikeOrderByCreateTime(model.getClassId(),Status.FINISH.getName(),model.getBeignTime(),model.getSubjectName()));

        }else {
            if (!(model.getStatisticsTimes().size() > 0)) {
                throw new StudentTestServiceException(ResultCode.PARAM_MISS_MSG);
            }


                for (Long beignTime : model.getStatisticsTimes()) {
                if (null == beignTime) {
                    continue;
                }
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(new Date(beignTime));
                calendar.set(Calendar.HOUR_OF_DAY, 23);
                calendar.set(Calendar.MINUTE, 59);
                calendar.set(Calendar.SECOND, 59);
                test.addAll(testTestStatisticsRepository.findByClassIdAndStatusAndEndTimeGreaterThanAndEndTimeLessThanAndSubjectNameLike(model.getClassId(), Status.FINISH.getName(), beignTime, calendar.getTimeInMillis(), model.getSubjectName()));
            }

        }
        List<String> questionIds = new ArrayList<String>();
        for (Test test1 : test) {
            questionIds.addAll(test1.getQuestionIds());
        }
        Map<String,Integer> selfTrue = new HashMap<String, Integer>();
        Map<String,Integer> classTrue = new HashMap<String, Integer>();
        for (String qid : questionIds){
            int trueNum = 0;
            int selfNum = 0;
            List<StudentAnswer> answers = studentAnswerRepository.findAllByQuestionId(qid);
            for (StudentAnswer answer : answers){
                if (Result.CORRECT.getName().equals(answer.getResult())){
                    trueNum ++;
                }else if (Result.PASS.getName().equals(answer.getResult())){
                    //todo 主观题统计
                }
                if (model.getStudentId().equals(answer.getStudentId())){
                    selfNum = 1;
                }
            }
            selfTrue.put(qid,selfNum);
            classTrue.put(qid,trueNum);
        }
        try {
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
                    qIds.add(knowledgeModel.getQuestionId());
                    questionMap.put(knowledge.getKnowledge(), qIds);
                }
            }
            int questionNum = 0;
            for (String key : questionMap.keySet()){
                questionNum = questionNum + questionMap.get(key).size();
            }
            List<KnowledgeStatisticsModel> models = new ArrayList<KnowledgeStatisticsModel>();
            for (String key : questionMap.keySet()){
                int trueNum = 0;
                int classNum = 0;
                for (String questionId: questionMap.get(key)){
                    trueNum = trueNum + selfTrue.get(questionId);
                    classNum = classNum + classTrue.get(questionId);
                }
                KnowledgeStatisticsModel statisticsModel = new KnowledgeStatisticsModel();

                statisticsModel.setClassRightAvgRate((double) classNum / (model.getStudentNum() * questionMap.get(key).size()));
                statisticsModel.setKnowledgeName(key);
                if (0 != questionNum) {
                    double rate = (double)questionMap.get(key).size() /questionNum;
                    statisticsModel.setKnowledgeRate(rate);
                }
                statisticsModel.setStudentRightRate((double) trueNum / questionMap.get(key).size());
                models.add(statisticsModel);
            }
            return models;
        } catch (StudentTestServiceException e) {
            throw new StudentTestServiceException(e.getMessage());
        }
    }
//根据试卷Id查问题
    @Override
    public List<Test> getTestBySubjectNameAndTestName(List<String> classId,String subjectId,long time) throws StudentTestServiceException{
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date(time));
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        Calendar calendar1 = Calendar.getInstance();
        calendar1.setTime(new Date(time));
        calendar1.set(Calendar.HOUR_OF_DAY, 23);
        calendar1.set(Calendar.MINUTE, 59);
        calendar1.set(Calendar.SECOND, 59);
        Criteria criteria=Criteria.where("classId").in(classId).and("subjectName").is(subjectId);
        criteria.andOperator(Criteria.where("endTime").lte(calendar1.getTimeInMillis()), Criteria.where("endTime").gte(calendar.getTimeInMillis()));
        Query query = new Query(criteria);
        List<Test> tests= mongoTemplate.find(query,Test.class);
        List<String> name=new ArrayList<String>();
        for (Test test:tests){
            name.add(test.getId());
        }
        List<Test> tests1=mongoTemplate.find(new Query(Criteria.where("id").in(name).and("subjectName").is(subjectId)),Test.class);
        return tests1;
    }


    @Override
    public TestScoreModel getTestByTestId(String testId) {
        Test tests=mongoTemplate.findById(testId,Test.class);
        Criteria criteria=Criteria.where("tests.0.testId").is(testId);
        Query query=new Query(criteria);
        query.with(new Sort(Sort.Direction.DESC,"tests.0.totalScore"));
        List<StudentTestCollection> testCollections=mongoTemplate.find(query,StudentTestCollection.class);
       // List<TestScoreModel> testScoreModel=new ArrayList<TestScoreModel>();
        TestScoreModel tsm=new TestScoreModel();
        Double maxScore=0.0,minScor=0.0,sum=0.0;
        int size=0;//记录集合长度
        if (testCollections.size()!=0){
        for (StudentTestCollection test:testCollections){
            size++;
            sum=sum+test.getTests().get(0).getTotalScore();
            maxScore=testCollections.get(0).getTests().get(0).getTotalScore();
            if (size<testCollections.size()){//判断是否是最后一个元素
                continue;
            }
            minScor=test.getTests().get(0).getTotalScore();
            tsm=new TestScoreModel(test.getTests().get(0).getTestName(),maxScore,minScor,sum/size,size);
        }
    }else {
        tsm = new TestScoreModel(tests.getName(), maxScore, minScor, 0.0, size);
    }
        return tsm;
    }


    @Override//每一次考试
    public HomeworkKnoledge getTestById(String testId) throws StudentTestServiceException{
        Test homework=mongoTemplate.findById(testId,Test.class);
        if (homework==null){throw  new StudentTestServiceException(ResultCode.SELECT_NULL_MSG);}
        List<StudentTestCollection> student=mongoTemplate.find(new Query(Criteria.where("tests.0.testId").is(homework.getId())),StudentTestCollection.class);
        List<String> studentId=new ArrayList<String>();
        if (student.size()!=0){
            for (StudentTestCollection stu:student) {
                studentId.add(stu.getStudentId());
            }
        }
        HomeworkKnoledge homeworkKnoledge=null;
        StudentAnswer studentAnswer=null;
        Question questions=null;
        Long count1=0L;
        Map<String,Integer> kkk=new HashMap<String,Integer>();
        Map<String,Integer> konC=new HashMap<String,Integer>();
        Map<String,Integer> konE=new HashMap<String,Integer>();
        Map<String,Integer> konCount=new HashMap<String,Integer>();
        int kk=0,kc=0,ke=0;

        for (String s:homework.getQuestionIds()) {
            //questions=questionRepository.findByIdAndStatus(s,Status.NOTSTART.getName());
            questions = mongoTemplate.findById(s, Question.class);
            if (questions != null) {
                for (Knowledge k : questions.getKnowledges()) {
                    kkk.put(k.getKnowledge(), kk++);
                }
                    for (String sid : studentId) {
                        Criteria criteria = Criteria.where("questionId").is(s).and("studentId").is(sid);
                        Query query = new Query(criteria);
                        query.with(new Sort(Sort.Direction.DESC, "updateTime"));
                        studentAnswer = mongoTemplate.findOne(query, StudentAnswer.class);
                        if (studentAnswer != null) {
                            if (studentAnswer.getResult() != null && !(studentAnswer.getResult().equals(""))) {
                                if (studentAnswer.getResult().equalsIgnoreCase(Result.CORRECT.getName())) {
                                    //获取知识点占比
                                    Object object = konC.get(questions.getKnowledges().get(0).getKnowledge());
                                    if (null == object) {
                                        konC.put(questions.getKnowledges().get(0).getKnowledge(), 1);
                                    } else {
                                        konC.put(questions.getKnowledges().get(0).getKnowledge(), (int) object + 1);
                                    }
                                }else {
                                    Object object = konE.get(questions.getKnowledges().get(0).getKnowledge());
                                    if (null == object) {
                                        konE.put(questions.getKnowledges().get(0).getKnowledge(), 1);
                                    } else {
                                        konE.put(questions.getKnowledges().get(0).getKnowledge(), (int) object + 1);
                                    }
                                }
                            }else {
                                Object object = konE.get(questions.getKnowledges().get(0).getKnowledge());
                                if (null == object) {
                                    konE.put(questions.getKnowledges().get(0).getKnowledge(), 1);
                                } else {
                                    konE.put(questions.getKnowledges().get(0).getKnowledge(), (int) object + 1);
                                }
                            }
                        }
                }
            }
        }
        for (String k : kkk.keySet()) {
            Criteria criteria1 = Criteria.where("id").in(homework.getQuestionIds()).and("knowledges.0.knowledge").is(k);
            Query query1 = new Query(criteria1);
            count1 = mongoTemplate.count(query1, Question.class);
            konCount.put(k, count1.intValue()*studentId.size());
        }

        List<String> kondege=new ArrayList<String>();
        Set<Map.Entry<String,Integer>> entrySet = konCount.entrySet();//所有知识点类型
        Set<Map.Entry<String,Integer>> entrySet1 = konC.entrySet();//知识点正确数
         Set<Map.Entry<String,Integer>> entrySet2 = konE.entrySet();//知识点错误数
        KnowledgePoints knowledgePoints2=null;
        List<KnowledgePoints> knowledgePoints1=new ArrayList<KnowledgePoints>();
        if (konC.size()!=0 && konE.size()!=0) {
            for (Map.Entry<String, Integer> entry : entrySet) {
                for (Map.Entry<String, Integer> entry1 : entrySet1) {
                    for (Map.Entry<String, Integer> entry2 : entrySet2) {
                        if (entry.getKey().equalsIgnoreCase(entry1.getKey())  && entry1.getKey().equalsIgnoreCase(entry2.getKey())){
                            int sec = entry.getValue();
                            int scc = entry1.getValue();
                            int see=entry2.getValue();
                            if (studentId.size()!=0) {
                                knowledgePoints2 = new KnowledgePoints(entry.getKey(), sec / studentId.size(), ((double) scc / sec) * 100, (1 - ((double) scc / sec)) * 100);
                            }else {
                                knowledgePoints2 = new KnowledgePoints(entry.getKey(), sec , ((double) scc / sec) * 100, (1 - ((double) scc / sec)) * 100);
                            }
                            knowledgePoints1.add(knowledgePoints2);
                            kondege.add(entry.getKey());
                        }
                    }
                }
            }
            if (entrySet1.size()<entrySet2.size()){
                for (Map.Entry<String, Integer> entry : entrySet) {
                    for (Map.Entry<String, Integer> entry1 : entrySet2) {
                        if (entry.getKey().equalsIgnoreCase(entry1.getKey()) ) {
                            boolean ok = konC.containsKey(entry1.getKey());
                            if (ok == false) {
                                if (studentId.size()!=0) {
                                    knowledgePoints2 = new KnowledgePoints(entry1.getKey(), entry.getValue() / studentId.size(), 0.0, 100.0);
                                }else {
                                    knowledgePoints2 = new KnowledgePoints(entry1.getKey(), entry.getValue(), 0.0, 100.0);
                                }
                                knowledgePoints1.add(knowledgePoints2);
                                kondege.add(entry.getKey());
                            }
                        }
                    }
                }
            }
            if (entrySet1.size()>entrySet2.size()){
                for (Map.Entry<String, Integer> entry : entrySet) {
                    for (Map.Entry<String, Integer> entry1 : entrySet1) {
                        if (entry.getKey().equalsIgnoreCase(entry1.getKey()) ) {
                            boolean ok = konE.containsKey(entry1.getKey());
                            if (ok == false) {
                                if (studentId.size()!=0) {
                                    knowledgePoints2 = new KnowledgePoints(entry1.getKey(), entry.getValue() / studentId.size(), 100.0, 0.0);
                                }else {
                                    knowledgePoints2 = new KnowledgePoints(entry1.getKey(), entry.getValue() , 100.0, 0.0);
                                }
                                knowledgePoints1.add(knowledgePoints2);
                                kondege.add(entry.getKey());
                            }
                        }
                    }
                }
            }
        }
        if (konC.size()!=0 && konE.size()==0) {
            for (Map.Entry<String, Integer> entry : entrySet) {
                for (Map.Entry<String, Integer> entry1 : entrySet1) {
                        int sec = entry.getValue();
                        int see = entry1.getValue();
                        if (entry.getKey().equalsIgnoreCase(entry1.getKey())){
                            if (studentId.size()!=0) {
                                knowledgePoints2 = new KnowledgePoints(entry.getKey(), sec / studentId.size(), 0.0, ((double) see / sec) * 100);
                            }else {
                                knowledgePoints2 = new KnowledgePoints(entry.getKey(), sec, 0.0, ((double) see / sec) * 100);
                            }
                            knowledgePoints1.add(knowledgePoints2);
                            kondege.add(entry.getKey());
                        }
                }
            }
        }
        if (konC.size()==0 && konE.size()!=0) {
            for (Map.Entry<String, Integer> entry : entrySet) {
                for (Map.Entry<String, Integer> entry2 : entrySet1) {
                    int sec = entry.getValue();
                    int scc = entry2.getValue();
                    if (entry.getKey().equalsIgnoreCase(entry2.getKey())){
                        if (studentId.size()!=0) {
                            knowledgePoints2 = new KnowledgePoints(entry.getKey(), sec / studentId.size(), ((double) scc / sec) * 100, 0.0);
                        }else {
                            knowledgePoints2 = new KnowledgePoints(entry.getKey(), sec , ((double) scc / sec) * 100, 0.0);
                        }
                        knowledgePoints1.add(knowledgePoints2);
                        kondege.add(entry.getKey());
                    }
                }
            }
        }
        if (konC.size()==0 && konE.size()==0) {
            for (Map.Entry<String, Integer> entry : entrySet) {
                    int sec = entry.getValue();
                if (studentId.size()!=0) {
                    knowledgePoints2 = new KnowledgePoints(entry.getKey(), sec / studentId.size(), 0.0, 0.0);
                }else {
                    knowledgePoints2 = new KnowledgePoints(entry.getKey(), sec / studentId.size(), 0.0, 0.0);
                }
                knowledgePoints1.add(knowledgePoints2);
                kondege.add(entry.getKey());
            }
        }
       for (Map.Entry<String, Integer> entry : entrySet){
            boolean ok=kondege.contains(entry.getKey());
            if (ok==false){
                if (studentId.size()!=0) {
                    knowledgePoints2 = new KnowledgePoints(entry.getKey(), entry.getValue() / studentId.size(), 0.0, 0.0);
                }else {
                    knowledgePoints2 = new KnowledgePoints(entry.getKey(), entry.getValue() , 0.0, 0.0);
                }
                knowledgePoints1.add(knowledgePoints2);
            }
       }

        homeworkKnoledge=new HomeworkKnoledge(homework.getName(),knowledgePoints1);
        return homeworkKnoledge;
    }

}
