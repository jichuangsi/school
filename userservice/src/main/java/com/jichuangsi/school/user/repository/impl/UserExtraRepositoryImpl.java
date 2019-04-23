package com.jichuangsi.school.user.repository.impl;

import com.jichuangsi.school.user.constant.Status;
import com.jichuangsi.school.user.entity.UserInfo;
import com.jichuangsi.school.user.repository.IUserExtraRepository;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.List;
import java.util.regex.Pattern;

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
    public List<UserInfo> findBySchoolId(String schoolId,int pageIndex,int pageSize) {
        Criteria criteria1 = Criteria.where("school.schoolId").is(schoolId).and("roleName").is("Teacher");
        Criteria criteria = Criteria.where("roleInfos").elemMatch(criteria1).and("status").is(Status.ACTIVATE.getName());
        Query query = new Query(criteria);
        query.skip((pageIndex-1)*pageSize).limit(pageSize).with(new Sort(Sort.Direction.ASC,"createTime"));
        return mongoTemplate.find(query,UserInfo.class);
    }

    @Override
    public int countBySchoolId(String schoolId) {
        Criteria criteria1 = Criteria.where("school.schoolId").is(schoolId).and("roleName").is("Teacher");
        Criteria criteria = Criteria.where("roleInfos").elemMatch(criteria1).and("status").is(Status.ACTIVATE.getName());
        Query query = new Query(criteria);
        return (int)mongoTemplate.count(query,UserInfo.class);
    }

    @Override
    public int findByPrimaryClassAndTeacher(String classId) {
        Criteria criteria1 = Criteria.where("primaryClass.classId").is(classId).and("roleName").is("Teacher");
        Criteria criteria = Criteria.where("roleInfos").elemMatch(criteria1).and("status").is(Status.ACTIVATE.getName());
        return (int) mongoTemplate.count(new Query(criteria),UserInfo.class);
    }

    @Override
    public List<UserInfo> findByCondition(String schoolId, String pharseId, String gradeId, String classId, String userName, String roleName,String subjectId,int pageIndex,int pageSize) {
        Criteria criteria = Criteria.where("school.schoolId").is(schoolId).and("roleName").is(roleName);
        if (!StringUtils.isEmpty(pharseId)){
            criteria.and("phrase.pid").is(pharseId);
        }
        if ("Teacher".equals(roleName)){
            Criteria criteriaC = new Criteria();
            if (!StringUtils.isEmpty(classId)) {
                Criteria criteria1 = Criteria.where("primaryClass.classId").is(classId);
                Criteria criteria2 = Criteria.where("secondaryClasses").elemMatch(Criteria.where("classId").is(classId));
                criteriaC.orOperator(criteria1,criteria2);
            }
            Criteria criteriaG = new Criteria();
            if (!StringUtils.isEmpty(gradeId)){
                Criteria criteria1 = Criteria.where("primaryGrade.gradeId").is(gradeId);
                Criteria criteria2 = Criteria.where("secondaryGrades").elemMatch(Criteria.where("gradeId").is(gradeId));
                criteriaG.orOperator(criteria1,criteria2);
            }
            Criteria criteriaS = new Criteria();
            if (!StringUtils.isEmpty(subjectId)){
                Criteria criteria1 = Criteria.where("primarySubject.subjectId").is(subjectId);
                Criteria criteria2 = Criteria.where("secondarySubjects").elemMatch(Criteria.where("subjectId").is(subjectId));
                criteriaS.orOperator(criteria1,criteria2);
            }
            criteria.andOperator(criteriaS,criteriaC,criteriaG);
        }else {
            if (!StringUtils.isEmpty(classId)) {
                criteria.and("primaryClass.classId").is(classId);
            }
            if (!StringUtils.isEmpty(gradeId)){
                criteria.and("primaryGrade.gradeId").is(gradeId);
            }
        }
        Criteria criteria1 = Criteria.where("roleInfos").elemMatch(criteria).and("status").is(Status.ACTIVATE.getName());
        if (!StringUtils.isEmpty(userName)) {
            Pattern pattern = Pattern.compile("^.*" + userName + ".*$", Pattern.CASE_INSENSITIVE);
            criteria1.and("name").regex(pattern);
        }
        Query query = new Query(criteria1);
        query.skip((pageIndex-1)*pageSize).limit(pageSize).with(new Sort(Sort.Direction.ASC,"createTime"));
        return mongoTemplate.find(query,UserInfo.class);
    }

    @Override
    public int countByCondition(String schoolId, String pharseId, String gradeId, String classId, String userName, String roleName, String subjectId) {
        Criteria criteria = Criteria.where("school.schoolId").is(schoolId).and("roleName").is(roleName);
        if (!StringUtils.isEmpty(pharseId)){
            criteria.and("phrase.pid").is(pharseId);
        }
        if ("Teacher".equals(roleName)){
            Criteria criteriaC = new Criteria();
            if (!StringUtils.isEmpty(classId)) {
                Criteria criteria1 = Criteria.where("primaryClass.classId").is(classId);
                Criteria criteria2 = Criteria.where("secondaryClasses").elemMatch(Criteria.where("classId").is(classId));
                criteriaC.orOperator(criteria1,criteria2);
            }
            Criteria criteriaG = new Criteria();
            if (!StringUtils.isEmpty(gradeId)){
                Criteria criteria1 = Criteria.where("primaryGrade.gradeId").is(gradeId);
                Criteria criteria2 = Criteria.where("secondaryGrades").elemMatch(Criteria.where("gradeId").is(gradeId));
                criteriaG.orOperator(criteria1,criteria2);
            }
            Criteria criteriaS = new Criteria();
            if (!StringUtils.isEmpty(subjectId)){
                Criteria criteria1 = Criteria.where("primarySubject.subjectId").is(subjectId);
                Criteria criteria2 = Criteria.where("secondarySubjects").elemMatch(Criteria.where("subjectId").is(subjectId));
                criteriaS.orOperator(criteria1,criteria2);
            }
            criteria.andOperator(criteriaS,criteriaC,criteriaG);
        }else {
            if (!StringUtils.isEmpty(classId)) {
                criteria.and("primaryClass.classId").is(classId);
            }
            if (!StringUtils.isEmpty(gradeId)){
                criteria.and("primaryGrade.gradeId").is(gradeId);
            }
        }
        Criteria criteria1 = Criteria.where("roleInfos").elemMatch(criteria).and("status").is(Status.ACTIVATE.getName());
        if (!StringUtils.isEmpty(userName)) {
            Pattern pattern = Pattern.compile("^.*" + userName + ".*$", Pattern.CASE_INSENSITIVE);
            criteria1.and("name").regex(pattern);
        }
        return (int)mongoTemplate.count(new Query(criteria1),UserInfo.class);
    }

    @Override
    public List<UserInfo> findByConditions(String schoolId, List<String> classIds, String userName, String roleName, String subjectId, int pageIndex, int pageSize) {
        Criteria criteria = Criteria.where("school.schoolId").is(schoolId).and("roleName").is(roleName);
        if ("Teacher".equals(roleName)){
            Criteria criteriaC = new Criteria();
            if ((classIds.size() > 0)) {
                Criteria criteria1 = Criteria.where("primaryClass.classId").in(classIds);
                Criteria criteria2 = Criteria.where("secondaryClasses").elemMatch(Criteria.where("classId").in(classIds));
                criteriaC.orOperator(criteria1,criteria2);
            }
            Criteria criteriaS = new Criteria();
            if (!StringUtils.isEmpty(subjectId)){
                Criteria criteria1 = Criteria.where("primarySubject.subjectId").is(subjectId);
                Criteria criteria2 = Criteria.where("secondarySubjects").elemMatch(Criteria.where("subjectId").is(subjectId));
                criteriaS.orOperator(criteria1,criteria2);
            }
            criteria.andOperator(criteriaS,criteriaC);
        }else {
            if (classIds.size() > 0) {
                criteria.and("primaryClass.classId").in(classIds);
            }
        }
        Criteria criteria1 = Criteria.where("roleInfos").elemMatch(criteria).and("status").is(Status.ACTIVATE.getName());
        if (!StringUtils.isEmpty(userName)) {
            Pattern pattern = Pattern.compile("^.*" + userName + ".*$", Pattern.CASE_INSENSITIVE);
            criteria1.and("name").regex(pattern);
        }
        Query query = new Query(criteria1);
        if (pageIndex > 0) {
            query.skip((pageIndex - 1) * pageSize).limit(pageSize).with(new Sort(Sort.Direction.ASC, "createTime"));
        }
        return mongoTemplate.find(query,UserInfo.class);
    }

    @Override
    public int countByConditions(String schoolId, List<String> classIds, String userName, String roleName, String subjectId) {
        Criteria criteria = Criteria.where("school.schoolId").is(schoolId).and("roleName").is(roleName);
        if ("Teacher".equals(roleName)){
            Criteria criteriaC = new Criteria();
            if ((classIds.size() > 0)) {
                Criteria criteria1 = Criteria.where("primaryClass.classId").in(classIds);
                Criteria criteria2 = Criteria.where("secondaryClasses").elemMatch(Criteria.where("classId").in(classIds));
                criteriaC.orOperator(criteria1,criteria2);
            }
            Criteria criteriaS = new Criteria();
            if (!StringUtils.isEmpty(subjectId)){
                Criteria criteria1 = Criteria.where("primarySubject.subjectId").is(subjectId);
                Criteria criteria2 = Criteria.where("secondarySubjects").elemMatch(Criteria.where("subjectId").is(subjectId));
                criteriaS.orOperator(criteria1,criteria2);
            }
            criteria.andOperator(criteriaS,criteriaC);
        }else {
            if (!(classIds.size() > 0)) {
                criteria.and("primaryClass.classId").in(classIds);
            }
        }
        Criteria criteria1 = Criteria.where("roleInfos").elemMatch(criteria).and("status").is(Status.ACTIVATE.getName());
        if (!StringUtils.isEmpty(userName)) {
            Pattern pattern = Pattern.compile("^.*" + userName + ".*$", Pattern.CASE_INSENSITIVE);
            criteria1.and("name").regex(pattern);
        }
        return (int)mongoTemplate.count(new Query(criteria1),UserInfo.class);
    }
}
