package com.jichuangsi.school.questionsrepository.repository;

import com.jichuangsi.microservice.common.model.UserInfoForToken;
import com.jichuangsi.school.questionsrepository.entity.SelfQuestions;
import com.jichuangsi.school.questionsrepository.model.common.DeleteQueryModel;
import com.jichuangsi.school.questionsrepository.model.common.SearchQuestionModel;

import java.util.List;

public interface ISelfQuestionsRepository<T> {
    T save(T entity);

    long getSelfQuestionCount(UserInfoForToken userInfoForToken, SearchQuestionModel searchQuestionModel);

    List<SelfQuestions> selectPageSelfQ(UserInfoForToken userInfoForToken, SearchQuestionModel searchQuestionModel);

    SelfQuestions findParticularQuesitonById(String userId, String questionId);

    void findAllAndRemove(DeleteQueryModel deleteQueryModel);
}
