package com.jichuangsi.school.homeworkservice.repository;

import com.jichuangsi.school.homeworkservice.entity.Question;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QuestionRepository extends MongoRepository<Question, String>{

}
