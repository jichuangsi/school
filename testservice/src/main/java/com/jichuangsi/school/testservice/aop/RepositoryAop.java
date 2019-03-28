package com.jichuangsi.school.testservice.aop;

import com.jichuangsi.school.testservice.constant.QuestionType;
import com.jichuangsi.school.testservice.entity.Question;
import com.jichuangsi.school.testservice.entity.elements.QuestionMappingElement;
import com.jichuangsi.school.testservice.service.IElementInfoService;
import org.apache.commons.lang.StringUtils;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Aspect
@Component
public class RepositoryAop {

    @Resource
    private IElementInfoService elementInfoService;

    @Before("execution(* com.jichuangsi.school.testservice.repository.QuestionRepository.save(..)) && args(question)")
    public void transferQuestions(Question question){
        QuestionMappingElement questionMappingElement = elementInfoService.fetchQuestionMapping(question.getType());
        if(questionMappingElement!=null){
            if(questionMappingElement.getOptions().size()>0){
                question.getOptions().addAll(questionMappingElement.getOptions());
            }
            if(questionMappingElement.getAnswers().size()>0){
                questionMappingElement.getAnswers().stream()
                        .filter(a -> a.getPre().equalsIgnoreCase(question.getAnswer())|| a.getPre().equalsIgnoreCase(question.getAnswerDetail()))
                        .findFirst().ifPresent((value) -> question.setAnswer(value.getNow()));
            }
        }

        String type = elementInfoService.fetchQuestionType(question.getType()).getName();
        question.setType(StringUtils.isEmpty(type)? QuestionType.SUBJECTIVE.getName():type);
    }
}
