package com.jichuangsi.school.examservice.repository;

import com.jichuangsi.school.examservice.entity.Question;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IQuestionRepository extends MongoRepository<Question,String> {

    int countByExamId(String id);


}
