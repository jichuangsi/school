package com.jichuangsi.school.courseservice.repository.impl;

import com.jichuangsi.school.courseservice.constant.Status;
import com.jichuangsi.school.courseservice.entity.Course;
import com.jichuangsi.school.courseservice.entity.Question;
import com.jichuangsi.school.courseservice.repository.QuestionExtraRepository;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.query.Criteria;

import javax.annotation.Resource;
import java.util.List;

import static org.springframework.data.domain.Sort.Direction.ASC;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.*;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.project;

public class QuestionExtraRepositoryImpl implements QuestionExtraRepository {

    @Resource
    private MongoTemplate mongoTemplate;

    @Override
    public List<Question> findQuestionsByTeacherIdAndCourseId(String teacherId, String courseId) {
        /**
         db.getCollection('school_course').aggregate(
         [
         {$match:{$and:[{"_id":"fa015ba0052241e483d86465373845bc"},{"teacherId":"123"}]}},
         {$unwind:{path:"$questionIds",includeArrayIndex:"questionIndex",preserveNullAndEmptyArrays:false}},
         {
         $lookup:{
         from: "school_course_question",
         localField: "questionIds",
         foreignField: "_id",
         as: "question"
         }
         },
         {$unwind:{path:"$question",preserveNullAndEmptyArrays:false}}
         ]
         )
         */
        Aggregation agg = Aggregation.newAggregation(
                match(Criteria.where("_id").is(courseId).andOperator(Criteria.where("teacherId").is(teacherId))),
                unwind("questionIds","questionIndex", false),
                lookup("school_course_question","questionIds","_id","question"),
                unwind("question", false),
                sort(ASC, "questionIndex"),
                project().and("question._id").as("_id")
                        .and("question.content").as("content")
                        .and("question.options").as("options")
                        .and("question.answer").as("answer")
                        .and("question.answerDetail").as("answerDetail")
                        .and("question.parse").as("parse")
                        .and("question.type").as("type")
                        .and("question.typeInCN").as("typeInCN")
                        .and("question.difficulty").as("difficulty")
                        .and("question.subjectId").as("subjectId")
                        .and("question.gradeId").as("gradeId")
                        .and("question.knowledge").as("knowledge")
                        .and("question.idMD52").as("idMD52")
                        .and("question.status").as("status")
                        .and("question.pic").as("pic")
        );
        return mongoTemplate.aggregate(agg, Course.class,Question.class).getMappedResults();
    }

    @Override
    public List<Question> findQuestionsByClassIdAndCourseId(String classId, String courseId) {
        Aggregation agg = Aggregation.newAggregation(
                match(Criteria.where("_id").is(courseId).andOperator(Criteria.where("classId").is(classId))),
                unwind("questionIds","questionIndex", false),
                lookup("school_course_question","questionIds","_id","question"),
                //match(Criteria.where("question.status").is(Status.FINISH.getName())),
                unwind("question", false),
                sort(ASC, "questionIndex"),
                project().and("question._id").as("_id")
                        .and("question.content").as("content")
                        .and("question.options").as("options")
                        .and("question.answer").as("answer")
                        .and("question.answerDetail").as("answerDetail")
                        .and("question.parse").as("parse")
                        .and("question.type").as("type")
                        .and("question.typeInCN").as("typeInCN")
                        .and("question.difficulty").as("difficulty")
                        .and("question.subjectId").as("subjectId")
                        .and("question.gradeId").as("gradeId")
                        .and("question.knowledge").as("knowledge")
                        .and("question.idMD52").as("idMD52")
                        .and("question.status").as("status")
                        .and("question.pic").as("pic")
        );
        return mongoTemplate.aggregate(agg, Course.class,Question.class).getMappedResults();
    }
}
