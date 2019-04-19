package com.jichuangsi.school.statistics.service.impl;

import com.jichuangsi.school.statistics.entity.StudentStatisticsEntity;
import com.jichuangsi.school.statistics.entity.performance.student.CoursePerformanceEntity;
import com.jichuangsi.school.statistics.model.result.performance.StudentCoursePerformanceMessageModel;
import com.jichuangsi.school.statistics.service.IStudentStatisticsService;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

@Service
public class StudentStatisticsServiceImpl implements IStudentStatisticsService {

    @Resource
    private MongoTemplate mongoTemplate;

    public StudentCoursePerformanceMessageModel saveStudentCoursePerformance(StudentCoursePerformanceMessageModel performance){
        final String courseId = performance.getCourseId();
        final String studentId = performance.getStudentId();

        // 更新作答信息，考虑到同步问题，不能直接用Repository找然后更新，需要使用upsert
        Query query = new Query();
        query.addCriteria(Criteria.where("studentId").is(studentId).and("coursePerformance.courseId").is(courseId));
        final String lock = courseId + "-" + studentId;
        // 防止同一个并发用户并发重复加入，todo集群需要用分布式锁
        synchronized (lock.intern()) {
            StudentStatisticsEntity studentStatisticsEntity = mongoTemplate.findOne(query, StudentStatisticsEntity.class);
            Update update = new Update();


            if (null == studentStatisticsEntity) {
                Query query1 = new Query();
                query1.addCriteria(Criteria.where("studentId").is(studentId));

                update.set("studentId", performance.getStudentId());
                update.set("studentName", performance.getStudentName());
                update.set("createdTime", new Date().getTime());
                CoursePerformanceEntity cp = new CoursePerformanceEntity();
                cp.setCourseId(courseId);
                cp.setCourseName(performance.getCourseName());
                cp.setTeacherId(performance.getTeacherId());
                cp.setTeacherName(performance.getTeacherName());
                cp.setCommend(performance.getCommend());

                // addToSet存在则不加，不存在则加,push不管是否存在都加，这里用addToSet
                update.addToSet("coursePerformance", cp);
                mongoTemplate.upsert(query1, update, StudentStatisticsEntity.class);
            } else {
                CoursePerformanceEntity performanceEntity = null;
                List<CoursePerformanceEntity> coursePerformances = studentStatisticsEntity.getCoursePerformance();

                for (CoursePerformanceEntity temp : coursePerformances) {
                    if (temp.getCourseId().equals(courseId)) {
                        performanceEntity = temp;
                        break;
                    }
                }
                if (null != performanceEntity) {
                    update.set("updatedTime", new Date().getTime());
                    int commend = performanceEntity.getCommend() + performance.getCommend();
                    update.set("coursePerformance.$.commend", commend);
                    mongoTemplate.updateFirst(query, update, StudentStatisticsEntity.class);
                }
            }
        }
        return performance;
    }
}
