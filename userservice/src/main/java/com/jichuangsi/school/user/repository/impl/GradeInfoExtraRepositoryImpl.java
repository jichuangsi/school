package com.jichuangsi.school.user.repository.impl;

import com.jichuangsi.school.user.entity.org.GradeInfo;
import com.jichuangsi.school.user.repository.IGradeInfoExtraRepository;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.List;

@Repository
public class GradeInfoExtraRepositoryImpl implements IGradeInfoExtraRepository {

    @Resource
    private MongoTemplate mongoTemplate;

    @Override
    public List<GradeInfo> findByDeleteFlagAndIdInAndUpdateTimeGreaterThanAndUpdateTimeLessThanOrderByCreateTime(String delete, List<String> ids, long beign, long end, int pageIndex, int pageSize) {
        Criteria criteria = Criteria.where("deleteFlag").is(delete).and("id").in(ids);
        criteria.andOperator(Criteria.where("updateTime").lte(end),Criteria.where("updateTime").gte(beign));
        Query query = new Query(criteria);
        query.with(new Sort(Sort.Direction.DESC,"createTime")).skip((pageIndex-1)*pageSize).limit(pageSize);
        return mongoTemplate.find(query,GradeInfo.class);
    }

    @Override
    public int countByDeleteFlagAndIdInAndUpdateTimeGreaterThanAndUpdateTimeLessThanOrderByCreateTime(String delete, List<String> ids, long beign, long end) {
        Criteria criteria = Criteria.where("deleteFlag").is(delete).and("id").in(ids);
        criteria.andOperator(Criteria.where("updateTime").lte(end),Criteria.where("updateTime").gte(beign));
        Query query = new Query(criteria);
        return (int)mongoTemplate.count(query,GradeInfo.class);
    }
}
