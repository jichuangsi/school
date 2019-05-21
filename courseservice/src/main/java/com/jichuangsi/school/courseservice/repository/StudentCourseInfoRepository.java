package com.jichuangsi.school.courseservice.repository;

import com.jichuangsi.school.courseservice.entity.Course;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StudentCourseInfoRepository extends MongoRepository<Course,String> {
    List<Course> findAllByClassIdAndStatus(String id,String status);
}
