package com.jichuangsi.school.courseservice.repository;

import com.jichuangsi.school.courseservice.entity.Question;

import java.util.List;

public interface StudentAnswerExtraRepository {

    List<Question> findAllBySubjectIdAndKnowledgeIdAndStudentIdAndResult(String subjectId, String knowledgeId, String studentId, String result);
}
