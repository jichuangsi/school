package com.jichuangsi.school.courseservice.service;

import com.jichuangsi.school.courseservice.Exception.TeacherCourseServiceException;
import com.jichuangsi.school.courseservice.model.CourseForTeacher;
import com.jichuangsi.school.courseservice.model.QuestionForTeacher;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface ITeacherCourseService {
    @Transactional
    CourseForTeacher saveCourse(CourseForTeacher course);

    @Transactional
    void deleteCourse(CourseForTeacher course);

    @Transactional
    List<QuestionForTeacher> saveQuestions(String courseId, List<QuestionForTeacher> questions) throws TeacherCourseServiceException;

    @Transactional
    void startCourse(CourseForTeacher course) throws TeacherCourseServiceException;
}
