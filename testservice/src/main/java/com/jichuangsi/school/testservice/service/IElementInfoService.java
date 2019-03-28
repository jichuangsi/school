package com.jichuangsi.school.testservice.service;

import com.jichuangsi.school.testservice.constant.QuestionType;
import com.jichuangsi.school.testservice.entity.elements.QuestionMappingElement;

public interface IElementInfoService {

    void initQuestionType();

    void initQuestionMapping();

    QuestionType fetchQuestionType(String type);

    QuestionMappingElement fetchQuestionMapping(String type);
}
