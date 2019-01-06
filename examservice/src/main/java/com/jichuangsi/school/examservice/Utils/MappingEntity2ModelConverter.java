package com.jichuangsi.school.examservice.Utils;

import com.jichuangsi.school.examservice.Model.ExamModel;
import com.jichuangsi.school.examservice.Model.QuestionModel;
import com.jichuangsi.school.examservice.constant.Status;
import com.jichuangsi.school.examservice.entity.Exam;
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
        questionModel.setKnowledge(question.getKnowledge());
        questionModel.setKnowledgeId(question.getKnowledgeId());
        questionModel.setCapability(question.getCapability());
        questionModel.setCapabilityId(question.getCapabilityId());
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
}
