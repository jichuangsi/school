package com.jichuangsi.school.questionsrepository.service;

import com.jichuangsi.microservice.common.model.UserInfoForToken;
import com.jichuangsi.school.questionsrepository.entity.FavorQuestions;
import com.jichuangsi.school.questionsrepository.exception.QuestionRepositoryServiceException;
import com.jichuangsi.school.questionsrepository.model.PageHolder;
import com.jichuangsi.school.questionsrepository.model.common.DeleteQueryModel;
import com.jichuangsi.school.questionsrepository.model.common.SearchQuestionModel;
import com.jichuangsi.school.questionsrepository.model.favor.FavorQuestion;

public interface IFavorQuestionsRepositoryService {

    //添加收藏
    Boolean addFavorQuestion(UserInfoForToken userInfoForToken,FavorQuestion favorQuestion);

    //返回分页查询收藏
    PageHolder<FavorQuestion> getFavorQuestionSortList(UserInfoForToken userInfoForToken, SearchQuestionModel searchQuestionModel);

    //通过id获取收藏
    FavorQuestions getFavorById(String id);

    //通过id删除收藏
    void deleteFavorQuestion(FavorQuestions fqs);

    void deleteFavorQuestions(DeleteQueryModel deleteQueryModel) throws QuestionRepositoryServiceException;
}
