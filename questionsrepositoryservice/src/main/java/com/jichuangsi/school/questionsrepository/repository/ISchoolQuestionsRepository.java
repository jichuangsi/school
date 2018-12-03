package com.jichuangsi.school.questionsrepository.repository;

import com.jichuangsi.school.questionsrepository.entity.SchoolQuestions;
import com.jichuangsi.school.questionsrepository.model.common.DeleteQueryModel;
import com.jichuangsi.school.questionsrepository.model.common.SearchQuestionModel;
import com.jichuangsi.school.questionsrepository.model.transfer.TransferSchool;

import java.util.List;

public interface ISchoolQuestionsRepository<T> {

    List<SchoolQuestions> selectSortSchoolQuestions(SearchQuestionModel searchQuestionModel, TransferSchool school);

    long selectSchoolCount(SearchQuestionModel searchQuestionModel, TransferSchool school);

    T save(T entity);

    void findAllAndRemove(DeleteQueryModel deleteQueryModel);
}
