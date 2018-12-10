package com.jichuangsi.school.questionsrepository.repository;

import com.jichuangsi.microservice.common.model.UserInfoForToken;
import com.jichuangsi.school.questionsrepository.entity.FavorQuestions;
import com.jichuangsi.school.questionsrepository.model.common.DeleteQueryModel;
import com.jichuangsi.school.questionsrepository.model.common.SearchQuestionModel;

import java.util.List;

public interface IFavorQuestionsRepository<T> {
    T save(T entity);

    List<FavorQuestions> selectPageFavorQ(UserInfoForToken userInfoForToken, SearchQuestionModel searchQuestionModel);

    long getFavorQuestionCount(UserInfoForToken userInfoForToken, SearchQuestionModel searchQuestionModel);

    void findAllAndRemove(DeleteQueryModel deleteQueryModel);

    FavorQuestions findFavorByMD52(String teacherId,String MD52);
}
