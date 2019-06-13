package com.jichuangsi.school.homeworkservice.feign.service;

import com.jichuangsi.microservice.common.model.ResponseModel;
import com.jichuangsi.school.homeworkservice.entity.Question;
import com.jichuangsi.school.homeworkservice.entity.StudentAnswer;

import java.util.List;

public interface FeignStaticsService {

    public List<Question> getQuestionBySubjectId(String subjectId);

    public Question getQuestion(String questionId);

    public List<StudentAnswer> getQuestionResult(List<String> questionId);

    public List<StudentAnswer> getQuestionByStudentId(String studentId);

    public Question getQuestionKnowledges(String questionId);

}