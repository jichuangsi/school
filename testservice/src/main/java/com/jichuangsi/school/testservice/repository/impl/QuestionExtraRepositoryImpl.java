package com.jichuangsi.school.testservice.repository.impl;

import com.jichuangsi.school.testservice.entity.Test;
import com.jichuangsi.school.testservice.entity.Question;
import com.jichuangsi.school.testservice.repository.QuestionExtraRepository;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.query.Criteria;

import javax.annotation.Resource;
import java.util.List;

import static org.springframework.data.domain.Sort.Direction.ASC;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.*;

public class QuestionExtraRepositoryImpl implements QuestionExtraRepository {

    @Resource
    private MongoTemplate mongoTemplate;

    @Override
    public List<Question> findQuestionsByTestId(String testId) {
        /**
         db.getCollection('school_course').aggregate(
         [
         {$match:{$and:[{"_id":"fa015ba0052241e483d86465373845bc"},{"teacherId":"123"}]}},
         {$unwind:{path:"$questionIds",includeArrayIndex:"questionIndex",preserveNullAndEmptyArrays:false}},
         {
         $lookup:{
         from: "school_question_collection",
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
                match(Criteria.where("_id").is(testId)),
                unwind("questionIds","questionIndex", false),
                lookup("school_question_collection","questionIds","_id","question"),
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
                        .and("question.knowledges").as("knowledges")
                        /*.and("question.knowledge").as("knowledge")
                        .and("question.knowledgeId").as("knowledgeId")
                        .and("question.capability").as("capability")
                        .and("question.capabilityId").as("capabilityId")*/
                        .and("question.idMD52").as("idMD52")
                        .and("question.status").as("status")
                        .and("question.pic").as("pic")
        );
        return mongoTemplate.aggregate(agg, Test.class,Question.class).getMappedResults();
    }
}
