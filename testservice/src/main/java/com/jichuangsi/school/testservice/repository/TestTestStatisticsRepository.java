package com.jichuangsi.school.testservice.repository;

import com.jichuangsi.school.testservice.entity.Test;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface TestTestStatisticsRepository {

    List<Test> findByClassIdAndStatusAndEndTimeGreaterThanAndEndTimeLessThanAndSubjectNameLike(String classId, String status, long beginTime, long endTime, String subjectName);

}
