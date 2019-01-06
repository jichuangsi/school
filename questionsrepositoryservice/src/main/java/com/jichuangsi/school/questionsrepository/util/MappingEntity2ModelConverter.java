package com.jichuangsi.school.questionsrepository.util;

import com.jichuangsi.school.questionsrepository.entity.FavorQuestions;
import com.jichuangsi.school.questionsrepository.entity.SchoolQuestions;
import com.jichuangsi.school.questionsrepository.entity.SelfQuestions;
import com.jichuangsi.school.questionsrepository.model.favor.FavorQuestion;
import com.jichuangsi.school.questionsrepository.model.school.SchoolQuestion;
import com.jichuangsi.school.questionsrepository.model.self.SelfQuestion;

public final class MappingEntity2ModelConverter {
    private MappingEntity2ModelConverter(){}

    public final static FavorQuestion ConverterFavorQuestions(FavorQuestions fqs){
        FavorQuestion fq = new FavorQuestion();
        fq.setAnswer(fqs.getAnswer());
        fq.setAnswerDetail(fqs.getAnswerDetail());
        fq.setDifficulty(fqs.getDifficulty());
        fq.setGradeId(fqs.getGradeId());
        fq.setKnowledge(fqs.getKnowledge());
        fq.setKnowledgeId(fqs.getKnowledgeId());
        fq.setCapability(fqs.getCapability());
        fq.setCapabilityId(fqs.getCapabilityId());
        fq.setOptions(fqs.getOptions());
        fq.setParse(fqs.getParse());
        fq.setQuesetionType(fqs.getType());
        fq.setQuestionContent(fqs.getContent());
        fq.setQuestionId(fqs.getId());
        fq.setQuestionIdMD52(fqs.getQuestionIdMD52());
        fq.setQuestionPic(fqs.getQuestionPic());
        fq.setSubjectId(fqs.getSubjectId());
        fq.setTeacherId(fqs.getTeacherId());
        fq.setTeacherName(fqs.getTeacherName());
        fq.setCreateTime(fqs.getCreateTime());
        fq.setUpdateTime(fqs.getUpdateTime());

        return fq;
    }


    public final static SelfQuestion ConverterSelfQuestions(SelfQuestions sqs){
        SelfQuestion sq = new SelfQuestion();
        sq.setAnswer(sqs.getAnswer());
        sq.setAnswerDetail(sqs.getAnswerDetail());
        sq.setDifficulty(sqs.getDifficulty());
        sq.setGradeId(sqs.getGradeId());
        sq.setKnowledge(sqs.getKnowledge());
        sq.setKnowledgeId(sqs.getKnowledgeId());
        sq.setCapability(sqs.getCapability());
        sq.setCapabilityId(sqs.getCapabilityId());
        sq.setOptions(sqs.getOptions());
        sq.setParse(sqs.getParse());
        sq.setQuesetionType(sqs.getType());
        sq.setQuestionContent(sqs.getContent());
        sq.setQuestionId(sqs.getId());
        sq.setQuestionIdMD52(sqs.getQuestionIdMD52());
        sq.setQuestionPic(sqs.getQuestionPic());
        sq.setSubjectId(sqs.getSubjectId());
        sq.setTeacherId(sqs.getTeacherId());
        sq.setTeacherName(sqs.getTeacherName());
        sq.setCreateTime(sqs.getCreateTime());
        sq.setUpdateTime(sqs.getUpdateTime());

        return sq;
    }


    public final static SchoolQuestion ConverterSchoolQuestions(SchoolQuestions sqs){
        SchoolQuestion sq = new SchoolQuestion();
        sq.setAnswer(sqs.getAnswer());
        sq.setAnswerDetail(sqs.getAnswerDetail());
        sq.setDifficulty(sqs.getDifficulty());
        sq.setGradeId(sqs.getGradeId());
        sq.setKnowledge(sqs.getKnowledge());
        sq.setKnowledgeId(sqs.getKnowledgeId());
        sq.setCapability(sqs.getCapability());
        sq.setCapabilityId(sqs.getCapabilityId());
        sq.setOptions(sqs.getOptions());
        sq.setParse(sqs.getParse());
        sq.setQuesetionType(sqs.getType());
        sq.setQuestionContent(sqs.getContent());
        sq.setQuestionId(sqs.getId());
        sq.setQuestionIdMD52(sqs.getQuestionIdMD52());
        sq.setQuestionPic(sqs.getQuestionPic());
        sq.setSubjectId(sqs.getSubjectId());
        sq.setTeacherId(sqs.getTeacherId());
        sq.setTeacherName(sqs.getTeacherName());
        sq.setSchoolId(sqs.getSchoolId());
        sq.setSchoolName(sqs.getSchoolName());
        sq.setCreateTime(sqs.getCreateTime());
        sq.setUpdateTime(sqs.getUpdateTime());

        return sq;
    }
}
