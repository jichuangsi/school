package com.jichuangsi.school.parents.repository.impl;

import com.jichuangsi.school.parents.entity.ParentMessage;
import com.jichuangsi.school.parents.repository.IParentMessageExtraRepository;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.List;

@Repository
public class ParentMessageRepositoryImpl implements IParentMessageExtraRepository {

    @Resource
    private MongoTemplate mongoTemplate;

    @Override
    public List<ParentMessage> findByParentIdAndTeacherIdAndSort(String parentId, String teacherId, int pageIndex, int pageSize) {
        Criteria criteria = Criteria.where("parentId").is(parentId).and("teacherId").is(teacherId);
        Query query = new Query(criteria).with(new Sort(Sort.Direction.ASC,"createdTime")).skip((pageIndex-1)*pageSize).limit(pageSize);
        return mongoTemplate.find(query,ParentMessage.class);
    }
}
