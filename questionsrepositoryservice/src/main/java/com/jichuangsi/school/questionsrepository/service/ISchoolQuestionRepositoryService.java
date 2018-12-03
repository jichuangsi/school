package com.jichuangsi.school.questionsrepository.service;

import com.jichuangsi.microservice.common.model.UserInfoForToken;
import com.jichuangsi.school.questionsrepository.entity.SchoolQuestions;
import com.jichuangsi.school.questionsrepository.exception.QuestionRepositoryServiceException;
import com.jichuangsi.school.questionsrepository.model.PageHolder;
import com.jichuangsi.school.questionsrepository.model.common.DeleteQueryModel;
import com.jichuangsi.school.questionsrepository.model.common.SearchQuestionModel;
import com.jichuangsi.school.questionsrepository.model.common.QuestionFile;
import com.jichuangsi.school.questionsrepository.model.school.SchoolQuestion;
import com.jichuangsi.school.questionsrepository.model.transfer.TransferSchool;

public interface ISchoolQuestionRepositoryService {

    void addSchoolQuestion(UserInfoForToken userInfoForToken, TransferSchool school, SchoolQuestion schoolQuestion);

    PageHolder<SchoolQuestion> getSortSchoolQuestion(TransferSchool school, SearchQuestionModel searchQuestionModel);

    //上传问题图片
    SchoolQuestion uploadQuestionPic(UserInfoForToken userInfo, QuestionFile questionFile) throws QuestionRepositoryServiceException;

    //下载问题图片
    QuestionFile downQuestionPic(UserInfoForToken userInfo, String fileName) throws QuestionRepositoryServiceException;

    //删除问题图片
    void deleteQuestionPic(UserInfoForToken userInfo, String fileName) throws QuestionRepositoryServiceException;

    SchoolQuestions getSchoolQuestionById(String id);

    void deleteSchoolQuestion(UserInfoForToken userInfoForToken, SchoolQuestions schoolQuestions) throws  QuestionRepositoryServiceException;

    //批量删除校库问题
    void deleteSchoolQuestions(UserInfoForToken userInfo, DeleteQueryModel deleteQueryModel) throws QuestionRepositoryServiceException;
}
