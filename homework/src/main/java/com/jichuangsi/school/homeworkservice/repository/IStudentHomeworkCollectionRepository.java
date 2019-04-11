package com.jichuangsi.school.homeworkservice.repository;

import com.jichuangsi.school.homeworkservice.entity.StudentHomeworkCollection;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface IStudentHomeworkCollectionRepository extends MongoRepository<StudentHomeworkCollection,String> {

    StudentHomeworkCollection findFirstByStudentId(String studentId);
}
