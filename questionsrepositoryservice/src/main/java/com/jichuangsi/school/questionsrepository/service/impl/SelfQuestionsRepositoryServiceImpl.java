package com.jichuangsi.school.questionsrepository.service.impl;

import com.jichuangsi.microservice.common.cache.ICacheService;
import com.jichuangsi.microservice.common.model.UserInfoForToken;
import com.jichuangsi.school.questionsrepository.constant.ResultCode;
import com.jichuangsi.school.questionsrepository.entity.SelfQuestions;
import com.jichuangsi.school.questionsrepository.exception.QuestionRepositoryServiceException;
import com.jichuangsi.school.questionsrepository.model.PageHolder;
import com.jichuangsi.school.questionsrepository.model.common.DeleteQueryModel;
import com.jichuangsi.school.questionsrepository.model.common.QuestionFile;
import com.jichuangsi.school.questionsrepository.model.common.SearchQuestionModel;
import com.jichuangsi.school.questionsrepository.model.common.SendCodePic;
import com.jichuangsi.school.questionsrepository.model.self.SelfQuestion;
import com.jichuangsi.school.questionsrepository.model.transfer.TransferTeacher;
import com.jichuangsi.school.questionsrepository.model.translate.PicContent;
import com.jichuangsi.school.questionsrepository.repository.ISelfQuestionsRepository;
import com.jichuangsi.school.questionsrepository.service.IFileStoreService;
import com.jichuangsi.school.questionsrepository.service.IPicTranslationService;
import com.jichuangsi.school.questionsrepository.service.ISelfQuestionsRepositoryService;
import com.jichuangsi.school.questionsrepository.service.IUserInfoService;
import com.jichuangsi.school.questionsrepository.util.MappingEntity2ModelConverter;
import com.jichuangsi.school.questionsrepository.util.MappingModel2EntityConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service
public class SelfQuestionsRepositoryServiceImpl implements ISelfQuestionsRepositoryService {

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
    public void uploadQuestionPic(QuestionFile questionFile, SendCodePic sendCodePic) throws QuestionRepositoryServiceException{
        try {
            fileStoreService.uploadQuestionFile(questionFile);
        } catch (Exception e) {
            throw new QuestionRepositoryServiceException(ResultCode.FILE_UPLOAD_ERROR);
        }
        //保存进内存
        cacheServiceLocal.set(picPrefix+sendCodePic.getOneCode(),questionFile.getStoredName(),picCacheTime);
        //清除识别图中文字的缓存，如果换Redis需要修改其他方法
        cacheServiceLocal.set(PIC_CONTENT_PREFIX+sendCodePic.getOneCode(), null, 0);
    }

    @Override
    public QuestionFile downQuestionPic(UserInfoForToken userInfo, String fileName) throws QuestionRepositoryServiceException {
        try {
            return fileStoreService.donwloadQuestionFile(fileName);
        } catch (Exception e) {
            throw  new QuestionRepositoryServiceException(ResultCode.FILE_DOWNLOAD_ERROR);
        }
    }

    @Override
    public void deleteQuestionPic(UserInfoForToken userInfo, String fileName) throws QuestionRepositoryServiceException {
        try {
            fileStoreService.deleteQuestionFile(fileName);
        } catch (Exception e) {
            throw new QuestionRepositoryServiceException(ResultCode.FILE_DELETE_ERROR);
        }
    }

    @Override
    public void addSelfQuestion(UserInfoForToken userInfoForToken, SelfQuestion selfQuestion) throws QuestionRepositoryServiceException {
        TransferTeacher transferTeacher = userInfoService.getUserForTeacherById(userInfoForToken.getUserId());
        if(transferTeacher==null) throw new QuestionRepositoryServiceException(ResultCode.TEACHER_INFO_NOT_EXISTED);
        String picName = cacheServiceLocal.get(picPrefix+userInfoForToken.getUserId()+selfQuestion.getCode());
        selfQuestion.setQuestionPic(picName);
        selfQuestion.setGradeId(transferTeacher.getGradeId());
        selfQuestion.setSubjectId(transferTeacher.getSubjectId());
        SelfQuestions selfQuestions = MappingModel2EntityConverter.ConverterSelfQuestion(userInfoForToken,selfQuestion);
        mongoTemplate.save(selfQuestions);
    }

