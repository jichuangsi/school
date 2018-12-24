package com.jichuangsi.school.courseservice.repository;

import com.jichuangsi.school.courseservice.entity.Course;
import com.jichuangsi.school.courseservice.model.CourseForTeacher;
import com.jichuangsi.school.courseservice.model.PageHolder;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CourseConsoleRepository<T> {

    //根据开始时间和结束时间判断是否存在时间冲突
    long findCourseByStartTimeAndEndTime(Long startTime, Long endTime, String teacherId);

    //查找排列条件查询课程
    List<Course> findCourseByCourseAndKeyWord(Course course, Integer sort, String keyWord, PageHolder page, Long[] time);

    //查询排列条件全部条数
    long findCourseByCourseAndKeyWordCount(Course course, Integer sort, String keyWord, Long[] time);

    //保存并返回insert
    T save(T entity) ;

    //查询当前用户最新5条未开课
    List<Course> findNewCourse(String teacherId);

    //修改课堂部分信息
    void updateCourseById(Course updatedCourse);

}
