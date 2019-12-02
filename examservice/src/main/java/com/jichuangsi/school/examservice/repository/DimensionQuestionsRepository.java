package com.jichuangsi.school.examservice.repository;

import com.jichuangsi.school.examservice.entity.DimensionQuestion;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DimensionQuestionsRepository extends MongoRepository<DimensionQuestion,String> {

}
