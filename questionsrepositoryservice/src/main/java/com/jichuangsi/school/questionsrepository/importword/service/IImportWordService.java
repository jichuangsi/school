package com.jichuangsi.school.questionsrepository.importword.service;


import com.jichuangsi.microservice.common.model.UserInfoForToken;
import com.jichuangsi.school.questionsrepository.entity.SchoolQuestions;
import com.jichuangsi.school.questionsrepository.entity.SelfQuestions;
import com.jichuangsi.school.questionsrepository.exception.QuestionRepositoryServiceException;
import com.jichuangsi.school.questionsrepository.model.school.SchoolQuestion;
import com.jichuangsi.school.questionsrepository.model.self.SelfQuestion;

public interface IImportWordService {
    void save(UserInfoForToken userInfoForToken, SelfQuestion selfQuestion) throws QuestionRepositoryServiceException;
//    SelfQuestions LoadingSelfQuestion(UserInfoForToken userInfoForToken, SelfQuestion selfQuestion) throws QuestionRepositoryServiceException;
    void savesimension(UserInfoForToken userInfoForToken, SelfQuestion selfQuestion) throws QuestionRepositoryServiceException;

}
