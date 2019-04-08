package com.jichuangsi.school.homeworkservice.service;


import com.jichuangsi.school.homeworkservice.constant.Result;
import com.jichuangsi.school.homeworkservice.entity.Question;

public interface IAutoVerifyAnswerService {

    Result verifyObjectiveAnswer(Question question, String answer);
}
