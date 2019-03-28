package com.jichuangsi.school.courseservice.repository;

import com.jichuangsi.school.courseservice.entity.Question;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuestionRepository extends MongoRepository<Question, String>, QuestionExtraRepository{

    List<Question> findByIdIn(List<String> questionIds);

    List<Question> findByGradeIdAndIdMD52In(String gradeId,List<String> MD52);
}
