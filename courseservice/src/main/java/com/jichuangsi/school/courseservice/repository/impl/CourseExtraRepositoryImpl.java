package com.jichuangsi.school.courseservice.repository.impl;

import com.jichuangsi.school.courseservice.constant.Status;
import com.jichuangsi.school.courseservice.entity.Course;
import com.jichuangsi.school.courseservice.entity.Question;
import com.jichuangsi.school.courseservice.repository.CourseExtraRepository;
import com.jichuangsi.school.courseservice.repository.CourseRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

import static com.mongodb.operation.OrderBy.DESC;
import static org.springframework.data.domain.Sort.Direction.ASC;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.*;

@Repository
public class CourseExtraRepositoryImpl implements CourseExtraRepository {

    @Resource
    private MongoTemplate mongoTemplate;

    @Override
    public List<Course> findCourseByTeacherIdAndStatus(String teacherId){

        Criteria criteria = Criteria.where("teacherId").is(teacherId).andOperator(Criteria.where("status").ne(Status.FINISH.getName()));
        Query query = new Query(criteria);
        query.with(new Sort(Sort.Direction.ASC, "startTime"));
        return mongoTemplate.find(query, Course.class);
    }

    @Override
    public List<Course> findHistoryCourseByTeacherIdAndStatus(String teacherId, int pageNum, int pageSize) {
        Criteria criteria = Criteria.where("teacherId").is(teacherId).andOperator(Criteria.where("status").is(Status.FINISH.getName()));
        Query query = new Query(criteria);
        query.with(new Sort(Sort.Direction.DESC, "endTime"));
        query.skip((pageNum-1)*pageSize).limit(pageSize);
        return mongoTemplate.find(query, Course.class);
    }

    @Override
    public List<Course> findCourseByTeacherIdAndConditions(String teacherId, Course course) {
        Criteria criteria = Criteria.where("teacherId").is(teacherId).andOperator(Criteria.where("name").regex(course.getName()));
        Query query = new Query(criteria);
        query.with(new Sort(Sort.Direction.DESC, "startTime"));
        return mongoTemplate.find(query, Course.class);
    }

    @Override
    public List<Course> findCourseByClassIdAndStatus(String classId){

        Criteria criteria = Criteria.where("classId").is(classId).andOperator(Criteria.where("status").ne(Status.FINISH.getName()));
        Query query = new Query(criteria);
        query.with(new Sort(Sort.Direction.ASC, "startTime"));
        return mongoTemplate.find(query, Course.class);
    }

    @Override
    public List<Course> findHistoryCourseByClassIdAndStatus(String classId, int pageNum, int pageSize){
        Criteria criteria = Criteria.where("classId").is(classId).andOperator(Criteria.where("status").is(Status.FINISH.getName()));
        Query query = new Query(criteria);
        query.with(new Sort(Sort.Direction.DESC, "endTime"));
        query.skip((pageNum-1)*pageSize).limit(pageSize);
        return mongoTemplate.find(query, Course.class);
    }
}
