package com.jichuangsi.school.testservice.service.impl;

import com.jichuangsi.school.testservice.constant.QuestionType;
import com.jichuangsi.school.testservice.entity.elements.QuestionMappingElement;
import com.jichuangsi.school.testservice.entity.elements.QuestionTypeElement;
import com.jichuangsi.school.testservice.service.IElementInfoService;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.*;

@Service
public class ElementInfoServiceImpl implements IElementInfoService {

    private final Map<String, Integer> QUESTION_TYPE = Collections.synchronizedMap(new HashMap<String, Integer>());

    private final Map<String, QuestionMappingElement> QUESTION_MAPPING = Collections.synchronizedMap(new HashMap<String, QuestionMappingElement>());

    @Resource
    private MongoTemplate mongoTemplate;

    @Override
    public void initQuestionType(){
        List<QuestionTypeElement> questionTypeElementList = mongoTemplate.findAll(QuestionTypeElement.class);
        questionTypeElementList.forEach(q -> {
            QUESTION_TYPE.put(q.getType(),q.getIndex());
        });
    }

    @Override
    public void initQuestionMapping(){
        List<QuestionMappingElement> questionMappingElementList = mongoTemplate.findAll(QuestionMappingElement.class);
        questionMappingElementList.forEach(q->{
            QUESTION_MAPPING.put(q.getType(),q);
        });
    }

    @Override
    public QuestionType fetchQuestionType(String type){
        if(StringUtils.isEmpty(type)) return QuestionType.EMPTY;
        return QuestionType.getResult(QUESTION_TYPE.get(type)==null?0:QUESTION_TYPE.get(type));
    }

    @Override
    public QuestionMappingElement fetchQuestionMapping(String type){
        if(StringUtils.isEmpty(type)) return null;
        return QUESTION_MAPPING.get(type);
    }
}
