package com.jichuangsi.school.testservice.repository;

import com.jichuangsi.school.testservice.entity.Question;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QuestionRepository extends MongoRepository<Question, String>, QuestionExtraRepository{

}
