package com.jichuangsi.school.user.service.impl;

import com.jichuangsi.school.user.constant.MyResultCode;
import com.jichuangsi.school.user.entity.org.ClassInfo;
import com.jichuangsi.school.user.entity.org.GradeInfo;
import com.jichuangsi.school.user.entity.org.SchoolInfo;
import com.jichuangsi.school.user.model.org.Class;
import com.jichuangsi.school.user.exception.ClassServiceException;
import com.jichuangsi.school.user.service.ISchoolClassService;
import com.jichuangsi.school.user.util.MappingEntity2ModelConverter;
import com.jichuangsi.school.user.util.MappingModel2EntityConverter;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.List;

@Service
public class SchoolClassServiceImpl implements ISchoolClassService {
    @Resource
    private MongoTemplate mongoTemplate;

    @Override
    public void saveOrUpClass(String schoolId, String gradeId, Class classModel) throws ClassServiceException {
        if(StringUtils.isEmpty(schoolId) || StringUtils.isEmpty(gradeId)) throw new ClassServiceException(MyResultCode.PARAM_MISS_MSG);
        SchoolInfo schoolInfo = mongoTemplate.findOne(new Query(Criteria.where("id").is(schoolId).andOperator(Criteria.where("gradeIds").is(gradeId))), SchoolInfo.class);
        if(schoolInfo == null) new ClassServiceException(MyResultCode.SCHOOL_GRADE_NOT_MATCH);
        ClassInfo classInfo = MappingModel2EntityConverter.ConvertClass(classModel);
        mongoTemplate.save(classInfo);
        GradeInfo gradeInfo = mongoTemplate.findOne(new Query(Criteria.where("id").is(gradeId).andOperator(Criteria.where("classIds").is(classInfo.getId()))), GradeInfo.class);
        if(gradeInfo == null){
            gradeInfo = mongoTemplate.findAndModify(new Query(Criteria.where("id").in(gradeId)), new Update().push("classIds", classInfo.getId()), GradeInfo.class);
            if(gradeInfo == null) new ClassServiceException(MyResultCode.GRADE_CLASS_NOT_SYNC);
        }
    }

    @Override
    public void deleteClass(String schoolId, String gradeId, String classId) throws ClassServiceException{
        SchoolInfo schoolInfo = mongoTemplate.findOne(new Query(Criteria.where("id").is(schoolId).andOperator(Criteria.where("gradeIds").is(gradeId))), SchoolInfo.class);
        if(schoolInfo == null) new ClassServiceException(MyResultCode.SCHOOL_GRADE_NOT_MATCH);
        GradeInfo gradeInfo = mongoTemplate.findAndModify(new Query(Criteria.where("id").in(gradeId)), new Update().pull("classIds", classId), GradeInfo.class);
        if(gradeInfo == null) new ClassServiceException(MyResultCode.GRADE_CLASS_NOT_SYNC);
        ClassInfo classInfo = mongoTemplate.findAndRemove(new Query(Criteria.where("id").in(classId)), ClassInfo.class);
        if(classInfo == null) new ClassServiceException(MyResultCode.CLASS_FAIL2REMOVE);
    }

    @Override
    public Class getClassInfo(String schoolId, String gradeId, String classId) throws ClassServiceException{
        SchoolInfo schoolInfo = mongoTemplate.findOne(new Query(Criteria.where("id").is(schoolId).andOperator(Criteria.where("gradeIds").is(gradeId))), SchoolInfo.class);
        if(schoolInfo == null) new ClassServiceException(MyResultCode.SCHOOL_GRADE_NOT_MATCH);
        GradeInfo gradeInfo = mongoTemplate.findOne(new Query(Criteria.where("id").is(gradeId).andOperator(Criteria.where("classIds").is(classId))), GradeInfo.class);
        if(gradeInfo == null) new ClassServiceException(MyResultCode.GRADE_CLASS_NOT_MATCH);
        return MappingEntity2ModelConverter.TransferClass(mongoTemplate.findById(classId,ClassInfo.class));
    }
}
