package com.jichuangsi.school.courseservice.service.impl;

import com.jichuangsi.school.courseservice.model.PageHolder;
import com.jichuangsi.school.courseservice.model.repository.QuestionNode;
import com.jichuangsi.school.courseservice.model.repository.QuestionQueryModel;
import com.jichuangsi.school.courseservice.service.IQuestionInfoService;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.ArrayList;
import java.util.List;

@Component
public class QuestionInfoServiceFallBack implements IQuestionInfoService {

    @Override
    public PageHolder<QuestionNode> getQuestionsByKnowledge(@RequestBody QuestionQueryModel questionQueryModel){
        return null;
    }
}
