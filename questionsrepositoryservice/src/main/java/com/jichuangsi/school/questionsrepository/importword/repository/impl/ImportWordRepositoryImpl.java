package com.jichuangsi.school.questionsrepository.importword.repository.impl;

import com.jichuangsi.school.questionsrepository.constant.ResultCode;
import com.jichuangsi.school.questionsrepository.entity.SelfQuestions;
import com.jichuangsi.school.questionsrepository.exception.QuestionRepositoryServiceException;
import com.jichuangsi.school.questionsrepository.importword.repository.IImportWordRepository;
import com.jichuangsi.school.questionsrepository.model.transfer.TransferTeacher;
import org.springframework.data.mongodb.core.MongoTemplate;

/**
 * Created by 窝里横 on 2019/4/16.
 */
 @Repository
public class ImportWordRepositoryImpl implements IImportWordRepository {


    private MongoTemplate mongoTemplate;
    @Override
    public void addWordTitle(SelfQuestions selfQuestions) {
        mongoTemplate.save(selfQuestions);
    }
}
