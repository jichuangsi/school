package com.jichuangsi.school.courseservice.repository;

import com.jichuangsi.school.courseservice.entity.Question;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QuestionRepository extends MongoRepository<Question, String>, QuestionExtraRepository{

}
