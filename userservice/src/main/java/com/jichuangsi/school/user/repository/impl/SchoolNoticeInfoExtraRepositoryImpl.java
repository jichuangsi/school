package com.jichuangsi.school.user.repository.impl;

import com.jichuangsi.school.user.entity.backstage.SchoolNoticeInfo;
import com.jichuangsi.school.user.repository.ISchoolNoticeInfoExtraRepository;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.List;

@Repository
public class SchoolNoticeInfoExtraRepositoryImpl implements ISchoolNoticeInfoExtraRepository {
    @Resource
    private MongoTemplate mongoTemplate;

    @Override
    public List<SchoolNoticeInfo> pageBySchoolIdAndDeleteFlagOrderByCreatedTimeDesc(String schoolId, String delete,int pageIndex,int pageSize) {
        Criteria criteria = Criteria.where("schoolId").is(schoolId).and("deleteFlag").is(delete);
        Query query = new Query(criteria);
        query.with(new Sort(Sort.Direction.DESC,"createdTime")).limit(pageSize).skip((pageIndex-1)*pageSize);
        return mongoTemplate.find(query,SchoolNoticeInfo.class);
    }
}
