package com.jichuangsi.school.examservice.repository.impl;

import com.jichuangsi.school.examservice.entity.Exam;
import com.jichuangsi.school.examservice.repository.IExamExtraRepository;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.List;
import java.util.regex.Pattern;

@Repository
public class ExamExtraRepositoryImpl implements IExamExtraRepository {
    @Resource
    private MongoTemplate mongoTemplate;
   /* @Override
    public void deleteEaxmsByEaxmId(List<String> eids) {
        Criteria criteria = Criteria.where("eaxmId").in(eids);
        mongoTemplate.remove(new Query(criteria), Eaxm.class);
    }*/

    @Override
    public List<Exam> findExamsByExamNameLike(String keyword,String pageSize,String pageIndex) {
        Pattern pattern= Pattern.compile("^.*"+keyword+".*$", Pattern.CASE_INSENSITIVE);
        Criteria criteria = Criteria.where("examName").regex(pattern);
        Query query = new Query(criteria);
        query.skip(Integer.valueOf(pageSize));
        query.limit((Integer.valueOf(pageIndex)-1)*Integer.valueOf(pageSize));
        return mongoTemplate.find(query,Exam.class);
    }

    @Override
    public long countByExamNameLike(String keyword) {
        Pattern pattern= Pattern.compile("^.*"+keyword+".*$", Pattern.CASE_INSENSITIVE);
        Criteria criteria = Criteria.where("examName").regex(pattern);
        return mongoTemplate.count(new Query(criteria),Exam.class);
    }

}
