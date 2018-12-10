package com.jichuangsi.school.examservice.repository.impl;

import com.jichuangsi.school.examservice.entity.Question;
import com.jichuangsi.school.examservice.repository.IQuestionExtraRepository;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;

@Repository
public class QuestionExtraRepository implements IQuestionExtraRepository {

    @Resource
    private MongoTemplate mongoTemplate;

    @Override
    public void deleteQuestionByExamId(String eid) {
        Criteria criteria = Criteria.where("examId").is(eid);
        mongoTemplate.findAllAndRemove(new Query(criteria), Question.class);
    }

}
