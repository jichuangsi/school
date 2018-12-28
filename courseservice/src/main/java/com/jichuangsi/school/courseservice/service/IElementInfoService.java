package com.jichuangsi.school.courseservice.service;

import com.jichuangsi.school.courseservice.constant.QuestionType;

public interface IElementInfoService {

    void initQuestionType();

    QuestionType fetchQuestionType(String type);
}
