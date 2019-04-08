package com.jichuangsi.school.homeworkservice.service;

import com.jichuangsi.school.homeworkservice.constant.QuestionType;
import com.jichuangsi.school.homeworkservice.entity.elements.QuestionMappingElement;

public interface IElementInfoService {

    void initQuestionType();

    void initQuestionMapping();

    QuestionType fetchQuestionType(String type);

    QuestionMappingElement fetchQuestionMapping(String type);
}
