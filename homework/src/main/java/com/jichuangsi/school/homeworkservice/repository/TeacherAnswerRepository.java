package com.jichuangsi.school.homeworkservice.repository;

import com.jichuangsi.school.homeworkservice.entity.TeacherAnswer;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface TeacherAnswerRepository extends MongoRepository<TeacherAnswer, String> {

    TeacherAnswer findFirstByQuestionIdAndStudentAnswerIdOrderByUpdateTimeDesc(String questionId, String studentAnswerId);

    TeacherAnswer findFirstByTeacherIdAndQuestionIdAndStudentAnswerIdOrderByUpdateTimeDesc(String teacherId, String questionId, String studentAnswerId);

    TeacherAnswer findFirstByTeacherIdAndQuestionIdAndIsShareOrderByShareTimeDesc(String teacherId, String questionId, boolean isShare);
}
