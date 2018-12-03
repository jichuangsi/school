package com.jichuangsi.school.questionsrepository.repository;

import com.jichuangsi.school.questionsrepository.entity.QuestionStatistics;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QuestionStatisticsRepository extends MongoRepository<QuestionStatistics,String> {
    QuestionStatistics findOneByQidMD52(String id);
}
