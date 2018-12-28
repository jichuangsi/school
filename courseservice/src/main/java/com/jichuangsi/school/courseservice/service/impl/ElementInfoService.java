package com.jichuangsi.school.courseservice.service.impl;

import com.jichuangsi.school.courseservice.constant.QuestionType;
import com.jichuangsi.school.courseservice.entity.elements.QuestionTypeElement;
import com.jichuangsi.school.courseservice.service.IElementInfoService;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ElementInfoService implements IElementInfoService {

    private final Map<String, Integer> QUESTION_TYPE = new HashMap<String, Integer>();

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
    public QuestionType fetchQuestionType(String type){
        if(StringUtils.isEmpty(type)) return QuestionType.EMPTY;
        return QuestionType.getResult(QUESTION_TYPE.get(type)==null?0:QUESTION_TYPE.get(type));
    }
}
