package com.jichuangsi.school.testservice.service;


import com.jichuangsi.school.testservice.constant.Result;
import com.jichuangsi.school.testservice.entity.Question;

public interface IAutoVerifyAnswerService {

    Result verifyObjectiveAnswer(Question question, String answer);
}
