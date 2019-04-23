package com.jichuangsi.school.courseservice.repository;

import com.jichuangsi.school.courseservice.entity.Course;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CourseRepository extends MongoRepository<Course, String>, CourseExtraRepository{

    List<Course> findAllByTeacherId(String teacherId);

    Course findFirstByIdAndTeacherIdOrderByUpdateTimeDesc(String courseId, String teacherId);

    Course findFirstByIdAndClassIdOrderByUpdateTimeDesc(String courseId, String classId);

    List<Course> findByClassIdAndStatus(String classId, String status);

    List<Course> findByTeacherIdAndStatus(String teacherId, String status);

    Course findFirstByIdAndTeacherId(String id,String teacherId);

    List<Course> findByClassIdAndStatusAndEndTimeGreaterThanAndTeacherIdAndSubjectNameLikeOrderByCreateTime(String classId,String status,long endTime,String teacherId,String subjectName);

    List<Course> findByClassIdAndStatusAndEndTimeGreaterThanAndEndTimeLessThan(String classId,String status,long beignTime,long endTime);

    List<Course> findByClassIdAndStatusAndEndTimeGreaterThanAndSubjectNameLikeOrderByCreateTime(String classId,String status,long endTime,String subjectName);

    List<Course> findByClassIdAndStatusAndEndTimeGreaterThanAndEndTimeLessThanAndSubjectNameLike(String classId,String status,long beignTime,long endTime,String subjectName);
}
