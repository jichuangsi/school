package com.jichuangsi.school.courseservice.repository;

import com.jichuangsi.school.courseservice.entity.TeacherAnswer;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface TeacherAnswerRepository extends MongoRepository<TeacherAnswer, String>, TeacherAnswerExtraRepository {

    TeacherAnswer findFirstByTeacherIdAndQuestionIdAndStudentAnswerIdOrderByUpdateTimeDesc(String teacherId, String questinoId, String studentAnswerId);
}
