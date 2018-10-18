package com.jichuangsi.school.courseservice.repository;

import com.jichuangsi.school.courseservice.entity.Course;
import com.jichuangsi.school.courseservice.entity.Question;

import java.util.List;

public interface CourseExtraRepository{

    List<Course> findCourseByTeacherIdAndStatus(String teacherId);

    List<Course> findHistoryCourseByTeacherIdAndStatus(String teacherId);

    List<Course> findCourseByTeacherIdAndConditions(String teacherId, Course course);

}
