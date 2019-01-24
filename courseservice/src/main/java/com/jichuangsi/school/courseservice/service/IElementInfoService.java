package com.jichuangsi.school.courseservice.service;

import com.jichuangsi.school.courseservice.constant.QuestionType;
import com.jichuangsi.school.courseservice.entity.elements.QuestionMappingElement;

public interface IElementInfoService {

    void initQuestionType();

    void initQuestionMapping();

    QuestionType fetchQuestionType(String type);

    QuestionMappingElement fetchQuestionMapping(String type);
}
