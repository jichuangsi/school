package com.jichuangsi.school.homeworkservice.service;

import com.jichuangsi.microservice.common.model.UserInfoForToken;
import com.jichuangsi.school.homeworkservice.exception.TeacherHomeworkServiceException;
import com.jichuangsi.school.homeworkservice.model.HomeworkModelForTeacher;
import com.jichuangsi.school.homeworkservice.model.QuestionModelForTeacher;
import com.jichuangsi.school.homeworkservice.model.SearchHomeworkModel;
import com.jichuangsi.school.homeworkservice.model.common.DeleteQueryModel;
import com.jichuangsi.school.homeworkservice.model.common.Elements;
import com.jichuangsi.school.homeworkservice.model.common.PageHolder;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface IHomeworkConsoleService {

    @Transactional
    void saveNewHomework(UserInfoForToken userInfo, HomeworkModelForTeacher homeworkModelForTeacher) throws TeacherHomeworkServiceException;

    @Transactional
    Elements getElementsList(UserInfoForToken userInfo) throws TeacherHomeworkServiceException;

    @Transactional
    PageHolder<HomeworkModelForTeacher> getSortedHomeworksList(UserInfoForToken userInfo, SearchHomeworkModel searchHomeworkModel) throws TeacherHomeworkServiceException;

    @Transactional
    List<QuestionModelForTeacher> getQuestionList(List<String> qIds) throws TeacherHomeworkServiceException;

    @Transactional
    void deleteHomeWorkIsNotStart(UserInfoForToken userInfo, DeleteQueryModel deleteQueryModel) throws TeacherHomeworkServiceException;

    @Transactional
    void updateHomeWorkIsNotStart(UserInfoForToken userInfo, HomeworkModelForTeacher homeworkModelForTeacher) throws TeacherHomeworkServiceException;

    @Transactional
    void updateHomeWork2NewStatus(UserInfoForToken userInfo, HomeworkModelForTeacher homeworkModelForTeacher) throws TeacherHomeworkServiceException;

}
