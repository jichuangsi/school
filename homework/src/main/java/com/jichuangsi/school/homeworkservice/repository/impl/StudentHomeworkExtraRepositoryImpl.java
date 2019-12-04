package com.jichuangsi.school.homeworkservice.repository.impl;

import com.jichuangsi.school.homeworkservice.constant.Status;
import com.jichuangsi.school.homeworkservice.entity.Homework;
import com.jichuangsi.school.homeworkservice.entity.StudentHomeworkCollection;
import com.jichuangsi.school.homeworkservice.repository.StudentHomeworkExtraRepository;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

import static org.springframework.data.domain.Sort.Direction.DESC;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.*;

@Repository
public class StudentHomeworkExtraRepositoryImpl implements StudentHomeworkExtraRepository {

    @Resource
    private MongoTemplate mongoTemplate;

    @Override
    public List<Homework> findProgressHomeworkByStudentId(String studentId) {
        /**
         db.getCollection('school_student_homework_collection').aggregate(
         [
         {$match:{"studentId":"430c242713e2466fa87717beca952b96"}},
         {$unwind:{path:"$homeworks",preserveNullAndEmptyArrays:false}},
         {$match:{"homeworks.completedTime":0}},
         {
         $lookup:{
         from: "school_homework",
         localField: "homeworks.homeworkId",
         foreignField: "_id",
         as: "homework"
         }
         },
         {$unwind:{path:"$homework",preserveNullAndEmptyArrays:false}},
         {$match:{"homework.status":"P"}},
         {$sort:{"homework.publishTime":-1}}
         ]
         )
         */
        Aggregation agg = Aggregation.newAggregation(
                match(Criteria.where("studentId").is(studentId)),
                unwind("$homeworks", false),
                //match(Criteria.where("homeworks.completedTime").is(0)),.orOperator(Criteria.where("homework.status")
            //                        .is(Status.PROGRESS.getName()).and("homework.students").in(studentId))
                lookup("school_homework", "homeworks.homeworkId", "_id", "homework"),
                unwind("$homework", false),
                match(Criteria.where("homework.status").is(Status.PROGRESS.getName()).and("homework.students").is(null)),
                sort(DESC, "homework.publishTime"),
                project().and("homework._id").as("_id")
                        .and("homework.name").as("name")
                        .and("homework.info").as("info")
                        .and("homework.status").as("status")
                        .and("homework.teacherId").as("teacherId")
                        .and("homework.teacherName").as("teacherName")
                        .and("homework.classId").as("classId")
                        .and("homework.className").as("className")
                        .and("homework.publishTime").as("publishTime")
                        .and("homework.endTime").as("endTime")
                        .and("homework.createTime").as("createTime")
                        .and("homework.updateTime").as("updateTime")
                        .and("homework.subjectName").as("subjectName")
                        .and("homework.subjectId").as("subjectId")
                        .and("homework.students").as("students")
        );
        return mongoTemplate.aggregate(agg, StudentHomeworkCollection.class, Homework.class).getMappedResults();
    }

