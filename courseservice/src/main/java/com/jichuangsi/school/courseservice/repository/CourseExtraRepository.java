package com.jichuangsi.school.courseservice.repository;

import com.jichuangsi.school.courseservice.entity.Course;

import java.util.List;

public interface CourseExtraRepository{

    List<Course> findCourseByTeacherIdAndStatus(String teacherId);

    List<Course> findHistoryCourseByTeacherIdAndStatus(String teacherId, int pageNum, int pageSize);

    List<Course> findCourseByTeacherIdAndConditions(String teacherId, Course course);

    List<Course> findCourseByClassIdAndStatus(String classId);

    List<Course> findHistoryCourseByClassIdAndStatus(String classId, int pageNum, int pageSize);

    Course findCourseByTeacherIdAndQuestionId(String teacherId, String questionId);

    List<Course> findOneWeekCourseByClassIdAndStatusAndEndTime(String classId,long endTime);



    List<Course> findHistoryCourseByClassIdAndStatusAndEndTimeGreaterThanAndEndTimeLessThan(String classId, int pageNum, int pageSize,long beginTime,long endTime);
}
