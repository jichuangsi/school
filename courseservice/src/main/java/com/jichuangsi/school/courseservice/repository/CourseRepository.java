package com.jichuangsi.school.courseservice.repository;

import com.jichuangsi.school.courseservice.entity.Course;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CourseRepository extends MongoRepository<Course, String>{

}
