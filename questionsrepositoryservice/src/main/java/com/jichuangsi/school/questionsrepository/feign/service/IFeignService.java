package com.jichuangsi.school.questionsrepository.feign.service;

import com.jichuangsi.microservice.common.model.UserInfoForToken;
import com.jichuangsi.school.questionsrepository.exception.QuestionRepositoryServiceException;
import com.jichuangsi.school.questionsrepository.feign.model.SechQuesionModel;
import com.jichuangsi.school.questionsrepository.model.PageHolder;
import com.jichuangsi.school.questionsrepository.model.QuestionNode;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;

public interface IFeignService {
    @Transactional
    PageHolder<QuestionNode> getListForQuestionsBy(@ModelAttribute UserInfoForToken userInfo, @RequestBody SechQuesionModel questionModel)throws QuestionRepositoryServiceException;
}
