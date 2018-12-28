package com.jichuangsi.school.courseservice.aop;

import com.jichuangsi.school.courseservice.constant.QuestionType;
import com.jichuangsi.school.courseservice.entity.Question;
import com.jichuangsi.school.courseservice.service.IElementInfoService;
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

    @Before("execution(* com.jichuangsi.school.courseservice.repository.CourseConsoleRepository.save(..)) && args(question)")
    public void transferQuestionType(Question question){
        String type = elementInfoService.fetchQuestionType(question.getType()).getName();
        question.setType(StringUtils.isEmpty(type)? QuestionType.SUBJECTIVE.getName():type);
    }
}
