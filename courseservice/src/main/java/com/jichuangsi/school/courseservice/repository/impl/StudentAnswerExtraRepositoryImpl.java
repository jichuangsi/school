package com.jichuangsi.school.courseservice.repository.impl;

import com.jichuangsi.school.courseservice.entity.Course;
import com.jichuangsi.school.courseservice.entity.Question;
import com.jichuangsi.school.courseservice.entity.StudentAnswer;
import com.jichuangsi.school.courseservice.repository.StudentAnswerExtraRepository;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.data.domain.Sort.Direction.ASC;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.*;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.project;

public class StudentAnswerExtraRepositoryImpl implements StudentAnswerExtraRepository {

    @Resource
    private MongoTemplate mongoTemplate;

    @Override
    public List<Question> findAllBySubjectIdAndKnowledgeIdAndStudentIdAndResult(String subjectId, String knowledgeId, String studentId, String result){

        /*
        db.getCollection('school_course_student_answer').aggregate(
         [
         {$match:{$and:[{"studentId":"430c242713e2466fa87717beca952b96"},{"result":"W"}]}},
         //{$unwind:{path:"$questionIds",includeArrayIndex:"questionIndex",preserveNullAndEmptyArrays:false}},
         {
         $lookup:{
         from: "school_course_question",
         localField: "questionId",
         foreignField: "_id",
         as: "question"
         }
         },
         {$unwind:{path:"$question",preserveNullAndEmptyArrays:false}},
         {$match:{$and:[{"question.subjectId":"2"},{"question.knowledgeId":{$ne:null}}]}}
         ]
         )
         */
        List<AggregationOperation> operations= new ArrayList<AggregationOperation>();
        operations.add(match(Criteria.where("studentId").is(studentId).andOperator(Criteria.where("result").is(result))));
        operations.add(lookup("school_course_question","questionId","_id","question"));
        operations.add(unwind("question", false));
        operations.add(match(Criteria.where("question.knowledgeId").ne(null)));
        if(!StringUtils.isEmpty(subjectId)) operations.add(match(Criteria.where("question.subjectId").is(subjectId)));
        if(!StringUtils.isEmpty(knowledgeId)) operations.add(match(Criteria.where("question.knowledgeId").is(knowledgeId)));
        operations.add(project().and("question._id").as("_id")
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
                .and("question.knowledgeId").as("knowledgeId")
                .and("question.capability").as("capability")
                .and("question.capabilityId").as("capabilityId")
                .and("question.idMD52").as("idMD52")
                .and("question.status").as("status")
                .and("question.pic").as("pic"));

        Aggregation agg = Aggregation.newAggregation(operations
                /*match(Criteria.where("studentId").is(studentId).andOperator(Criteria.where("result").is(result))),
                lookup("school_course_question","questionId","_id","question"),
                unwind("question", false),
                match(Criteria.where("question.subjectId").is(subjectId)),
                match(Criteria.where("question.knowledgeId").is(knowledgeId)),
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
                        .and("question.knowledgeId").as("knowledgeId")
                        .and("question.capability").as("capability")
                        .and("question.capabilityId").as("capabilityId")
                        .and("question.idMD52").as("idMD52")
                        .and("question.status").as("status")
                        .and("question.pic").as("pic")*/
        );
        return mongoTemplate.aggregate(agg, StudentAnswer.class, Question.class).getMappedResults();
    }
}
