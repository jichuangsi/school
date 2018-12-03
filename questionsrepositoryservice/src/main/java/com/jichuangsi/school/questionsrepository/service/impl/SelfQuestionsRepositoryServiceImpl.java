package com.jichuangsi.school.questionsrepository.service.impl;

import com.jichuangsi.microservice.common.model.UserInfoForToken;
import com.jichuangsi.school.questionsrepository.constant.ResultCode;
import com.jichuangsi.school.questionsrepository.entity.SelfQuestions;
import com.jichuangsi.school.questionsrepository.exception.QuestionRepositoryServiceException;
import com.jichuangsi.school.questionsrepository.model.PageHolder;
import com.jichuangsi.school.questionsrepository.model.common.DeleteQueryModel;
import com.jichuangsi.school.questionsrepository.model.common.SearchQuestionModel;
import com.jichuangsi.school.questionsrepository.model.common.QuestionFile;
import com.jichuangsi.school.questionsrepository.model.self.SelfQuestion;
import com.jichuangsi.school.questionsrepository.repository.ISelfQuestionsRepository;
import com.jichuangsi.school.questionsrepository.service.IFileStoreService;
import com.jichuangsi.school.questionsrepository.service.ISelfQuestionsRepositoryService;
import com.jichuangsi.school.questionsrepository.util.MappingEntity2ModelConverter;
import com.jichuangsi.school.questionsrepository.util.MappingModel2EntityConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service
public class SelfQuestionsRepositoryServiceImpl implements ISelfQuestionsRepositoryService {

    @Value("${com.jichuangsi.school.result.page-size}")
    private int defaultPageSize;

    @Resource
    private IFileStoreService fileStoreService;

    @Resource
    private ISelfQuestionsRepository selfQuestionsRepository;

    @Resource
    private MongoTemplate mongoTemplate;
    @Override
    public SelfQuestion uploadQuestionPic(UserInfoForToken userInfo, QuestionFile questionFile) throws QuestionRepositoryServiceException{
        try {
            fileStoreService.uploadQuestionFile(questionFile);
        } catch (Exception e) {
            throw new QuestionRepositoryServiceException(ResultCode.FILE_UPLOAD_ERROR);
        }
        SelfQuestion selfQuestion = new SelfQuestion();
        selfQuestion.setTeacherId(userInfo.getUserId());
        selfQuestion.setQuestionPic(questionFile.getStoredName());
        selfQuestion.setTeacherName(userInfo.getUserName());
        return selfQuestion;
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
    public void addSelfQuestion(UserInfoForToken userInfoForToken, SelfQuestion selfQuestion) {
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
}
