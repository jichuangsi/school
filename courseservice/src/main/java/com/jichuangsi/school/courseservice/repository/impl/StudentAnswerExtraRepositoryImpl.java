package com.jichuangsi.school.courseservice.repository.impl;

import com.jichuangsi.school.courseservice.entity.Knowledge;
import com.jichuangsi.school.courseservice.entity.Question;
import com.jichuangsi.school.courseservice.entity.StudentAnswer;
import com.jichuangsi.school.courseservice.repository.StudentAnswerExtraRepository;
import org.bson.Document;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.*;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.*;

public class StudentAnswerExtraRepositoryImpl implements StudentAnswerExtraRepository {

    @Resource
    private MongoTemplate mongoTemplate;

    @Override
    public List<Question> findAllBySubjectIdAndKnowledgeIdAndStudentIdAndResult(String subjectId, String knowledgeId, String studentId, String result){

        /*
        db.getCollection('school_student_answer_collection').aggregate(
         [
         {$match:{$and:[{"studentId":"430c242713e2466fa87717beca952b96"},{"result":"W"}]}},
         //{$unwind:{path:"$questionIds",includeArrayIndex:"questionIndex",preserveNullAndEmptyArrays:false}},
         {
         $lookup:{
         from: "school_question_collection",
         localField: "questionId",
         foreignField: "_id",
         as: "question"
         }
         },
         {$unwind:{path:"$question",preserveNullAndEmptyArrays:false}},
         {$match:{"question.knowledges.0":{$exists:1}}},
         //{$match:{$and:[{"question.subjectId":"2"},{"question.knowledges":{$elemMatch:{"knowledgeId":"3ad1c9adb15a800b629366802923dc93"}}}]}},
         {$match:{"question.subjectId":"2"}},
         {$unwind:{path:"$question.knowledges",preserveNullAndEmptyArrays:false}},
         //{$match:{"question.knowledges.knowledgeId":"3ad1c9adb15a800b629366802923dc93"}}
         { $project: { knowledges: [ "$question.knowledges.knowledgeId", "$question.knowledges.knowledge" ] } }
         ]
         )
         */
        List<AggregationOperation> operations= new ArrayList<AggregationOperation>();
        operations.add(match(Criteria.where("studentId").is(studentId).andOperator(Criteria.where("result").is(result))));
        operations.add(lookup("school_question_collection","questionId","_id","question"));
        operations.add(unwind("question", false));
        operations.add(match(Criteria.where("question.knowledges.0").exists(true)));
        if(!StringUtils.isEmpty(subjectId)) operations.add(match(Criteria.where("question.subjectId").is(subjectId)));
        if(!StringUtils.isEmpty(knowledgeId)) operations.add(match(Criteria.where("question.knowledges").elemMatch(Criteria.where("knowledgeId").is(knowledgeId))));
        operations.add(unwind("question.knowledges", false));
        if(!StringUtils.isEmpty(knowledgeId)) operations.add(match(Criteria.where("question.knowledges.knowledgeId").is(knowledgeId)));

        operations.add(new CustomProjectAggregationOperation(
                new Document("$project",
                        new Document().append("_id","$question._id").append("content","$question.content").append("options","$question.options").append("answer","$question.answer").append("answerDetail","$question.answerDetail")
                                .append("parse","$question.parse").append("type","$question.type").append("typeInCN","$question.typeInCN").append("difficulty","$question.difficulty").append("subjectId","$question.subjectId")
                                .append("gradeId","$question.gradeId").append("idMD52","$question.idMD52").append("status","$question.status").append("pic","$question.pic")
                                .append("knowledges",new Object[]{new Knowledge("$question.knowledges.knowledgeId","$question.knowledges.knowledge","$question.knowledges.capabilityId","$question.knowledges.capability")})
                )));
        Aggregation agg = Aggregation.newAggregation(operations);
        return mongoTemplate.aggregate(agg, StudentAnswer.class, Question.class).getMappedResults();
    }

    class CustomProjectAggregationOperation implements AggregationOperation {
        private Document document;

        public CustomProjectAggregationOperation (Document document) {
            this.document = document;
        }

        @Override
        public Document toDocument(AggregationOperationContext context) {
            return context.getMappedObject(document);
        }
    }
}
