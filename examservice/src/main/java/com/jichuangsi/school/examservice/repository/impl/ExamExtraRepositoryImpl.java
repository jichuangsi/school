package com.jichuangsi.school.examservice.repository.impl;

import com.jichuangsi.school.examservice.entity.DimensionQuestion;
import com.jichuangsi.school.examservice.entity.Exam;
import com.jichuangsi.school.examservice.entity.ExamDimension;
import com.jichuangsi.school.examservice.entity.Question;
import com.jichuangsi.school.examservice.repository.IExamDimensionRepository;
import com.jichuangsi.school.examservice.repository.IExamExtraRepository;
import com.jichuangsi.school.examservice.repository.IExamRepository;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.GroupOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

@Repository
public class ExamExtraRepositoryImpl implements IExamExtraRepository {
    @Resource
    private MongoTemplate mongoTemplate;

    @Resource
    private IExamRepository examRepository;
    @Resource
    IExamDimensionRepository dimensionRepository;
   /* @Override
    public void deleteEaxmsByEaxmId(List<String> eids) {
        Criteria criteria = Criteria.where("eaxmId").in(eids);
        mongoTemplate.remove(new Query(criteria), Eaxm.class);
    }*/

    @Override
    public List<Exam> findExamByExamNameAndConditions(String userId, String keyword,Integer pageSize,Integer pageIndex) {
        Criteria criteria = new Criteria("teacherId").is(userId);
        if(!StringUtils.isEmpty(keyword)){
            Pattern pattern= Pattern.compile("^.*"+keyword+".*$", Pattern.CASE_INSENSITIVE);
            Criteria    c1 = Criteria.where("examName").regex(pattern);
            Criteria    c2 = Criteria.where("examSecondName").regex(pattern);
            criteria.orOperator(c1,c2);
        }
        Query query = new Query(criteria);
        query.limit(pageSize);
        query.skip((pageIndex-1)*pageSize);
        return mongoTemplate.find(query,Exam.class);
    }

    @Override
    public long countByExamNameLike(String userId, String keyword) {
        Criteria criteria = new Criteria("teacherId").is(userId);
        if (!StringUtils.isEmpty(keyword)){
        Pattern pattern= Pattern.compile("^.*"+keyword+".*$", Pattern.CASE_INSENSITIVE);
        criteria.andOperator(Criteria.where("examName").regex(pattern)) ;}
        return mongoTemplate.count(new Query(criteria),Exam.class);
    }

    @Override
    public Exam save(Exam exam) {

        return examRepository.save(exam);
    }

    @Override
    public void deleteExamsByExamIdIsIn(List<String> eids) {
        examRepository.deleteExamsByExamIdIsIn(eids);
    }

    @Override
    public Exam findOneByExamId(String eid) {
        return examRepository.findOneByExamId(eid);
    }

    @Override
    public List<Exam> findByTeacherId(String tid) {
        return examRepository.findByTeacherId(tid);
    }

    @Override
    public List<Map<String,Object>> getGroupCount(String fields,String eid) {
        Aggregation agg = null;
        GroupOperation group = Aggregation.group(fields).count().as("num");
        Criteria criteria = Criteria.where("examId").is(eid);
        agg = Aggregation.newAggregation(Question.class,Aggregation.match(criteria),group);
        AggregationResults<Map> results = mongoTemplate.aggregate(agg,Question.class,Map.class);
        List<Map<String,Object>> maps = new ArrayList<Map<String,Object>>();
        results.forEach(map -> {
            Map<String,Object> m = new HashMap<String,Object>();
            m.put(fields,map.get("_id"));
            m.put("num",map.get("num"));
            maps.add(m);
        });
        return maps;
    }



    @Override
    public List<ExamDimension> findExamDimensionByExamNameAndConditions(String userId, String keyword, Integer pageSize, Integer pageIndex) {
        Criteria criteria = new Criteria();
        if(!StringUtils.isEmpty(keyword)){
            Pattern pattern= Pattern.compile("^.*"+keyword+".*$", Pattern.CASE_INSENSITIVE);
            Criteria    c1 = Criteria.where("examName").regex(pattern);
            Criteria    c2 = Criteria.where("examSecondName").regex(pattern);
            criteria.orOperator(c1,c2);
        }
        Query query = new Query(criteria);
        query.limit(pageSize);
        query.skip((pageIndex-1)*pageSize);
        List<ExamDimension> ll=mongoTemplate.find(query,ExamDimension.class);
        return mongoTemplate.find(query,ExamDimension.class);
    }

    @Override
    public ExamDimension save(ExamDimension exam) {
        return dimensionRepository.save(exam);
    }

    @Override
    public long countByExamDimensionNameLike(String userId, String keyword) {
        Criteria criteria = new Criteria();
        if (!StringUtils.isEmpty(keyword)){
            Pattern pattern= Pattern.compile("^.*"+keyword+".*$", Pattern.CASE_INSENSITIVE);
            criteria.andOperator(Criteria.where("examName").regex(pattern)) ;}
        return mongoTemplate.count(new Query(criteria),ExamDimension.class);
    }

    @Override
    public void deleteExamDimensionByExamIdIsIn(List<String> eids) {
        dimensionRepository.deleteByExamIdIsIn(eids);
    }

    @Override
    public List<Map<String,Object>> getGroupCounts(String fields,String eid) {
        Aggregation agg = null;
        GroupOperation group = Aggregation.group(fields).count().as("num");
        Criteria criteria = Criteria.where("examId").is(eid);
        agg = Aggregation.newAggregation(DimensionQuestion.class,Aggregation.match(criteria),group);
        AggregationResults<Map> results = mongoTemplate.aggregate(agg,DimensionQuestion.class,Map.class);
        List<Map<String,Object>> maps = new ArrayList<Map<String,Object>>();
        results.forEach(map -> {
            Map<String,Object> m = new HashMap<String,Object>();
            m.put(fields,map.get("_id"));
            m.put("num",map.get("num"));
            maps.add(m);
        });
        return maps;
    }

    @Override
    public List<ExamDimension> findExamDimensionByExamIdIsInAndConditions(List<String> examId, Integer pageSize, Integer pageIndex) {
          Query query = new Query(Criteria.where("examId").in(examId));
        query.limit(pageSize);//
        query.skip((pageIndex-1)*pageSize);
        return mongoTemplate.find(query,ExamDimension.class);
    }

    @Override
    public long countByExamDimensionExamId( List<String> examId) {
        return mongoTemplate.count(new Query(Criteria.where("examId").in(examId)),ExamDimension.class);
    }
}
