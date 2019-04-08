package com.jichuangsi.school.homeworkservice.service;

import com.jichuangsi.microservice.common.model.UserInfoForToken;
import com.jichuangsi.school.homeworkservice.exception.StudentHomeworkServiceException;
import com.jichuangsi.school.homeworkservice.model.AnswerModelForStudent;
import com.jichuangsi.school.homeworkservice.model.HomeworkModelForStudent;
import com.jichuangsi.school.homeworkservice.model.SearchHomeworkModel;
import com.jichuangsi.school.homeworkservice.model.common.PageHolder;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface IStudentHomeworkService {

    @Transactional
    List<HomeworkModelForStudent> getHomeworksList(UserInfoForToken userInfo) throws StudentHomeworkServiceException;

    @Transactional
    PageHolder<HomeworkModelForStudent> getHistoryHomeworksList(UserInfoForToken userInfo, SearchHomeworkModel searchHomeworkModel) throws StudentHomeworkServiceException;

    @Transactional
    HomeworkModelForStudent getParticularHomework(UserInfoForToken userInfo, String homeworkId) throws StudentHomeworkServiceException;

    @Transactional
    void saveStudentAnswer(UserInfoForToken userInfo, String homeworkId, String questionId, AnswerModelForStudent answer) throws StudentHomeworkServiceException;

    @Transactional
    void submitParticularHomework(UserInfoForToken userInfo, String homeworkId) throws StudentHomeworkServiceException;

    List<HomeworkModelForStudent> getHomeworksListOnWeek(String userId,String subject) throws StudentHomeworkServiceException;
}
