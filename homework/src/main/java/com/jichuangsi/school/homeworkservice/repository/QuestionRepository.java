package com.jichuangsi.school.homeworkservice.repository;

import com.jichuangsi.school.homeworkservice.entity.Question;
import org.springframework.data.domain.Example;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuestionRepository extends MongoRepository<Question, String>, QuestionExtraRepository{

    Question findFirstById(String questionId);

    //List<Question> findOnMonthQuestionBySubjectIdAndCreateTimeIsAfter(String subjectId,Long createTime);

    List<Question> findBySubjectIdAndCreateTimeGreaterThan(String subjectId,Long createTime);

}
