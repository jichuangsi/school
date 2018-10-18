package com.jichuangsi.school.courseservice.repository;

import com.jichuangsi.school.courseservice.entity.Question;

import java.util.List;

public interface QuestionExtraRepository {

    List<Question> findQuestionsByTeacherIdAndCourseId(String teacherId, String courseId);
}
