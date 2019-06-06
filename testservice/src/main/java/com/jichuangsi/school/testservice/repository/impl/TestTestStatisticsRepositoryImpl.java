package com.jichuangsi.school.testservice.repository.impl;

import com.jichuangsi.school.testservice.constant.Status;
import com.jichuangsi.school.testservice.entity.Test;
import com.jichuangsi.school.testservice.repository.TestTestStatisticsRepository;
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
public class TestTestStatisticsRepositoryImpl implements TestTestStatisticsRepository {

    @Resource
    private MongoTemplate mongoTemplate;


    @Override
    public List<Test> findByClassIdAndStatusAndEndTimeGreaterThanAndEndTimeLessThanAndSubjectNameLike(String classId, String status, long beignTime, long endTime, String subjectName) {
        Criteria criteria = Criteria.where("classId").is(classId).and("status").is(status);
        if (!StringUtils.isEmpty(subjectName)) {
            Pattern pattern = Pattern.compile("^.*" + subjectName + ".*$", Pattern.CASE_INSENSITIVE);
            criteria.and("subjectName").regex(pattern);
        }
        criteria.andOperator(Criteria.where("endTime").lte(endTime), Criteria.where("endTime").gte(beignTime));

        return mongoTemplate.find(new Query(criteria), Test.class);
    }
}
