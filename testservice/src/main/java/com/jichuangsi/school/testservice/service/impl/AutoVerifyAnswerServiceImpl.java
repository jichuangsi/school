package com.jichuangsi.school.testservice.service.impl;

import com.jichuangsi.school.testservice.constant.Result;
import com.jichuangsi.school.testservice.entity.Question;
import com.jichuangsi.school.testservice.service.IAutoVerifyAnswerService;
import org.springframework.stereotype.Service;

@Service
public class AutoVerifyAnswerServiceImpl implements IAutoVerifyAnswerService {

    @Override
    public Result verifyObjectiveAnswer(Question question, String answer) {
        if(answer.trim().equalsIgnoreCase(question.getAnswer()))
            return Result.CORRECT;
        return Result.WRONG;
    }
}
