package com.jichuangsi.school.questionsrepository.service.impl;

import com.jichuangsi.microservice.common.model.UserInfoForToken;
import com.jichuangsi.school.questionsrepository.constant.ResultCode;
import com.jichuangsi.school.questionsrepository.entity.FavorQuestions;
import com.jichuangsi.school.questionsrepository.exception.QuestionRepositoryServiceException;
import com.jichuangsi.school.questionsrepository.model.PageHolder;
import com.jichuangsi.school.questionsrepository.model.common.DeleteQueryModel;
import com.jichuangsi.school.questionsrepository.model.common.SearchQuestionModel;
import com.jichuangsi.school.questionsrepository.model.favor.FavorQuestion;
import com.jichuangsi.school.questionsrepository.model.transfer.TransferTeacher;
import com.jichuangsi.school.questionsrepository.repository.IFavorQuestionsRepository;
import com.jichuangsi.school.questionsrepository.service.IFavorQuestionsRepositoryService;
import com.jichuangsi.school.questionsrepository.service.IUserInfoService;
import com.jichuangsi.school.questionsrepository.util.MappingEntity2ModelConverter;
import com.jichuangsi.school.questionsrepository.util.MappingModel2EntityConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service
public class FavorQuestionsRepositoryServiceImpl implements IFavorQuestionsRepositoryService {

    @Value("${com.jichuangsi.school.result.page-size}")
    private int defaultPageSize;

    @Resource
    private IFavorQuestionsRepository favorQuestionsRepository;

    @Resource
    private MongoTemplate mongoTemplate;

    @Resource
    private IUserInfoService userInfoService;

    @Override
    public Boolean addFavorQuestion(UserInfoForToken userInfoForToken,FavorQuestion favorQuestion) throws QuestionRepositoryServiceException{
        TransferTeacher transferTeacher = userInfoService.getUserForTeacherById(userInfoForToken.getUserId());
        if(transferTeacher==null) throw new QuestionRepositoryServiceException(ResultCode.TEACHER_INFO_NOT_EXISTED);
        favorQuestion.setGradeId(transferTeacher.getGradeId());
        favorQuestion.setSubjectId(transferTeacher.getSubjectId());
        FavorQuestions favorQuestions = MappingModel2EntityConverter.ConverterFavorQuestion(userInfoForToken,favorQuestion);
        favorQuestions = (FavorQuestions) favorQuestionsRepository.save(favorQuestions);
        if(favorQuestions!=null){
            if(favorQuestions.getId()!=null){
                return true;
            }
        }
        return false;
    }

    @Override
    public PageHolder<FavorQuestion> getFavorQuestionSortList(UserInfoForToken userInfo, SearchQuestionModel searchQuestionModel) {
        PageHolder<FavorQuestion> page = new PageHolder<FavorQuestion>();
        page.setPageNum(searchQuestionModel.getPageIndex());
        page.setPageSize(searchQuestionModel.getPageSize()>0?searchQuestionModel.getPageSize():defaultPageSize);
        page.setTotal((int)favorQuestionsRepository.getFavorQuestionCount(userInfo,searchQuestionModel));
        page.setContent(changeForFavorQuestions(favorQuestionsRepository.selectPageFavorQ(userInfo,searchQuestionModel)));
        return page;
    }


    private List<FavorQuestion> changeForFavorQuestions(List<FavorQuestions> favorQuestions){
        List<FavorQuestion> favorQuestion = new ArrayList<FavorQuestion>();
        favorQuestions.forEach(fqs -> {
            FavorQuestion fq = MappingEntity2ModelConverter.ConverterFavorQuestions(fqs);
            favorQuestion.add(fq);
        });
        return favorQuestion;
    }

    /*@Override
    public FavorQuestions getFavorById(String id) {
        return mongoTemplate.findById(id,FavorQuestions.class);
    }

    @Override
    public void  deleteFavorQuestion(FavorQuestions fqs) {
        mongoTemplate.remove(fqs);
    }*/

    @Override
    public void deleteFavorQuestions(DeleteQueryModel deleteQueryModel) throws QuestionRepositoryServiceException {
        favorQuestionsRepository.findAllAndRemove(deleteQueryModel);
    }

    @Override
    public Boolean isExistFavor(UserInfoForToken userInfo, String MD52) {
        return favorQuestionsRepository.findFavorByMD52(userInfo.getUserId(),MD52)!=null;
    }
}
