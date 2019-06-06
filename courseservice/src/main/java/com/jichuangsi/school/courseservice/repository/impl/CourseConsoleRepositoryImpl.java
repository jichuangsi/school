package com.jichuangsi.school.courseservice.repository.impl;

import com.jichuangsi.school.courseservice.constant.CourseSort;
import com.jichuangsi.school.courseservice.constant.Status;
import com.jichuangsi.school.courseservice.entity.Course;
import com.jichuangsi.school.courseservice.model.PageHolder;
import com.jichuangsi.school.courseservice.repository.CourseConsoleRepository;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

@Repository
public class CourseConsoleRepositoryImpl<T> implements CourseConsoleRepository<T> {

    @Resource
    private MongoTemplate mongoTemplate;

    //新手小标记
    @Override
    public long findCourseByStartTimeAndEndTime(Long startTime, Long endTime, String teacherId) {
        Criteria c1 = Criteria.where("teacherId").is(teacherId).and("startTime").lte(startTime).and("endTime").gte(startTime);
        Criteria c2 = Criteria.where("teacherId").is(teacherId).and("startTime").lte(endTime).and("endTime").gte(endTime);
        Query query = new Query(new Criteria().orOperator(c1, c2));
        return mongoTemplate.count(query, Course.class);
    }

    @Override
    public List<Course> findCourseByCourseAndKeyWord(Course course, Integer sort, String keyWord, PageHolder page, Long[] time) {
        Criteria criteria = Criteria.where("teacherId").is(course.getTeacherId());/*new Criteria();Criteria.where("startTime").lte(time[1]).gte(time[0]);*/
        if (time != null) {
            criteria.and("startTime").lte(time[1]).gte(time[0]);
        }
        if (!StringUtils.isEmpty(course.getStatus()) && !Status.EMPTY.equals(course.getStatus())) {
            criteria.and("status").is(course.getStatus());
        }
        if (!StringUtils.isEmpty(keyWord)) {
            Pattern pattern = Pattern.compile("^.*" + keyWord + ".*$", Pattern.CASE_INSENSITIVE);
            Criteria c1 = Criteria.where("info").regex(pattern);
            Criteria c2 = Criteria.where("name").regex(pattern);
            criteria.orOperator(c1, c2);
        }
        Query query = new Query(criteria);
        if (sort != null) {
            if (CourseSort.TIME.getName().equals(CourseSort.getName(sort))) {
                query.with(new Sort(Sort.Direction.DESC, "startTime"));
            } else {
                query.with(new Sort(Sort.Direction.DESC, "createTime"));
            }
        }
        query.skip((page.getPageNum() - 1) * page.getPageSize());
        query.limit(page.getPageSize());
        return mongoTemplate.find(query, Course.class);
    }

    @Override
    public T save(T entity) {
        mongoTemplate.insert(entity);
        return entity;
    }

    @Override
    public T save(T entity, List<String> points) {
        mongoTemplate.insert(entity);
        return entity;
    }

    @Override
    public List<Course> findNewCourse(String teacherId) {
        Criteria criteria = Criteria.where("status").is(Status.NOTSTART.getName())
                .and("teacherId").is(teacherId);
        Query query = new Query(criteria);
        query.with(new Sort(Sort.Direction.DESC, "createTime"));
        query.limit(5);
        return mongoTemplate.find(query, Course.class);
    }

    @Override
    public long findCourseByCourseAndKeyWordCount(Course course, Integer sort, String keyWord, Long[] time) {
        Criteria criteria = Criteria.where("teacherId").is(course.getTeacherId());/*new Criteria();Criteria.where("startTime").lte(time[1]).gte(time[0]);*/
        if (time != null) {
            criteria.and("startTime").lte(time[1]).gte(time[0]);
        }
        if (!StringUtils.isEmpty(course.getStatus())) {
            criteria.and("status").is(course.getStatus());
        }
        if (!StringUtils.isEmpty(keyWord)) {
            Pattern pattern = Pattern.compile("^.*" + keyWord + ".*$", Pattern.CASE_INSENSITIVE);
            Criteria c1 = Criteria.where("info").regex(pattern);
            Criteria c2 = Criteria.where("className").regex(pattern);
            criteria.orOperator(c1, c2);
        }
        Query query = new Query(criteria);
        /*query.with(new Sort(Sort.Direction.DESC,"createTime"));
        if (CourseSort.TIME.getName().equals(CourseSort.getName(sort))){
            query.with(new Sort(Sort.Direction.DESC,"startTime"));
        }*/
        return mongoTemplate.count(query, Course.class);
    }

    @Override
    public void updateCourseById(Course updatedCourse) {
        Criteria criteria = Criteria.where("_id").is(updatedCourse.getId());
        Update update = new Update();
        if (!StringUtils.isEmpty(updatedCourse.getName()))
            update.set("name", updatedCourse.getName());
        if (!StringUtils.isEmpty(updatedCourse.getClassId()))
            update.set("classId", updatedCourse.getClassId());
        if (!StringUtils.isEmpty(updatedCourse.getClassName()))
            update.set("className", updatedCourse.getClassName());
        if (updatedCourse.getStartTime() != 0)
            update.set("startTime", updatedCourse.getStartTime());
        if (updatedCourse.getEndTime() != 0)
            update.set("endTime", updatedCourse.getEndTime());
        if (!StringUtils.isEmpty(updatedCourse.getInfo()))
            update.set("info", updatedCourse.getInfo());
        if (!StringUtils.isEmpty(updatedCourse.getPicAddress()))
            update.set("picAddress", updatedCourse.getPicAddress());
        update.set("updateTime", new Date().getTime());
        mongoTemplate.updateFirst(new Query(criteria), update, Course.class);
    }

    @Override
    public List<Course> findByClassIdAndStatusAndEndTimeGreaterThanAndEndTimeLessThanAndSubjectNameLike(String classId, String status, long beignTime, long endTime, String subjectName) {
        Criteria criteria = Criteria.where("classId").is(classId).and("status").is(status);
        if (!StringUtils.isEmpty(subjectName)) {
            Pattern pattern = Pattern.compile("^.*" + subjectName + ".*$", Pattern.CASE_INSENSITIVE);
            criteria.and("subjectName").regex(pattern);
        }
        criteria.andOperator(Criteria.where("endTime").lte(endTime), Criteria.where("endTime").gte(beignTime));
        return mongoTemplate.find(new Query(criteria), Course.class);
    }
}
