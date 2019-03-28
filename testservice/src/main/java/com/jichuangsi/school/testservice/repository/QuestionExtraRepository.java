package com.jichuangsi.school.testservice.repository;

import com.jichuangsi.school.testservice.entity.Question;

import java.util.List;

public interface QuestionExtraRepository {

    List<Question> findQuestionsByTestId(String testId);
}