    @Override
    public List<Homework> findProgressHomeworkByStudentIds(String studentId) {
        /**
         db.getCollection('school_student_homework_collection').aggregate(
         [
         {$match:{"studentId":"430c242713e2466fa87717beca952b96"}},
         {$unwind:{path:"$homeworks",preserveNullAndEmptyArrays:false}},
         {$match:{"homeworks.completedTime":0}},
         {
         $lookup:{
         from: "school_homework",
         localField: "homeworks.homeworkId",
         foreignField: "_id",
         as: "homework"
         }
         },
         {$unwind:{path:"$homework",preserveNullAndEmptyArrays:false}},
         {$match:{"homework.status":"P"}},
         {$sort:{"homework.publishTime":-1}}
         ]
         )
         */
        Aggregation agg = Aggregation.newAggregation(
            match(Criteria.where("studentId").is(studentId)),
            unwind("$homeworks", false),
            lookup("school_homework", "homeworks.homeworkId", "_id", "homework"),
            unwind("$homework", false),
            match(Criteria.where("homework.status").is(Status.PROGRESS.getName()).and("homework.students").in(studentId)),
            sort(DESC, "homework.publishTime"),
            project().and("homework._id").as("_id")
                .and("homework.name").as("name")
                .and("homework.info").as("info")
                .and("homework.status").as("status")
                .and("homework.teacherId").as("teacherId")
                .and("homework.teacherName").as("teacherName")
                .and("homework.classId").as("classId")
                .and("homework.className").as("className")
                .and("homework.publishTime").as("publishTime")
                .and("homework.endTime").as("endTime")
                .and("homework.createTime").as("createTime")
                .and("homework.updateTime").as("updateTime")
                .and("homework.subjectName").as("subjectName")
                .and("homework.subjectId").as("subjectId")
                .and("homework.students").as("students")
        );
        return mongoTemplate.aggregate(agg, StudentHomeworkCollection.class, Homework.class).getMappedResults();
    }
    @Override
    public int countFinishedHomeworkByStudentId(String studentId) {
        Aggregation agg = Aggregation.newAggregation(
                match(Criteria.where("studentId").is(studentId)),
                unwind("$homeworks", false),
                //match(Criteria.where("homeworks.completedTime").is(0)),
                lookup("school_homework", "homeworks.homeworkId", "_id", "homework"),
                unwind("$homework", false),
                match(Criteria.where("homework.status").in(Arrays.asList(Status.FINISH.getName(), Status.COMPLETED.getName()))
                    .and("homework.students").is(null)));
        return mongoTemplate.aggregate(agg, StudentHomeworkCollection.class, Homework.class).getMappedResults().size();
    }
    @Override
    public List<Homework> findFinishedHomeworkByStudentId(String studentId, int pageNum, int pageSize) {
        Aggregation agg = Aggregation.newAggregation(
            match(Criteria.where("studentId").is(studentId)),
            unwind("$homeworks", false),
            //match(Criteria.where("homeworks.completedTime").is(0)),
            lookup("school_homework", "homeworks.homeworkId", "_id", "homework"),
            unwind("$homework", false),
            match(Criteria.where("homework.status").in(Arrays.asList(Status.FINISH.getName(), Status.COMPLETED.getName()))
                .and("homework.students").is(null)),
            sort(DESC, "homework.publishTime"),
            skip((long) ((pageNum - 1) * pageSize)),
            limit((long) pageSize),
            project().and("homework._id").as("_id")
                .and("homework.name").as("name")
                .and("homework.info").as("info")
                .and("homework.status").as("status")
                .and("homework.teacherId").as("teacherId")
                .and("homework.teacherName").as("teacherName")
                .and("homework.classId").as("classId")
                .and("homework.className").as("className")
                .and("homework.publishTime").as("publishTime")
                .and("homework.endTime").as("endTime")
                .and("homework.createTime").as("createTime")
                .and("homework.updateTime").as("updateTime")
                .and("homework.subjectName").as("subjectName")
                .and("homework.subjectId").as("subjectId")
                .and("homework.students").as("students")
        );
        return mongoTemplate.aggregate(agg, StudentHomeworkCollection.class, Homework.class).getMappedResults();
    }
    @Override
    public List<Homework> findFinishedHomeworkByStudentIds(String studentId, int pageNum, int pageSize) {
        Aggregation agg = Aggregation.newAggregation(
            match(Criteria.where("studentId").is(studentId)),
            unwind("$homeworks", false),
            //match(Criteria.where("homeworks.completedTime").is(0)),
            lookup("school_homework", "homeworks.homeworkId", "_id", "homework"),
            unwind("$homework", false),
            match(Criteria.where("homework.status")
                .in(Arrays.asList(Status.FINISH.getName(), Status.COMPLETED.getName())).and("homework.students").in(studentId)),
            sort(DESC, "homework.publishTime"),
            skip((long) ((pageNum - 1) * pageSize)),
            limit((long) pageSize),
            project().and("homework._id").as("_id")
                .and("homework.name").as("name")
                .and("homework.info").as("info")
                .and("homework.status").as("status")
                .and("homework.teacherId").as("teacherId")
                .and("homework.teacherName").as("teacherName")
                .and("homework.classId").as("classId")
                .and("homework.className").as("className")
                .and("homework.publishTime").as("publishTime")
                .and("homework.endTime").as("endTime")
                .and("homework.createTime").as("createTime")
                .and("homework.updateTime").as("updateTime")
                .and("homework.subjectName").as("subjectName")
                .and("homework.subjectId").as("subjectId")
                .and("homework.students").as("students")
        );
        return mongoTemplate.aggregate(agg, StudentHomeworkCollection.class, Homework.class).getMappedResults();
    }
    @Override
    public int countFinishedHomeworkByStudentIds(String studentId) {
        Aggregation agg = Aggregation.newAggregation(
            match(Criteria.where("studentId").is(studentId)),
            unwind("$homeworks", false),
            //match(Criteria.where("homeworks.completedTime").is(0)),
            lookup("school_homework", "homeworks.homeworkId", "_id", "homework"),
            unwind("$homework", false),
            match(Criteria.where("homework.status")
                .in(Arrays.asList(Status.FINISH.getName(), Status.COMPLETED.getName())).and("homework.students").in(studentId)));
        return mongoTemplate.aggregate(agg, StudentHomeworkCollection.class, Homework.class).getMappedResults().size();
    }
    @Override
    public List<Homework> findFinishedHomeWorkByStudentIdAndEndTime(String studentId, long endTime, String subject) {
        Aggregation agg = Aggregation.newAggregation(
                match(Criteria.where("studentId").is(studentId)),
                unwind("$homeworks", false),
                //match(Criteria.where("homeworks.publishTime").gte(0)),
                lookup("school_homework", "homeworks.homeworkId", "_id", "homework"),
                unwind("$homework", false),
            match(Criteria.where("homework.status").in(Arrays.asList(Status.FINISH.getName(), Status.COMPLETED.getName()))
                .orOperator(Criteria.where("homework.students").is(null),Criteria.where("homework.students").is(studentId))),
               /* match(Criteria.where("homework.status").in(Arrays.asList(Status.FINISH.getName(), Status.COMPLETED.getName()))
                    .and("homework.students").is(null)
                    .orOperator(Criteria.where("homework.status").is(Status.PROGRESS.getName()).and("homework.students").is(studentId))),*/
                match(Criteria.where("homework.endTime").gte(endTime)),
                match(Criteria.where("homework.subjectName").is(subject)),
                sort(DESC, "homework.publishTime"),
                project().and("homework._id").as("_id")
                        .and("homework.name").as("name")
                        .and("homework.info").as("info")
                        .and("homework.status").as("status")
                        .and("homework.teacherId").as("teacherId")
                        .and("homework.teacherName").as("teacherName")
                        .and("homework.classId").as("classId")
                        .and("homework.className").as("className")
                        .and("homework.publishTime").as("publishTime")
                        .and("homework.endTime").as("endTime")
                        .and("homework.createTime").as("createTime")
                        .and("homework.updateTime").as("updateTime")
                        .and("homework.subjectName").as("subjectName")
                        .and("homework.subjectId").as("subjectId")
                        .and("homework.questionIds").as("questionIds")
        );
        return mongoTemplate.aggregate(agg, StudentHomeworkCollection.class, Homework.class).getMappedResults();

    }


}
