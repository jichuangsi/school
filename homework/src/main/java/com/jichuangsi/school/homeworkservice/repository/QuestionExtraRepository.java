package com.jichuangsi.school.homeworkservice.repository;

import com.jichuangsi.school.homeworkservice.entity.Question;

import java.util.List;

public interface QuestionExtraRepository {

    List<Question> findQuestionsByHomeworkId(String homeworkId);

    Question save(Question entity, List<String> points) ;
}
