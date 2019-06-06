package com.jichuangsi.school.homeworkservice.service.impl;

import com.jichuangsi.school.homeworkservice.constant.Result;
import com.jichuangsi.school.homeworkservice.entity.Question;
import com.jichuangsi.school.homeworkservice.service.IAutoVerifyAnswerService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

//import java.util.regex.Matcher;
//import java.util.regex.Pattern;

@Service
public class AutoVerifyAnswerServiceImpl implements IAutoVerifyAnswerService {

    @Value("${com.jichuangsi.school.question.answer.split}")
    private String answerSplit;

    @Override
    public Result verifyObjectiveAnswer(Question question, String answer) {
        String questionAnswer = question.getAnswer();
       /* Matcher m= Pattern.compile(".*([a-zA-Z]\\|)+.*").matcher(questionAnswer);
        boolean tea = m.matches();*/
        if(!questionAnswer.contains(answerSplit)){//正确答案没有以'|'分割
            answer = answer.replace(answerSplit, "");
        }
        if(answer.trim().equalsIgnoreCase(questionAnswer))
            return Result.CORRECT;
        return Result.WRONG;
    }
}
