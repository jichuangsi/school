package com.jichuangsi.school.statistics.repository;

import com.jichuangsi.school.statistics.entity.StudentStatisticsEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StudentStatisticsRepository extends MongoRepository<StudentStatisticsEntity, String> {

}
