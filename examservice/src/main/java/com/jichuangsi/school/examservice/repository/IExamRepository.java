package com.jichuangsi.school.examservice.repository;

import com.jichuangsi.school.examservice.entity.Exam;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IExamRepository extends MongoRepository<Exam,String> {
    Exam findOneByExamId(String id);

    List<Exam> findByTeacherId(String id);

    void deleteExamsByExamIdIsIn(List<String> eids);
}
