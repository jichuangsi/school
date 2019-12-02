package com.jichuangsi.school.examservice.Utils;

import com.jichuangsi.school.examservice.Model.ExamModel;
import com.jichuangsi.school.examservice.Model.Knowledge;
import com.jichuangsi.school.examservice.Model.QuestionModel;
import com.jichuangsi.school.examservice.entity.DimensionQuestion;
import com.jichuangsi.school.examservice.entity.Exam;
import com.jichuangsi.school.examservice.entity.ExamDimension;
import com.jichuangsi.school.examservice.entity.Question;

public final class MappingEntity2ModelConverter {
    private MappingEntity2ModelConverter(){}

    public final static QuestionModel ConverterForQuestion(Question question){
        QuestionModel questionModel = new QuestionModel();
        questionModel.setAnswer(question.getAnswer());
        questionModel.setAnswerDetail(question.getAnswerDetail());
        questionModel.setCreateTime(question.getCreateTime());
        questionModel.setDifficulty(question.getDifficulty());
        questionModel.setGradeId(question.getGradeId());
        question.getKnowledges().forEach(q->{
            questionModel.getKnowledges().add(new Knowledge(q.getKnowledgeId(),
                    q.getKnowledge(),q.getCapabilityId(),q.getCapability()));
        });
        questionModel.setOptions(question.getOptions());
        questionModel.setParse(question.getParse());
        questionModel.setQuesetionType(question.getType());
        questionModel.setQuestionContent(question.getContent());
        questionModel.setQuestionId(question.getId());
        questionModel.setQuestionIdMD52(question.getIdMD52());
        questionModel.setQuestionPic(question.getPic());
        //questionModel.setQuestionStatus(Status.getStatus(question.getStatus()));
        questionModel.setSubjectId(question.getSubjectId());
        questionModel.setGradeId(question.getGradeId());
        questionModel.setUpdateTime(question.getUpdateTime());

        return questionModel;
    }

    public final static ExamModel ConverterForExam(Exam exam){

        ExamModel examModel = new ExamModel();
        examModel.setExamSecondName(exam.getExamSecondName());
        examModel.setUpdateTime(exam.getUpdateTime());
        examModel.setExamName(exam.getExamName());
        examModel.setExamId(exam.getExamId());
        examModel.setCreateTime(exam.getCreateTime());

        return examModel;
    }
    public final static ExamModel ConverterForExam(ExamDimension exam){

        ExamModel examModel = new ExamModel();
        examModel.setExamSecondName(exam.getExamSecondName());
        examModel.setUpdateTime(exam.getUpdateTime());
        examModel.setExamName(exam.getExamName());
        examModel.setExamId(exam.getExamId());
        examModel.setCreateTime(exam.getCreateTime());

        return examModel;
    }


    public final static QuestionModel ConverterForDimensionQuestion(DimensionQuestion question){
        QuestionModel questionModel = new QuestionModel();
        questionModel.setAnswer(question.getAnswer());
        questionModel.setAnswerDetail(question.getAnswerDetail());
        questionModel.setCreateTime(question.getCreateTime());
        questionModel.setDifficulty(question.getDifficulty());
        questionModel.setGradeId(question.getGradeId());
        question.getKnowledges().forEach(q->{
            questionModel.getKnowledges().add(new Knowledge(q.getKnowledgeId(),
                q.getKnowledge(),q.getCapabilityId(),q.getCapability()));
        });
        questionModel.setOptions(question.getOptions());
        questionModel.setParse(question.getParse());
        questionModel.setQuesetionType(question.getType());
        questionModel.setQuestionContent(question.getContent());
        questionModel.setQuestionId(question.getId());
        questionModel.setQuestionIdMD52(question.getIdMD52());
        questionModel.setQuestionPic(question.getPic());
        //questionModel.setQuestionStatus(Status.getStatus(question.getStatus()));
        questionModel.setSubjectId(question.getSubjectId());
        questionModel.setGradeId(question.getGradeId());
        questionModel.setUpdateTime(question.getUpdateTime());

        return questionModel;
    }
}
