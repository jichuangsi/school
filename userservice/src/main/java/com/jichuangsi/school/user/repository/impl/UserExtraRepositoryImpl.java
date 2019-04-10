package com.jichuangsi.school.user.repository.impl;

import com.jichuangsi.school.user.constant.Status;
import com.jichuangsi.school.user.entity.UserInfo;
import com.jichuangsi.school.user.repository.IUserExtraRepository;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.List;

@Repository
public class UserExtraRepositoryImpl implements IUserExtraRepository {

    @Resource
    private MongoTemplate mongoTemplate;

    @Override
    public List<UserInfo> findByRoleInfos(String classId) {
        Criteria criteria1 = Criteria.where("primaryClass.classId").is(classId).and("roleName").is("Teacher");
        Criteria criteria2 = Criteria.where("secondaryClasses").elemMatch(Criteria.where("classId").is(classId)).and("roleName").is("Teacher");
        Criteria criteria3 = new Criteria().orOperator(criteria1,criteria2);
        Criteria criteria = Criteria.where("roleInfos").elemMatch(criteria3).and("status").is(Status.ACTIVATE.getName());
        Query query = new Query(criteria);
        return mongoTemplate.find(query,UserInfo.class);
    }

    @Override
    public List<UserInfo> findBySchoolId(String schoolId) {
        Criteria criteria1 = Criteria.where("school.schoolId").is(schoolId).and("roleName").is("Teacher");
        Criteria criteria = Criteria.where("roleInfos").elemMatch(criteria1).and("status").is(Status.ACTIVATE.getName());
        Query query = new Query(criteria);
        return mongoTemplate.find(query,UserInfo.class);
    }
}
