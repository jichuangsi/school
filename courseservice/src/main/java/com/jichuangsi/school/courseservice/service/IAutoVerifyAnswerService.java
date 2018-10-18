package com.jichuangsi.school.courseservice.service;

import com.jichuangsi.school.courseservice.constant.Result;
import com.jichuangsi.school.courseservice.entity.Question;

public interface IAutoVerifyAnswerService {

    Result verifyObjectiveAnswer(Question question, String answer);
}
