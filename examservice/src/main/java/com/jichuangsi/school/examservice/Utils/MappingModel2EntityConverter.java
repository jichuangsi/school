package com.jichuangsi.school.examservice.Utils;

import com.jichuangsi.microservice.common.model.UserInfoForToken;
import com.jichuangsi.school.examservice.Model.ExamModel;
import com.jichuangsi.school.examservice.Model.QuestionModel;
import com.jichuangsi.school.examservice.entity.Exam;
import com.jichuangsi.school.examservice.entity.Knowledge;
import com.jichuangsi.school.examservice.entity.Question;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.List;
import java.util.UUID;

public final  class MappingModel2EntityConverter {
    private MappingModel2EntityConverter(){}

    public final static Exam converterForExamModel(UserInfoForToken userInfo, ExamModel eaxmModel,
                                                   List<String> qids){
        Exam exam = new Exam();
        if(StringUtils.isEmpty(eaxmModel.getExamId())){
            exam.setExamId( UUID.randomUUID().toString().replace("-",""));
        }else {exam.setExamId(eaxmModel.getExamId());}
        exam.setExamName(eaxmModel.getExamName());
        exam.setCreateTime(eaxmModel.getCreateTime()==0?new Date().getTime():eaxmModel.getCreateTime());
        exam.setTeacherId(userInfo.getUserId());
        exam.setTeacherName(userInfo.getUserName());
        exam.setUpdateTime(new Date().getTime());
        exam.setExamSecondName(eaxmModel.getExamSecondName());
        exam.setQuestionIds(qids);
        return exam;
    }

    public final  static Question converterForQuestionModel(QuestionModel questionModel){
        Question question = new Question();
        question.setAnswer(questionModel.getAnswer());
        question.setAnswerDetail(questionModel.getAnswerDetail());
        question.setContent(questionModel.getQuestionContent());
        question.setCreateTime(questionModel.getCreateTime()==0?new Date().getTime():questionModel.getCreateTime());
        question.setDifficulty(questionModel.getDifficulty());
        question.setGradeId(questionModel.getGradeId());
        question.setId(StringUtils.isEmpty(questionModel.getQuestionId())?
                                    UUID.randomUUID().toString().replace("-",""):questionModel.getQuestionId());
        question.setIdMD52(questionModel.getQuestionIdMD52());
        questionModel.getKnowledges().forEach(q->{
            question.getKnowledges().add(new Knowledge(q.getKnowledgeId(),
                    q.getKnowledge(),q.getCapabilityId(),q.getCapability()));
        });
        question.setOptions(questionModel.getOptions());
        question.setParse(questionModel.getParse());
        question.setPic(questionModel.getQuestionPic());
        //question.setStatus(questionModel.getQuestionStatus().getName());
        question.setSubjectId(questionModel.getSubjectId());
        question.setGradeId(questionModel.getGradeId());
        question.setType(questionModel.getQuesetionType());
        question.setUpdateTime(new Date().getTime());
        return question;
    }
}
