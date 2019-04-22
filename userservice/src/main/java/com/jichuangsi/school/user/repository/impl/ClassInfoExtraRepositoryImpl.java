package com.jichuangsi.school.user.repository.impl;

import com.jichuangsi.school.user.entity.org.ClassInfo;
import com.jichuangsi.school.user.repository.IClassInfoExtraRepository;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.List;

@Repository
public class ClassInfoExtraRepositoryImpl implements IClassInfoExtraRepository {
    @Resource
    private MongoTemplate mongoTemplate;

    @Override
    public List<ClassInfo> findByClassIdInAndDeleteFlagAndUpdateTimeGreaterThanAndUpdateTimeLessThanOrderByCreatedTime(List<String> classIds, String delete, long beign, long end, int pageIndex, int pageSize) {
        Criteria criteria = Criteria.where("id").in(classIds).and("deleteFlag").is(delete);
        criteria.andOperator(Criteria.where("updateTime").gte(beign),Criteria.where("updateTime").lte(end));
        Query query = new Query(criteria);
        query.with(new Sort(Sort.Direction.DESC,"createTime")).limit(pageSize).skip((pageIndex-1)*pageSize);
        return mongoTemplate.find(query,ClassInfo.class);
    }

    @Override
    public int countByClassIdInAndDeleteFlagAndUpdateTimeGreaterThanAndUpdateTimeLessThanOrderByCreatedTime(List<String> classIds, String delete, long beign, long end) {
        Criteria criteria = Criteria.where("id").in(classIds).and("deleteFlag").is(delete);
        criteria.andOperator(Criteria.where("updateTime").gte(beign),Criteria.where("updateTime").lte(end));
        Query query = new Query(criteria);
        return (int)mongoTemplate.count(query,ClassInfo.class);
    }
}
