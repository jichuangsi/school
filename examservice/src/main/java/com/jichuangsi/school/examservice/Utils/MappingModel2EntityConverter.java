package com.jichuangsi.school.examservice.Utils;

import com.jichuangsi.microservice.common.model.UserInfoForToken;
import com.jichuangsi.school.examservice.Model.ExamModel;
import com.jichuangsi.school.examservice.Model.QuestionModel;
import com.jichuangsi.school.examservice.entity.*;
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

    public final static PaperDimension converterForExamDimension(String exaid,String dname,String grade,String subject,String type,
                                                             List<String> paperid){
        PaperDimension exam = new PaperDimension();
        exam.setId(StringUtils.isEmpty(exaid)? UUID.randomUUID().toString().replace("-",""):exaid);
        exam.setYear(dname);
        exam.setGrade(grade);
        exam.setSubject(subject);
        exam.setType(type);
        exam.setPaper(paperid);
        exam.setCreateTime(new Date().getTime());
        return exam;
    }
    public final  static ExamDimension converterForDimensionModel(UserInfoForToken userInfo, ExamModel eaxmModel,
                                                                  List<String> qids){
        ExamDimension exam = new ExamDimension();
        if(StringUtils.isEmpty(eaxmModel.getExamId())){
            exam.setExamId( UUID.randomUUID().toString().replace("-",""));
        }else {exam.setExamId(eaxmModel.getExamId());}
        exam.setExamName(eaxmModel.getExamName());
        exam.setCreateTime(eaxmModel.getCreateTime()==0?new Date().getTime():eaxmModel.getCreateTime());
       /* exam.setTeacherId(userInfo.getUserId());
        exam.setTeacherName(userInfo.getUserName());*/
        exam.setUpdateTime(new Date().getTime());
        exam.setExamSecondName(eaxmModel.getExamSecondName());
        exam.setQuestionIds(qids);
        return exam;
    }


    public final  static DimensionQuestion converterForDimensionQuestion(QuestionModel questionModel){
        DimensionQuestion question = new DimensionQuestion();
        question.setAnswer(questionModel.getAnswer());
        question.setAnswerDetail(questionModel.getAnswerDetail());
        question.setContent(questionModel.getQuestionContent());
        question.setCreateTime(questionModel.getCreateTime()==0?new Date().getTime():questionModel.getCreateTime());
        question.setDifficulty(questionModel.getDifficulty());
        question.setId(StringUtils.isEmpty(questionModel.getQuestionId())?
            UUID.randomUUID().toString().replace("-",""):questionModel.getQuestionId());
       question.setIdMD52(questionModel.getQuestionIdMD52());
        //question.setIdMD52(StringUtils.isEmpty(questionModel.getQuestionIdMD52())?Md5Util.encodeByMd5(question.getId()):questionModel.getQuestionIdMD52());
        questionModel.getKnowledges().forEach(q->{
            question.getKnowledges().add(new Knowledge(q.getKnowledgeId(),
                q.getKnowledge(),q.getCapabilityId(),q.getCapability()));
        });
        question.setOptions(questionModel.getOptions());
        question.setParse(questionModel.getParse());
        question.setPic(questionModel.getQuestionPic());
        question.setType(questionModel.getQuesetionType());
        question.setUpdateTime(new Date().getTime());
        return question;
    }




    public final static QuestionModel converterForExam(UserInfoForToken userInfo, DimensionQuestion d){
        QuestionModel question = new QuestionModel();
        question.setAnswer(d.getAnswer());
        question.setAnswerDetail(d.getAnswerDetail());
        question.setQuestionContent(d.getContent());
        question.setCreateTime(d.getCreateTime()==0?new Date().getTime():d.getCreateTime());
        question.setDifficulty(d.getDifficulty());
        question.setGradeId(d.getGradeId());
        question.setQuestionId(UUID.randomUUID().toString().replace("-",""));
        question.setQuestionIdMD52(d.getIdMD52());//new Knowledge(k.getKnowledgeId(),k.getKnowledge(),k.getCapabilityId(),k.getCapability())
        d.getKnowledges().forEach(k->{
            question.getKnowledges().add(new com.jichuangsi.school.examservice.Model.Knowledge
                (k.getKnowledgeId(),k.getKnowledge(),k.getCapabilityId(),k.getCapability()));
        });
        question.setOptions(d.getOptions());
        question.setParse(d.getParse());
        question.setQuestionPic(d.getPic());
        question.setSubjectId(d.getSubjectId());
        question.setGradeId(d.getGradeId());
        question.setQuesetionType(d.getType());
        question.setUpdateTime(new Date().getTime());
        return question;
    }
}