    @Override
    public PageHolder<SelfQuestion> getSelfQuestionSortList(UserInfoForToken userInfoForToken, SearchQuestionModel searchQuestionModel) {
        PageHolder<SelfQuestion> page = new PageHolder<SelfQuestion>();
        page.setPageNum(searchQuestionModel.getPageIndex());
        page.setPageSize(searchQuestionModel.getPageSize()>0?searchQuestionModel.getPageSize():defaultPageSize);
        page.setTotal((int)selfQuestionsRepository.getSelfQuestionCount(userInfoForToken,searchQuestionModel));
        page.setContent(changeForSelfQuestions(selfQuestionsRepository.selectPageSelfQ(userInfoForToken,searchQuestionModel)));
        return page;
    }


    private List<SelfQuestion> changeForSelfQuestions(List<SelfQuestions> selfQuestions){
        List<SelfQuestion> selfQuestion = new ArrayList<SelfQuestion>();
        selfQuestions.forEach(sqs -> {
            SelfQuestion sq = MappingEntity2ModelConverter.ConverterSelfQuestions(sqs);
            selfQuestion.add(sq);
        });
        return selfQuestion;
    }

    @Override
    public SelfQuestions getSelfQuestionsById(String id) {
        return mongoTemplate.findById(id,SelfQuestions.class);
    }

    @Override
    public void deleteSelfQuestion(UserInfoForToken userInfoForToken,SelfQuestions selfQuestions) throws QuestionRepositoryServiceException {
        deleteQuestionPic(userInfoForToken,selfQuestions.getQuestionPic());
        mongoTemplate.remove(selfQuestions);
    }

    @Override
    public void deleteSelfQuestions(UserInfoForToken userInfo, DeleteQueryModel deleteQueryModel) throws QuestionRepositoryServiceException {
        selfQuestionsRepository.findAllAndRemove(deleteQueryModel);
    }

    @Override
    public PicContent transQuestionPic(SendCodePic sendCodePic) throws QuestionRepositoryServiceException {
        PicContent picContent = null;
        picContent = cacheServiceLocal.get(PIC_CONTENT_PREFIX+sendCodePic.getOneCode());

        if(picContent==null) {
            String picName = cacheServiceLocal.get(picPrefix + sendCodePic.getOneCode());
            if (StringUtils.isEmpty(picName)) throw new QuestionRepositoryServiceException(ResultCode.FILE_NOT_EXISTED);
            QuestionFile questionFile = null;
            try {
                questionFile = fileStoreService.donwloadQuestionFile(picName);
            } catch (Exception e) {
                throw new QuestionRepositoryServiceException(ResultCode.FILE_DOWNLOAD_ERROR);
            }
            if (questionFile.getContent() == null)
                throw new QuestionRepositoryServiceException(ResultCode.FILE_NOT_EXISTED);
            picContent = picTranslationService.translate(questionFile.getContent());
            picContent.setPicSub(picName);
            //保存进内存
            cacheServiceLocal.set(PIC_CONTENT_PREFIX + sendCodePic.getOneCode(), picContent, picCacheTime);
        }
        return picContent;
    }

    @Override
    public QuestionFile viewQuestionPic(SendCodePic sendCodePic) throws QuestionRepositoryServiceException {
        String picName = cacheServiceLocal.get(picPrefix + sendCodePic.getOneCode());
        if (StringUtils.isEmpty(picName)) throw new QuestionRepositoryServiceException(ResultCode.FILE_NOT_EXISTED);
        QuestionFile questionFile = null;
        try {
            questionFile = fileStoreService.donwloadQuestionFile(picName);
        } catch (Exception e) {
            throw new QuestionRepositoryServiceException(ResultCode.FILE_DOWNLOAD_ERROR);
        }
        return questionFile;
    }

}
