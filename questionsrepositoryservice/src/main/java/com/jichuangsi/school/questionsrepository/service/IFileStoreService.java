package com.jichuangsi.school.questionsrepository.service;

import com.jichuangsi.school.questionsrepository.model.common.QuestionFile;

public interface IFileStoreService {

    QuestionFile uploadQuestionFile(QuestionFile file) throws Exception;

    QuestionFile donwloadQuestionFile(String fileName) throws Exception;

    void deleteQuestionFile(String fileName) throws Exception;

}
