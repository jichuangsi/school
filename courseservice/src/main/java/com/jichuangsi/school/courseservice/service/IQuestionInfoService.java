package com.jichuangsi.school.courseservice.service;

import com.jichuangsi.school.courseservice.model.PageHolder;
import com.jichuangsi.school.courseservice.model.repository.QuestionNode;
import com.jichuangsi.school.courseservice.model.repository.QuestionQueryModel;
import com.jichuangsi.school.courseservice.service.impl.QuestionInfoServiceFallBack;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@FeignClient(name = "questionsrepository", fallback = QuestionInfoServiceFallBack.class)
public interface IQuestionInfoService {

    @RequestMapping("/feign/getQuestionsByKnowledge")
    public PageHolder<QuestionNode> getQuestionsByKnowledge(@RequestBody QuestionQueryModel questionQueryModel);

}
