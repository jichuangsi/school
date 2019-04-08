package com.jichuangsi.school.homeworkservice.repository.impl;

import com.jichuangsi.school.homeworkservice.entity.Homework;
import com.jichuangsi.school.homeworkservice.repository.IHomeworkExtraRepository;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.List;
import java.util.regex.Pattern;

@Repository
public class HomeworkExtraRepositoryImpl implements IHomeworkExtraRepository {
    @Resource
    private MongoTemplate mongoTemplate;

    @Override
    public List<Homework> findByClassIdAndStatusAndEndTimeGreaterThanAndEndTimeLessThanAndSubjectNameLike(String classId, String status, long beginTime, long endTime, String subjectName) {
        Criteria criteria = Criteria.where("classId").is(classId).and("status").is(status);
        if (!StringUtils.isEmpty(subjectName)) {
            Pattern pattern = Pattern.compile("^.*" + subjectName + ".*$", Pattern.CASE_INSENSITIVE);
            criteria.and("subjectName").regex(pattern);
        }
        criteria.andOperator(Criteria.where("endTime").lte(endTime), Criteria.where("endTime").gte(beginTime));
        return mongoTemplate.find(new Query(criteria), Homework.class);
    }
}
