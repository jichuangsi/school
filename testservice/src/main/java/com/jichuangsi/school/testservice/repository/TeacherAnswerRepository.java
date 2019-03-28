package com.jichuangsi.school.testservice.repository;

import com.jichuangsi.school.testservice.entity.TeacherAnswer;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface TeacherAnswerRepository extends MongoRepository<TeacherAnswer, String> {

    TeacherAnswer findFirstByQuestionIdAndStudentAnswerIdOrderByUpdateTimeDesc(String questionId, String studentAnswerId);

    TeacherAnswer findFirstByTeacherIdAndQuestionIdAndStudentAnswerIdOrderByUpdateTimeDesc(String teacherId, String questionId, String studentAnswerId);

    TeacherAnswer findFirstByTeacherIdAndQuestionIdAndIsShareOrderByShareTimeDesc(String teacherId, String questionId, boolean isShare);
}
