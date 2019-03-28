package com.jichuangsi.school.testservice.repository;

import com.jichuangsi.school.testservice.entity.Test;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TestRepository extends MongoRepository<Test, String>, StudentTestExtraRepository{

    Test findFirstByIdOrderByUpdateTimeDesc(String testId);
}
