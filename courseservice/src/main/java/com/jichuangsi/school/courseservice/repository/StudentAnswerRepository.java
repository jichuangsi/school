package com.jichuangsi.school.courseservice.repository;

import com.jichuangsi.school.courseservice.entity.StudentAnswer;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface StudentAnswerRepository extends MongoRepository<StudentAnswer, String>, StudentAnswerExtraRepository {

    List<StudentAnswer> findAllByQuestionId(String questionId);

    StudentAnswer findFirstByQuestionIdAndStudentIdOrderByUpdateTimeDesc(String questionId, String studentId);

    List<StudentAnswer> findAllByStudentIdAndResult(String studentId, String result);
}
