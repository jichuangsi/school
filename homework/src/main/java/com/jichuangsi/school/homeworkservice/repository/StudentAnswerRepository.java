package com.jichuangsi.school.homeworkservice.repository;

import com.jichuangsi.school.homeworkservice.entity.StudentAnswer;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface StudentAnswerRepository extends MongoRepository<StudentAnswer, String> {

    List<StudentAnswer> findAllByQuestionId(String questionId);

    StudentAnswer findFirstByQuestionIdAndStudentIdOrderByUpdateTimeDesc(String questionId, String studentId);

    List<StudentAnswer> findAllByStudentIdAndResult(String studentId, String result);

    List<StudentAnswer> findByQuestionIdIn(List<String> questionIds);

    List<StudentAnswer> findByQuestionIdInAndStudentId(List<String> questionIds,String studentId);
}
