package com.jichuangsi.school.questionsrepository.service.impl;

import com.jichuangsi.microservice.common.model.UserInfoForToken;
import com.jichuangsi.school.questionsrepository.constant.ResultCode;
import com.jichuangsi.school.questionsrepository.entity.SchoolQuestions;
import com.jichuangsi.school.questionsrepository.exception.QuestionRepositoryServiceException;
import com.jichuangsi.school.questionsrepository.model.PageHolder;
import com.jichuangsi.school.questionsrepository.model.common.DeleteQueryModel;
import com.jichuangsi.school.questionsrepository.model.common.SearchQuestionModel;
import com.jichuangsi.school.questionsrepository.model.common.QuestionFile;
import com.jichuangsi.school.questionsrepository.model.school.SchoolQuestion;
import com.jichuangsi.school.questionsrepository.repository.ISchoolQuestionsRepository;
import com.jichuangsi.school.questionsrepository.service.IFileStoreService;
import com.jichuangsi.school.questionsrepository.service.ISchoolQuestionRepositoryService;
import com.jichuangsi.school.questionsrepository.util.MappingEntity2ModelConverter;
import com.jichuangsi.school.questionsrepository.util.MappingModel2EntityConverter;
import com.jichuangsi.school.questionsrepository.model.transfer.TransferSchool;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service
public class SchoolQuestionRepositoryServiceImpl implements ISchoolQuestionRepositoryService {

    @Value("${com.jichuangsi.school.result.page-size}")
    private int defaultPageSize;

    @Resource
    private MongoTemplate mongoTemplate;

    @Resource
    private IFileStoreService fileStoreService;

    @Resource
    private ISchoolQuestionsRepository schoolQuestionsRepository;

    @Override
    public void addSchoolQuestion(UserInfoForToken userInfo, TransferSchool school, SchoolQuestion schoolQuestion) {
        schoolQuestion.setSchoolName(school.getSchoolName());
        schoolQuestion.setSchoolId(school.getSchoolId());
        mongoTemplate.save(MappingModel2EntityConverter.ConverterSchoolQuestion(userInfo,schoolQuestion));
    }

    @Override
    public PageHolder<SchoolQuestion> getSortSchoolQuestion(TransferSchool school, SearchQuestionModel searchQuestionModel) {
        PageHolder<SchoolQuestion> page = new PageHolder<SchoolQuestion>();
        page.setPageNum(searchQuestionModel.getPageIndex());
        page.setPageSize(searchQuestionModel.getPageSize()>0?searchQuestionModel.getPageSize():defaultPageSize);
        page.setContent(changForSchoolQuestions(schoolQuestionsRepository.selectSortSchoolQuestions(searchQuestionModel,school)));
        page.setTotal((int)schoolQuestionsRepository.selectSchoolCount(searchQuestionModel,school));
        return page;
    }

    private List<SchoolQuestion> changForSchoolQuestions(List<SchoolQuestions> sqs){
        List<SchoolQuestion> list = new ArrayList<SchoolQuestion>();
        sqs.forEach(sq->{
            list.add(MappingEntity2ModelConverter.ConverterSchoolQuestions(sq));
        });
        return list;
    }

    @Override
    public SchoolQuestion uploadQuestionPic(UserInfoForToken userInfo, QuestionFile questionFile) throws QuestionRepositoryServiceException {
        try {
            fileStoreService.uploadQuestionFile(questionFile);
        } catch (Exception e) {
            throw new QuestionRepositoryServiceException(ResultCode.FILE_UPLOAD_ERROR);
        }
        SchoolQuestion schoolQuestion = new SchoolQuestion();
        schoolQuestion.setTeacherId(userInfo.getUserId());
        schoolQuestion.setQuestionPic(questionFile.getStoredName());
        schoolQuestion.setTeacherName(userInfo.getUserName());
        return schoolQuestion;
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
    public SchoolQuestions getSchoolQuestionById(String id) {
        return mongoTemplate.findById(id,SchoolQuestions.class);
    }

    @Override
    public void deleteSchoolQuestion(UserInfoForToken userInfoForToken, SchoolQuestions schoolQuestions) throws  QuestionRepositoryServiceException{
        deleteQuestionPic(userInfoForToken,schoolQuestions.getQuestionPic());
        mongoTemplate.remove(schoolQuestions);
    }

    @Override
    public void deleteSchoolQuestions(UserInfoForToken userInfo, DeleteQueryModel deleteQueryModel) throws QuestionRepositoryServiceException {
        schoolQuestionsRepository.findAllAndRemove(deleteQueryModel);
    }
}
