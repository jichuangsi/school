package com.jichuangsi.school.questionsrepository.service;

import com.jichuangsi.microservice.common.model.UserInfoForToken;
import com.jichuangsi.school.questionsrepository.exception.QuestionRepositoryServiceException;
import com.jichuangsi.school.questionsrepository.model.*;
import org.apache.catalina.User;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

public interface IQuestionsRepositoryService {

    @Transactional
    List<EditionTreeNode> getTreeForSubjectEditionInfo(UserInfoForToken userInfo) throws QuestionRepositoryServiceException;

    @Transactional
    Map<String, List> getMapForOtherBasicInfo(UserInfoForToken userInfo) throws QuestionRepositoryServiceException;

    @Transactional
    Map<String, List> getAllBasicInfoForQuestionSelection(UserInfoForToken userInfoForToken) throws QuestionRepositoryServiceException;

    @Transactional
    List<ChapterTreeNode> getTreeForChapterInfo(UserInfoForToken userInfoForToken, ChapterQueryModel chapterQueryModel) throws QuestionRepositoryServiceException;

    @Transactional
    PageHolder<QuestionNode> getListForQuestionsByKnowledge(UserInfoForToken userInfoForToken, QuestionQueryModel questionQueryModel) throws QuestionRepositoryServiceException;

    @Transactional
    PageHolder<QuestionExtraNode> getListForQuestionsExtraByKnowledge(UserInfoForToken userInfoForToken, QuestionQueryModel questionQueryModel) throws QuestionRepositoryServiceException;

    @Transactional
    List<AnswerNode> getListForAnswersByQuestionId(UserInfoForToken userInfo, AnswerQueryModel answerQueryModel) throws QuestionRepositoryServiceException;

    @Transactional
    List<EditionTreeNode> getTreeForSubjectEditionInfoByTeacher(UserInfoForToken userInfo) throws QuestionRepositoryServiceException;

    /*@Transactional
    PageHolder<Map<QuestionNode,Integer>> SortByAnswerCount(UserInfoForToken userInfoForToken, QuestionQueryModel questionQueryModel) throws QuestionRepositoryServiceException;

    @Transactional
    PageHolder SortByAddPapercount(UserInfoForToken userInfoForToken, QuestionQueryModel questionQueryModel) throws QuestionRepositoryServiceException;*/
}
