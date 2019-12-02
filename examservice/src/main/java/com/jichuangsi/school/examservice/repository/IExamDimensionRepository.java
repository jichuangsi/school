package com.jichuangsi.school.examservice.repository;

import com.jichuangsi.school.examservice.entity.ExamDimension;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface IExamDimensionRepository extends MongoRepository<ExamDimension,String> {

   void deleteByExamIdIsIn(List<String> eids);

   List<ExamDimension> findByExamId(String examId);

    List<ExamDimension> findByExamIdIsIn(List<String> eid);
}
