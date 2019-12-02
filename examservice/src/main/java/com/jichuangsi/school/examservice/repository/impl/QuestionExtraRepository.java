package com.jichuangsi.school.examservice.repository.impl;

import com.jichuangsi.school.examservice.Model.ExamModel;
import com.jichuangsi.school.examservice.entity.DimensionQuestion;
import com.jichuangsi.school.examservice.entity.ExamDimension;
import com.jichuangsi.school.examservice.entity.Question;
import com.jichuangsi.school.examservice.repository.IExamDimensionRepository;
import com.jichuangsi.school.examservice.repository.IQuestionExtraRepository;
import com.jichuangsi.school.examservice.repository.IQuestionRepository;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.List;

@Repository
public class QuestionExtraRepository implements IQuestionExtraRepository {

    @Resource
    private MongoTemplate mongoTemplate;

    @Resource
    private IQuestionRepository questionRepository;
    @Resource
    private IExamDimensionRepository dimensionRepository;

    @Override
    public void deleteQuestionByExamId(String eid) {
        Criteria criteria = Criteria.where("examId").is(eid);
        mongoTemplate.findAllAndRemove(new Query(criteria), Question.class);
    }

    @Override
    public List<Question> findPageQuestions(ExamModel examModel) {
        Criteria criteria = Criteria.where("examId").is(examModel.getExamId());
        Query query = new Query(criteria).skip((examModel.getPageIndex()-1)
                *examModel.getPageSize()).limit(examModel.getPageSize());
        return mongoTemplate.find(query,Question.class);
    }

    @Override
    public List<Question> findQuestionsByExamId(String examId) {
        Criteria criteria = Criteria.where("examId").is(examId);
        return mongoTemplate.find(new Query(criteria),Question.class);
    }

    @Override
    public long findCountByExamId(String eid) {
        Criteria criteria = Criteria.where("examId").is(eid);
        return mongoTemplate.count(new Query(criteria),Question.class);
    }

    @Override
    public Question save(Question question) {
        return questionRepository.save(question);
    }

    @Override
    public ExamDimension save(ExamDimension question) {
        return dimensionRepository.save(question);
    }
    @Override
    public List<DimensionQuestion> findDimensionQuestionByExamId(String examId) {
        Criteria criteria = Criteria.where("examId").is(examId);
        return mongoTemplate.find(new Query(criteria),DimensionQuestion.class);
    }
    @Override
    public List<DimensionQuestion> findPageDimensionQuestion(ExamModel examModel) {
        Criteria criteria = Criteria.where("examId").is(examModel.getExamId());
        Query query = new Query(criteria).skip((examModel.getPageIndex()-1)
            *examModel.getPageSize()).limit(examModel.getPageSize());
        return mongoTemplate.find(query,DimensionQuestion.class);
    }

    @Override
    public void deleteDimensionQuestionByExamId(String eid) {
        Criteria criteria = Criteria.where("examId").is(eid);
        mongoTemplate.findAllAndRemove(new Query(criteria), DimensionQuestion.class);
    }

    @Override
    public long findCountByDimensionExamId(String eid) {
        Criteria criteria = Criteria.where("examId").is(eid);
        return mongoTemplate.count(new Query(criteria),DimensionQuestion.class);
    }
}
