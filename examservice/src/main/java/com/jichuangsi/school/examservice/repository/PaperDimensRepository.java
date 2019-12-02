package com.jichuangsi.school.examservice.repository;

import com.jichuangsi.school.examservice.entity.PaperDimension;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PaperDimensRepository extends MongoRepository<PaperDimension,String> {

    List<PaperDimension> findByYear(String year);//根据年查询年级

    List<PaperDimension> findByGradeAndYear(String grade,String year);//根据年级查询科目

    List<PaperDimension> findBySubjectAndGradeAndYear(String subjiect,String grade,String year);//根据科目查询类型

   /* PaperDimension findById(String examid);//根据类型查询题目*/
}
