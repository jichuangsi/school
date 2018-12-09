package com.jichuangsi.school.eaxmservice.repository;

import com.jichuangsi.school.eaxmservice.entity.Question;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IQuestionRepository extends MongoRepository<Question,String>,IQuestionExtraRepository{

    List<Question> findByEaxmId(String id);


}
