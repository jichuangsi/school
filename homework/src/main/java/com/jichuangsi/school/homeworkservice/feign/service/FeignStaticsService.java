package com.jichuangsi.school.homeworkservice.feign.service;

import com.jichuangsi.school.homeworkservice.entity.Homework;
import com.jichuangsi.school.homeworkservice.entity.Question;
import com.jichuangsi.school.homeworkservice.entity.StudentAnswer;
import com.jichuangsi.school.homeworkservice.entity.StudentHomeworkCollection;
import com.jichuangsi.school.homeworkservice.model.Report.HomeworkKnoledge;
import com.jichuangsi.school.homeworkservice.model.Report.TestScoreModel;

import java.util.List;

public interface FeignStaticsService {

    public List<Question> getQuestionBySubjectId(String subjectId);

    public Question getQuestion(String questionId);

    public List<StudentAnswer> getQuestionResult(List<String> questionId);

    public List<StudentAnswer> getQuestionByStudentId(String studentId);

    public Question getQuestionKnowledges(String questionId);

    public Homework getSubjectIdByHomeworkId(String homeworkId,String classId);

    public List<StudentHomeworkCollection> getTotalScoreByHomeworkId(String homeworkId);

    public HomeworkKnoledge getHomeworkByHomeworkId(String homeworkId);

    List<Homework> getHomeworkBySubjectNameAndHomeworkId(List<String> classId, String subjectId, long time);

    TestScoreModel getHomeworkById(String HomeworkId);
}