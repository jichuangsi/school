package com.jichuangsi.school.courseservice.repository.impl;

import com.jichuangsi.school.courseservice.constant.Status;
import com.jichuangsi.school.courseservice.entity.Course;
import com.jichuangsi.school.courseservice.repository.CourseExtraRepository;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.List;
import java.util.regex.Pattern;

@Repository
public class CourseExtraRepositoryImpl implements CourseExtraRepository {

    @Resource
    private MongoTemplate mongoTemplate;

    @Override
    public List<Course> findCourseByTeacherIdAndStatus(String teacherId) {

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
        query.skip((pageNum - 1) * pageSize).limit(pageSize);
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
    public List<Course> findCourseByClassIdAndStatus(String classId) {

        Criteria criteria = Criteria.where("classId").is(classId).andOperator(Criteria.where("status").ne(Status.FINISH.getName()));
        Query query = new Query(criteria);
        query.with(new Sort(Sort.Direction.ASC, "startTime"));
        return mongoTemplate.find(query, Course.class);
    }

    @Override
    public List<Course> findHistoryCourseByClassIdAndStatus(String classId, int pageNum, int pageSize) {
        Criteria criteria = Criteria.where("classId").is(classId).andOperator(Criteria.where("status").is(Status.FINISH.getName()));
        Query query = new Query(criteria);
        query.with(new Sort(Sort.Direction.DESC, "endTime"));
        query.skip((pageNum - 1) * pageSize).limit(pageSize);
        List<Course> courses = mongoTemplate.find(query, Course.class);
        return courses;

    }

    @Override
    public Course findCourseByTeacherIdAndQuestionId(String teacherId, String questionId) {
        return mongoTemplate.findOne(new Query(Criteria.where("teacherId").is(teacherId).andOperator(Criteria.where("questionIds").is(questionId))), Course.class);
    }

    @Override
    public List<Course> findOneWeekCourseByClassIdAndStatusAndEndTime(String classId, long endTime) {
        Criteria criteria = Criteria.where("classId").is(classId).and("status").is(Status.FINISH.getName()).and("endTime").gte(endTime);
        Query query = new Query(criteria);
        query.with(new Sort(Sort.Direction.DESC, "endTime"));
        return mongoTemplate.find(query, Course.class);
    }
    @Override
    public List<Course> findHistoryCourseByClassIdAndStatusAndEndTimeGreaterThanAndEndTimeLessThan(String classId, int pageNum, int pageSize, long beignTime, long endTime) {

        Criteria criteria = Criteria.where("classId").is(classId).and("status").is(Status.PROGRESS.getName());

        criteria.andOperator(Criteria.where("endTime").lte(endTime), Criteria.where("endTime").gte(beignTime));

        return mongoTemplate.find(new Query(criteria), Course.class);
    }
}
