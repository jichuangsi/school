package com.jichuangsi.school.courseservice.aop;

import com.jichuangsi.school.courseservice.constant.QuestionType;
import com.jichuangsi.school.courseservice.entity.Question;
import com.jichuangsi.school.courseservice.entity.elements.AnswerMapping;
import com.jichuangsi.school.courseservice.entity.elements.QuestionMappingElement;
import com.jichuangsi.school.courseservice.service.IElementInfoService;
import org.springframework.util.StringUtils;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.Optional;

@Aspect
@Component
public class RepositoryAop {

    @Resource
    private IElementInfoService elementInfoService;

    private final static int SINGLE_OPTION_QUESTION_INDEX = 0;

    private final static int MULTIPLE_OPTION_QUESTION_INDEX = 1;

    private final static int SUBJECTIVE_QUESTION_INDEX = 2;

    @Before("execution(* com.jichuangsi.school.courseservice.repository.CourseConsoleRepository.save(..)) && args(question, points)")
    public void transferQuestions(Question question, List<String> points){
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

        if(points.size()>0){
            if(QuestionType.OBJECTIVE.getName().equalsIgnoreCase(question.getType())){//客观题
                if(question.getAnswer().length() > 1 && !StringUtils.isEmpty(points.get(MULTIPLE_OPTION_QUESTION_INDEX))){//多选题
                    question.setPoint(points.get(MULTIPLE_OPTION_QUESTION_INDEX));
                }else if(question.getAnswer().length() == 1 && !StringUtils.isEmpty(points.get(SINGLE_OPTION_QUESTION_INDEX))){//单选题
                    question.setPoint(points.get(SINGLE_OPTION_QUESTION_INDEX));
                }
            }else if(QuestionType.SUBJECTIVE.getName().equalsIgnoreCase(question.getType())){//主观题
                if(!StringUtils.isEmpty(points.get(SUBJECTIVE_QUESTION_INDEX))){
                    question.setPoint(points.get(SUBJECTIVE_QUESTION_INDEX));
                }
            }
        }
    }
}
