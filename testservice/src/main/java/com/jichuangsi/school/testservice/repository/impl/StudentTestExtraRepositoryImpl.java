package com.jichuangsi.school.testservice.repository.impl;

import com.jichuangsi.school.testservice.constant.Status;
import com.jichuangsi.school.testservice.entity.Test;
import com.jichuangsi.school.testservice.entity.StudentTestCollection;
import com.jichuangsi.school.testservice.repository.StudentTestExtraRepository;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.query.Criteria;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;

import static org.springframework.data.domain.Sort.Direction.DESC;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.*;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.project;

public class StudentTestExtraRepositoryImpl implements StudentTestExtraRepository {

    @Resource
    private MongoTemplate mongoTemplate;

    @Override
    public List<Test> findProgressTestByStudentId(String studentId){
        /**
         db.getCollection('school_student_test_collection').aggregate(
         [
         {$match:{"studentId":"430c242713e2466fa87717beca952b96"}},
         {$unwind:{path:"$tests",preserveNullAndEmptyArrays:false}},
         {$match:{"tests.completedTime":0}},
         {
         $lookup:{
         from: "school_test",
         localField: "tests.testId",
         foreignField: "_id",
         as: "test"
         }
         },
         {$unwind:{path:"$test",preserveNullAndEmptyArrays:false}},
         {$match:{"test.status":"P"}},
         {$sort:{"test.publishTime":-1}}
         ]
         )
         */
        Aggregation agg = Aggregation.newAggregation(
                match(Criteria.where("studentId").is(studentId)),
                unwind("$tests", false),
                //match(Criteria.where("tests.completedTime").is(0)),
                lookup("school_test","tests.testId","_id","test"),
                unwind("$test", false),
                match(Criteria.where("test.status").is(Status.PROGRESS.getName())),
                sort(DESC, "test.publishTime"),
                project().and("test._id").as("_id")
                        .and("test.name").as("name")
                        .and("test.info").as("info")
                        .and("test.status").as("status")
                        .and("test.teacherId").as("teacherId")
                        .and("test.teacherName").as("teacherName")
                        .and("test.classId").as("classId")
                        .and("test.className").as("className")
                        .and("test.publishTime").as("publishTime")
                        .and("test.endTime").as("endTime")
                        .and("test.createTime").as("createTime")
                        .and("test.updateTime").as("updateTime")
                        .and("test.subjectName").as("subjectName")
                        .and("test.subjectId").as("subjectId")
        );
        return mongoTemplate.aggregate(agg, StudentTestCollection.class,Test.class).getMappedResults();
    }

    @Override
    public int countFinishedTestByStudentId(String studentId){
        Aggregation agg = Aggregation.newAggregation(
                match(Criteria.where("studentId").is(studentId)),
                unwind("$tests", false),
                //match(Criteria.where("tests.completedTime").is(0)),
                lookup("school_test","tests.testId","_id","test"),
                unwind("$test", false),
                match(Criteria.where("test.status").in(Arrays.asList(Status.FINISH.getName(),Status.COMPLETED.getName()))));
        return mongoTemplate.aggregate(agg, StudentTestCollection.class,Test.class).getMappedResults().size();
    }

    @Override
    public List<Test> findFinishedTestByStudentId(String studentId, int pageNum, int pageSize){
        Aggregation agg = Aggregation.newAggregation(
                match(Criteria.where("studentId").is(studentId)),
                unwind("$tests", false),
                //match(Criteria.where("tests.completedTime").is(0)),
                lookup("school_test","tests.testId","_id","test"),
                unwind("$test", false),
                match(Criteria.where("test.status").in(Arrays.asList(Status.FINISH.getName(),Status.COMPLETED.getName()))),
                sort(DESC, "test.publishTime"),
                skip((long)((pageNum-1)*pageSize)),
                limit((long)pageSize),
                project().and("test._id").as("_id")
                        .and("test.name").as("name")
                        .and("test.info").as("info")
                        .and("test.status").as("status")
                        .and("test.teacherId").as("teacherId")
                        .and("test.teacherName").as("teacherName")
                        .and("test.classId").as("classId")
                        .and("test.className").as("className")
                        .and("test.publishTime").as("publishTime")
                        .and("test.endTime").as("endTime")
                        .and("test.createTime").as("createTime")
                        .and("test.updateTime").as("updateTime")
                        .and("test.subjectName").as("subjectName")
                        .and("test.subjectId").as("subjectId")
        );
        return mongoTemplate.aggregate(agg, StudentTestCollection.class,Test.class).getMappedResults();
    }
}
