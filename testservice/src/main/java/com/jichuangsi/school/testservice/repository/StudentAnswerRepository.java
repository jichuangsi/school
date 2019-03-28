package com.jichuangsi.school.testservice.repository;

import com.jichuangsi.school.testservice.entity.StudentAnswer;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface StudentAnswerRepository extends MongoRepository<StudentAnswer, String> {

    List<StudentAnswer> findAllByQuestionId(String questionId);

    StudentAnswer findFirstByQuestionIdAndStudentIdOrderByUpdateTimeDesc(String questionId, String studentId);

    List<StudentAnswer> findAllByStudentIdAndResult(String studentId, String result);
}
