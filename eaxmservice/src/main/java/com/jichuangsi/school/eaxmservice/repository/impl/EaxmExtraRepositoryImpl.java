package com.jichuangsi.school.eaxmservice.repository.impl;

import com.jichuangsi.school.eaxmservice.repository.IEaxmExtraRepository;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;

@Repository
public class EaxmExtraRepositoryImpl implements IEaxmExtraRepository {
    @Resource
    private MongoTemplate mongoTemplate;
   /* @Override
    public void deleteEaxmsByEaxmId(List<String> eids) {
        Criteria criteria = Criteria.where("eaxmId").in(eids);
        mongoTemplate.remove(new Query(criteria), Eaxm.class);
    }*/


}
