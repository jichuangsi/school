package com.jichuangsi.school.homeworkservice.feign.service.impl;

import com.jichuangsi.microservice.common.model.ResponseModel;
import com.jichuangsi.school.homeworkservice.constant.Result;
import com.jichuangsi.school.homeworkservice.constant.Status;
import com.jichuangsi.school.homeworkservice.entity.*;
import com.jichuangsi.school.homeworkservice.feign.service.FeignStaticsService;
import com.jichuangsi.school.homeworkservice.model.Report.*;
import com.jichuangsi.school.homeworkservice.repository.HomeworkRepository;
import com.jichuangsi.school.homeworkservice.repository.QuestionRepository;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.*;

@Service
public class FeignStaticsServiceImpl implements FeignStaticsService {
    @Resource
    private MongoTemplate mongoTemplate;
    @Resource
    private QuestionRepository questionRepository;
    @Resource
    private HomeworkRepository homeworkRepository;

    @Override
    public List<Question> getQuestionBySubjectId(String subjectId) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.MONTH,-1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        List<Question> list = questionRepository.findBySubjectIdAndCreateTimeGreaterThan
                (subjectId,calendar.getTimeInMillis());
        return questionRepository.findBySubjectIdAndCreateTimeGreaterThan
                (subjectId,calendar.getTimeInMillis());
    }
    @Override
    public Question getQuestion(String questionId) {
        return mongoTemplate.findOne(new Query(Criteria.where("id").is(questionId)),Question.class);
    }


    @Override
    public List<StudentAnswer> getQuestionResult(List<String> questionId) {
        return mongoTemplate.find(new Query(Criteria.where("questionId").in(questionId)),StudentAnswer.class);
    }

    @Override
    public List<StudentAnswer> getQuestionByStudentId(String studentId) {
        return mongoTemplate.find(new Query(Criteria.where("studentId").is(studentId)),StudentAnswer.class);
    }

    @Override
    public Question getQuestionKnowledges(String questionId) {
        return mongoTemplate.findOne(new Query(Criteria.where("id").is(questionId)),Question.class);
    }

    @Override
    public Homework getSubjectIdByHomeworkId(String homeworkId,String classId) {

        return mongoTemplate.findOne(new Query(Criteria.where("id").is(homeworkId).and("classId").is(classId)),Homework.class);
    }

    @Override
    public List<StudentHomeworkCollection> getTotalScoreByHomeworkId(String homeworkId) {
        return mongoTemplate.find(new Query(Criteria.where("homeworks.0.homeworkId").is(homeworkId)),StudentHomeworkCollection.class);

    }



    @Override//每一次习题
    public HomeworkKnoledge getHomeworkByHomeworkId(String homeworkId) {
        Homework homework=mongoTemplate.findById(homeworkId,Homework.class);
        if (homework==null){}
        List<StudentHomeworkCollection> student=mongoTemplate.find(new Query(Criteria.where("homeworks.0.homeworkId").is(homework.getId())),StudentHomeworkCollection.class);
        List<String> studentId=new ArrayList<String>();
        if (student.size()!=0){
            for (StudentHomeworkCollection stu:student) {
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
                //for (String k : kkk.keySet()) {
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
               // }
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
                            knowledgePoints2 = new KnowledgePoints(entry.getKey(), sec/studentId.size(), ((double) scc / sec) * 100, (1-((double) scc / sec)) * 100);
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
                                knowledgePoints2 = new KnowledgePoints(entry1.getKey(), entry.getValue()/studentId.size(), 0.0, 100.0);
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
                                knowledgePoints2 = new KnowledgePoints(entry1.getKey(), entry.getValue()/studentId.size(),  100.0, 0.0);
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
                        knowledgePoints2 = new KnowledgePoints(entry.getKey(), sec/studentId.size(), 0.0, ((double) see / sec) * 100);
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
                        knowledgePoints2 = new KnowledgePoints(entry.getKey(), sec/studentId.size(), ((double) scc / sec) * 100, 0.0);
                        knowledgePoints1.add(knowledgePoints2);
                        kondege.add(entry.getKey());
                    }
                }
            }
        }
        if (konC.size()==0 && konE.size()==0) {
            for (Map.Entry<String, Integer> entry : entrySet) {
                int sec = entry.getValue();
                knowledgePoints2 = new KnowledgePoints(entry.getKey(), sec/studentId.size(), 0.0, 0.0);
                knowledgePoints1.add(knowledgePoints2);
                kondege.add(entry.getKey());
            }
        }
        for (Map.Entry<String, Integer> entry : entrySet){
            boolean ok=kondege.contains(entry.getKey());
            if (ok==false){
                knowledgePoints2 = new KnowledgePoints(entry.getKey(), entry.getValue()/studentId.size(), 0.0, 0.0);
                knowledgePoints1.add(knowledgePoints2);
            }
        }

        homeworkKnoledge=new HomeworkKnoledge(homework.getName(),knowledgePoints1);
        return homeworkKnoledge;
    }

    @Override
    public List<Homework> getHomeworkBySubjectNameAndHomeworkId(List<String> classId, String subjectId, long time) {
        //根据学科名字和考试名称查询考试信息
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date(time));
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        Calendar calendar1 = Calendar.getInstance();
        calendar.setTime(new Date(time));
        calendar1.set(Calendar.HOUR_OF_DAY, 23);
        calendar1.set(Calendar.MINUTE, 59);
        calendar1.set(Calendar.SECOND, 59);

        List<String> name=new ArrayList<String>();
        Criteria criteria=Criteria.where("classId").in(classId).and("subjectName").is(subjectId);
        criteria.andOperator(Criteria.where("endTime").lte(calendar1.getTimeInMillis()), Criteria.where("endTime").gte(calendar.getTimeInMillis()));
        List<Homework>  homework=mongoTemplate.find(new Query(criteria),Homework.class);

        for (Homework c:homework) {
            if (homework!=null){
                name.add(c.getId());
            }
        }
        List<Homework> tests=mongoTemplate.find(new Query(Criteria.where("id").in(name).and("subjectName").is(subjectId)),Homework.class);
        return tests;
    }

    @Override
    public TestScoreModel getHomeworkById(String HomeworkId) {
        Homework tests=mongoTemplate.findById(HomeworkId,Homework.class);
        Criteria criteria=Criteria.where("homeworks.0.homeworkId").is(HomeworkId);
        Query query=new Query(criteria);
        query.with(new Sort(Sort.Direction.DESC,"homeworks.0.totalScore"));
        List<StudentHomeworkCollection> testCollections=mongoTemplate.find(query,StudentHomeworkCollection.class);
        TestScoreModel tsm=new TestScoreModel();
        Double maxScore=0.0,minScor=0.0,sum=0.0;
        int size=0;//记录集合长度
        if (testCollections.size()!=0) {
            for (StudentHomeworkCollection test : testCollections) {
                size++;
                sum = sum + test.getHomeworks().get(0).getTotalScore();
                maxScore = testCollections.get(0).getHomeworks().get(0).getTotalScore();
                if (size < testCollections.size()) {//判断是否是最后一个元素
                    continue;
                }
                minScor = test.getHomeworks().get(0).getTotalScore();
                tsm = new TestScoreModel(tests.getName(), maxScore, minScor, sum / size, size);
            }
        }else {
            tsm = new TestScoreModel(tests.getName(), maxScore, minScor, 0.0, size);
        }
        return tsm;
    }
}
