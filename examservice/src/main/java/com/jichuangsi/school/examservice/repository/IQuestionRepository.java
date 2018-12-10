package com.jichuangsi.school.examservice.repository;

import com.jichuangsi.school.examservice.entity.Question;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IQuestionRepository extends MongoRepository<Question,String>, IQuestionExtraRepository {

    List<Question> findByExamId(String id);


}
