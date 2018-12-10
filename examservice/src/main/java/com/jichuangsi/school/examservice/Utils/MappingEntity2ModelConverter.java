package com.jichuangsi.school.examservice.Utils;

import com.jichuangsi.school.examservice.Model.QuestionModel;
import com.jichuangsi.school.examservice.constant.Status;
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
        questionModel.setOptions(question.getOptions());
        questionModel.setParse(question.getParse());
        questionModel.setQuesetionType(question.getType());
        questionModel.setQuestionContent(question.getContent());
        questionModel.setQuestionId(question.getId());
        questionModel.setQuestionIdMD52(question.getIdMD52());
        questionModel.setQuestionPic(question.getPic());
        questionModel.setQuestionStatus(Status.getStatus(question.getStatus()));
        questionModel.setSubjectId(question.getSubjectId());
        questionModel.setUpdateTime(question.getUpdateTime());

        return questionModel;
    }
}
