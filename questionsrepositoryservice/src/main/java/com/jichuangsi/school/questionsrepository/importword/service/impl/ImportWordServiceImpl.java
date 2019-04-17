package com.jichuangsi.school.questionsrepository.importword.service.impl;

import com.jichuangsi.microservice.common.cache.ICacheService;
import com.jichuangsi.microservice.common.model.UserInfoForToken;
import com.jichuangsi.school.questionsrepository.constant.ResultCode;
import com.jichuangsi.school.questionsrepository.entity.SelfQuestions;
import com.jichuangsi.school.questionsrepository.exception.QuestionRepositoryServiceException;
import com.jichuangsi.school.questionsrepository.importword.repository.IImportWordRepository;

import com.jichuangsi.school.questionsrepository.importword.service.IImportWordService;
import com.jichuangsi.school.questionsrepository.model.self.SelfQuestion;
import com.jichuangsi.school.questionsrepository.model.transfer.TransferTeacher;
import com.jichuangsi.school.questionsrepository.repository.ISelfQuestionsRepository;
import com.jichuangsi.school.questionsrepository.service.IFileStoreService;
import com.jichuangsi.school.questionsrepository.service.IPicTranslationService;
import com.jichuangsi.school.questionsrepository.service.IUserInfoService;
import com.jichuangsi.school.questionsrepository.util.MappingModel2EntityConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

@Service

/**
 * @Author LaiJX
 * @Date 15:30 2019/4/1
 * @Param
 * @What?
        * @return
        **/
@Transactional
public class ImportWordServiceImpl implements IImportWordService{
    @Resource
    private IImportWordRepository iImportWordRepository;

    @Value("${com.jichuangsi.school.result.page-size}")
    private int defaultPageSize;
    @Value("${com.jichuangsi.school.codePic.time}")
    private long picCacheTime;
    @Value("${com.jichuangsi.school.codePic.prefix}")
    private String picPrefix;

    private final static String PIC_CONTENT_PREFIX = "picContent_";

    @Resource
    private IFileStoreService fileStoreService;

    @Resource
    private ISelfQuestionsRepository selfQuestionsRepository;

    @Resource
    private IUserInfoService userInfoService;

    @Resource
    private ICacheService cacheServiceLocal;

    @Resource
    private MongoTemplate mongoTemplate;

    @Resource
    private IPicTranslationService picTranslationService;

    @Override
    /**
     * @Author LaiJX
     * @Date 15:30 2019/4/1
     * @Param []
     * @What? 保存题目
            * @return void
            **/
    public void save(UserInfoForToken userInfoForToken, SelfQuestion selfQuestion) throws QuestionRepositoryServiceException {
        TransferTeacher transferTeacher = userInfoService.getUserForTeacherById(userInfoForToken.getUserId());
        if(transferTeacher==null) throw new QuestionRepositoryServiceException(ResultCode.TEACHER_INFO_NOT_EXISTED);
        selfQuestion.setGradeId(transferTeacher.getGradeId());
        selfQuestion.setSubjectId(transferTeacher.getSubjectId());
        SelfQuestions selfQuestions = MappingModel2EntityConverter.ConverterSelfQuestion(userInfoForToken,selfQuestion);
        iImportWordRepository.addWordTitle(selfQuestions);
    }

//    /**
//     * @Author LaiJX
//     * @Date 16:24 2019/4/16
//     * @Param
//     * @What? 装载题目类蕴含的信息
//            * @return
//            **/
//    @Override
//    public SelfQuestions LoadingSelfQuestion(UserInfoForToken userInfoForToken , SelfQuestion selfQuestion) throws QuestionRepositoryServiceException {
//        TransferTeacher transferTeacher = userInfoService.getUserForTeacherById(userInfoForToken.getUserId());
//        if(transferTeacher==null) throw new QuestionRepositoryServiceException(ResultCode.TEACHER_INFO_NOT_EXISTED);
//        selfQuestion.setGradeId(transferTeacher.getGradeId());
//        selfQuestion.setSubjectId(transferTeacher.getSubjectId());
//        SelfQuestions selfQuestions = MappingModel2EntityConverter.ConverterSelfQuestion(userInfoForToken,selfQuestion);
//        return null;
//    }
}
