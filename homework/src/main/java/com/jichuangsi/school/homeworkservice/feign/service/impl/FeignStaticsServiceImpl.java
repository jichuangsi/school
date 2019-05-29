package com.jichuangsi.school.homeworkservice.feign.service.impl;

import com.jichuangsi.school.homeworkservice.entity.Question;
import com.jichuangsi.school.homeworkservice.entity.StudentAnswer;
import com.jichuangsi.school.homeworkservice.feign.service.FeignStaticsService;
import com.jichuangsi.school.homeworkservice.repository.QuestionRepository;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
@Service
public class FeignStaticsServiceImpl implements FeignStaticsService {
    @Resource
    private MongoTemplate mongoTemplate;
    @Resource
    private QuestionRepository questionRepository;

    @Override
    public List<Question> getQuestionBySubjectId(String subjectId) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.MONTH,-1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        return questionRepository.findBySubjectIdAndCreateTimeGreaterThan
                (subjectId,calendar.getTimeInMillis());
    }

    @Override
    public List<Question> getQuestion(List<String> questionId) {

        return mongoTemplate.find(new Query(Criteria.where("id").in(questionId)),Question.class);
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

}
