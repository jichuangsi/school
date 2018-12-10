package com.jichuangsi.school.eaxmservice.Utils;

import com.jichuangsi.school.eaxmservice.Model.EaxmModel;
import com.jichuangsi.school.eaxmservice.Model.QuestionModel;
import com.jichuangsi.school.eaxmservice.entity.Eaxm;
import com.jichuangsi.school.eaxmservice.entity.Question;
import com.jichuangsi.microservice.common.model.UserInfoForToken;

import java.util.Date;
import java.util.List;
import java.util.UUID;

public final  class MappingModel2EntityConverter {
    private MappingModel2EntityConverter(){}

    public final static Eaxm converterForEaxmModel(UserInfoForToken userInfo, EaxmModel eaxmModel,
                                                   List<String> qids){
        Eaxm eaxm = new Eaxm();
        if(eaxmModel.getEaxmId()==null){
            eaxm.setEaxmId( UUID.randomUUID().toString());
        }else {eaxm.setEaxmId(eaxmModel.getEaxmId());}
        eaxm.setEaxmName(eaxmModel.getEaxmName());
        eaxm.setCreateTime(eaxmModel.getCreateTime()==0?new Date().getTime():eaxmModel.getCreateTime());
        eaxm.setTeacherId(userInfo.getUserId());
        eaxm.setTeacherName(userInfo.getUserName());
        eaxm.setUpdateTime(new Date().getTime());
        eaxm.setQuestionIds(qids);
        return eaxm;
    }

    public final  static Question converterForQuestionModel(QuestionModel questionModel){
        Question question = new Question();
        question.setAnswer(questionModel.getAnswer());
        question.setAnswerDetail(questionModel.getAnswerDetail());
        question.setContent(questionModel.getQuestionContent());
        question.setCreateTime(questionModel.getCreateTime()==0?new Date().getTime():questionModel.getCreateTime());
        question.setDifficulty(questionModel.getDifficulty());
        question.setGradeId(questionModel.getGradeId());
        question.setId(questionModel.getQuestionId()==null?
                                    UUID.randomUUID().toString():questionModel.getQuestionId());
        question.setIdMD52(questionModel.getQuestionIdMD52());
        question.setKnowledge(questionModel.getKnowledge());
        question.setOptions(questionModel.getOptions());
        question.setParse(questionModel.getParse());
        question.setPic(questionModel.getQuestionPic());
        question.setStatus(questionModel.getQuestionStatus().getName());
        question.setSubjectId(questionModel.getSubjectId());
        question.setType(questionModel.getQuesetionType());
        question.setUpdateTime(new Date().getTime());
        return question;
    }
}
