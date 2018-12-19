package com.jichuangsi.school.questionsrepository.repository.impl;

import com.jichuangsi.microservice.common.model.UserInfoForToken;
import com.jichuangsi.school.questionsrepository.entity.FavorQuestions;
import com.jichuangsi.school.questionsrepository.model.common.DeleteQueryModel;
import com.jichuangsi.school.questionsrepository.model.common.SearchQuestionModel;
import com.jichuangsi.school.questionsrepository.repository.IFavorQuestionsRepository;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.List;
import java.util.regex.Pattern;

@Repository
public class FavorQuestionsRepositoryImpl<T> implements IFavorQuestionsRepository<T> {

    @Resource
    private MongoTemplate mongoTemplate;

    @Override
    public T save(T entity) {
        mongoTemplate.insert(entity);
        return entity;
    }

    @Override
    public List<FavorQuestions> selectPageFavorQ(UserInfoForToken userInfo, SearchQuestionModel searchQuestionModel) {
        Criteria criteria = Criteria.where("teacherId").is(userInfo.getUserId())
                //.and("knowledge").is(searchQuestionModel.getKnowledge())
                ;
        if(!StringUtils.isEmpty(searchQuestionModel.getDifficulty())){
            Pattern diff= Pattern.compile("^.*"+searchQuestionModel.getDifficulty()+".*$", Pattern.CASE_INSENSITIVE);
            criteria.and("difficulty").regex(diff);
        }
        if(!StringUtils.isEmpty(searchQuestionModel.getType())){
            Pattern type= Pattern.compile("^.*"+searchQuestionModel.getType()+".*$", Pattern.CASE_INSENSITIVE);
            criteria.and("type").regex(type);
        }
        if(!StringUtils.isEmpty(searchQuestionModel.getKeyWord())){
            Pattern pattern= Pattern.compile("^.*"+searchQuestionModel.getKeyWord()+".*$", Pattern.CASE_INSENSITIVE);
            Criteria c1 = Criteria.where("content").regex(pattern);
            criteria.andOperator(c1);
        }
        Query query = new Query(criteria);
        /*if("1".equals(searchQuestionModel.getSort())){//排序需要调整
            query.with(new Sort(Sort.Direction.DESC,"_id"));
        }*/
        query.limit(searchQuestionModel.getPageSize());
        query.skip((searchQuestionModel.getPageIndex()-1)*searchQuestionModel.getPageSize());
        return mongoTemplate.find(query,FavorQuestions.class);
    }

    @Override
    public long getFavorQuestionCount(UserInfoForToken userInfo, SearchQuestionModel searchQuestionModel) {
        Criteria criteria = Criteria.where("teacherId").is(userInfo.getUserId())
                //.and("knowledge").is(searchQuestionModel.getKnowledge())
                ;
        if(!StringUtils.isEmpty(searchQuestionModel.getDifficulty())){
            Pattern diff= Pattern.compile("^.*"+searchQuestionModel.getDifficulty()+".*$", Pattern.CASE_INSENSITIVE);
            criteria.and("difficulty").regex(diff);
        }
        if(!StringUtils.isEmpty(searchQuestionModel.getType())){
            Pattern type= Pattern.compile("^.*"+searchQuestionModel.getType()+".*$", Pattern.CASE_INSENSITIVE);
            criteria.and("type").regex(type);
        }
        if(!StringUtils.isEmpty(searchQuestionModel.getKeyWord())){
            Pattern pattern= Pattern.compile("^.*"+searchQuestionModel.getKeyWord()+".*$", Pattern.CASE_INSENSITIVE);
            Criteria c1 = Criteria.where("content").regex(pattern);
            criteria.andOperator(c1);
        }
        Query query = new Query(criteria);

        return mongoTemplate.count(query,FavorQuestions.class);
    }

    @Override
    public void findAllAndRemove(DeleteQueryModel deleteQueryModel) {
        Criteria criteria = Criteria.where("questionIdMD52").in(deleteQueryModel.getIds());
        mongoTemplate.findAllAndRemove(new Query(criteria),FavorQuestions.class);
    }

    @Override
    public FavorQuestions findFavorByMD52(String teacherId, String MD52) {
        Criteria criteria = Criteria.where("teacherId").is(teacherId)
                .and("questionIdMD52").is(MD52);
        return mongoTemplate.findOne(new Query(criteria),FavorQuestions.class);
    }
}
