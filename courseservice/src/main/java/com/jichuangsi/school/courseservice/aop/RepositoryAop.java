package com.jichuangsi.school.courseservice.aop;

import com.jichuangsi.school.courseservice.constant.QuestionType;
import com.jichuangsi.school.courseservice.entity.Question;
import com.jichuangsi.school.courseservice.entity.elements.AnswerMapping;
import com.jichuangsi.school.courseservice.entity.elements.QuestionMappingElement;
import com.jichuangsi.school.courseservice.service.IElementInfoService;
import org.apache.commons.lang.StringUtils;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Optional;

@Aspect
@Component
public class RepositoryAop {

    @Resource
    private IElementInfoService elementInfoService;

    @Before("execution(* com.jichuangsi.school.courseservice.repository.CourseConsoleRepository.save(..)) && args(question)")
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
