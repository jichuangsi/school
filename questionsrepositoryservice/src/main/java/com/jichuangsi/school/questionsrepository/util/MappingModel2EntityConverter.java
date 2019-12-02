package com.jichuangsi.school.questionsrepository.util;

import com.jichuangsi.microservice.common.model.UserInfoForToken;
import com.jichuangsi.school.questionsrepository.entity.*;
import com.jichuangsi.school.questionsrepository.model.favor.FavorQuestion;
import com.jichuangsi.school.questionsrepository.model.school.SchoolQuestion;
import com.jichuangsi.school.questionsrepository.model.self.SelfQuestion;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.UUID;

public final class MappingModel2EntityConverter {
    private MappingModel2EntityConverter(){}

    public final static FavorQuestions ConverterFavorQuestion(UserInfoForToken userInfoForToken, FavorQuestion fq){
        FavorQuestions favorQuestions = new FavorQuestions();
        favorQuestions.setId(StringUtils.isEmpty(fq.getQuestionId())? UUID.randomUUID().toString().replaceAll("-", ""):fq.getQuestionId());
        favorQuestions.setAnswer(fq.getAnswer());
        favorQuestions.setAnswerDetail(fq.getAnswerDetail());
        favorQuestions.setContent(fq.getQuestionContent());
        favorQuestions.setDifficulty(fq.getDifficulty());
        favorQuestions.setGradeId(fq.getGradeId());
        fq.getKnowledges().forEach(q->{
            favorQuestions.getKnowledges().add(new Knowledge(q.getKnowledgeId(),
                    q.getKnowledge(),q.getCapabilityId(),q.getCapability()));
        });
        favorQuestions.setOptions(fq.getOptions());
        favorQuestions.setParse(fq.getParse());
        favorQuestions.setQuestionIdMD52(StringUtils.isEmpty(fq.getQuestionIdMD52())?Md5Util.encodeByMd5(favorQuestions.getId()):fq.getQuestionIdMD52());
        favorQuestions.setQuestionPic(fq.getQuestionPic());
        favorQuestions.setSubjectId(fq.getSubjectId());
        favorQuestions.setTeacherId(userInfoForToken.getUserId());
        favorQuestions.setTeacherName(userInfoForToken.getUserName());
        favorQuestions.setType(fq.getQuesetionType());
        if(StringUtils.isEmpty(fq.getQuestionId()))
            favorQuestions.setCreateTime(new Date().getTime());
        else
            favorQuestions.setCreateTime(fq.getCreateTime());
        favorQuestions.setUpdateTime(new Date().getTime());
        return favorQuestions;
    }

    public final static SelfQuestions ConverterSelfQuestion(UserInfoForToken userInfoForToken, SelfQuestion sq){
        SelfQuestions selfQuestions = new SelfQuestions();
        selfQuestions.setId(StringUtils.isEmpty(sq.getQuestionId())? UUID.randomUUID().toString().replaceAll("-", ""):sq.getQuestionId());
        selfQuestions.setAnswer(sq.getAnswer());
        selfQuestions.setAnswerDetail(sq.getAnswerDetail());
        selfQuestions.setContent(sq.getQuestionContent());
        selfQuestions.setDifficulty(sq.getDifficulty());
        selfQuestions.setGradeId(sq.getGradeId());
        sq.getKnowledges().forEach(q->{
            selfQuestions.getKnowledges().add(new Knowledge(q.getKnowledgeId(),
                    q.getKnowledge(),q.getCapabilityId(),q.getCapability()));
        });
        selfQuestions.setOptions(sq.getOptions());
        selfQuestions.setParse(sq.getParse());
        selfQuestions.setQuestionIdMD52(StringUtils.isEmpty(sq.getQuestionIdMD52())?Md5Util.encodeByMd5(selfQuestions.getId()):sq.getQuestionIdMD52());
        selfQuestions.setQuestionPic(sq.getQuestionPic());
        selfQuestions.setSubjectId(sq.getSubjectId());
        selfQuestions.setTeacherId(userInfoForToken.getUserId());
        selfQuestions.setTeacherName(userInfoForToken.getUserName());
        selfQuestions.setType(sq.getQuesetionType());
        if(StringUtils.isEmpty(sq.getQuestionId()))
            selfQuestions.setCreateTime(new Date().getTime());
        else
            selfQuestions.setCreateTime(sq.getCreateTime());
        selfQuestions.setUpdateTime(new Date().getTime());
        return selfQuestions;
    }

    public final static SchoolQuestions ConverterSchoolQuestion(UserInfoForToken userInfoForToken, SchoolQuestion sq){
        SchoolQuestions sqs = new SchoolQuestions();
        sqs.setId(StringUtils.isEmpty(sq.getQuestionId())? UUID.randomUUID().toString().replaceAll("-", ""):sq.getQuestionId());
        sqs.setAnswer(sq.getAnswer());
        sqs.setAnswerDetail(sq.getAnswerDetail());
        sqs.setContent(sq.getQuestionContent());
        sqs.setDifficulty(sq.getDifficulty());
        sqs.setGradeId(sq.getGradeId());
        sq.getKnowledges().forEach(q->{
            sqs.getKnowledges().add(new Knowledge(q.getKnowledgeId(),
                    q.getKnowledge(),q.getCapabilityId(),q.getCapability()));
        });
        sqs.setOptions(sq.getOptions());
        sqs.setParse(sq.getParse());
        sqs.setQuestionIdMD52(StringUtils.isEmpty(sq.getQuestionIdMD52())?Md5Util.encodeByMd5(sqs.getId()):sq.getQuestionIdMD52());
        sqs.setQuestionPic(sq.getQuestionPic());
        sqs.setSubjectId(sq.getSubjectId());
        sqs.setTeacherId(userInfoForToken.getUserId());
        sqs.setTeacherName(userInfoForToken.getUserName());
        sqs.setType(sq.getQuesetionType());
        sqs.setSchoolId(sq.getSchoolId());
        sqs.setSchoolName(sq.getSchoolName());
        if(StringUtils.isEmpty(sq.getQuestionId()))
            sqs.setCreateTime(new Date().getTime());
        else
            sqs.setCreateTime(sq.getCreateTime());
        sqs.setUpdateTime(new Date().getTime());
        return sqs;
    }



    public final static Dimension ConverterDimension(UserInfoForToken userInfoForToken, SelfQuestion sq){
        Dimension dimension = new Dimension();
        dimension.setId(StringUtils.isEmpty(sq.getQuestionId())? UUID.randomUUID().toString().replaceAll("-", ""):sq.getQuestionId());
        dimension.setAnswer(sq.getAnswer());
        dimension.setAnswerDetail(sq.getAnswerDetail());
        dimension.setContent(sq.getQuestionContent());
        dimension.setDifficulty(sq.getDifficulty());
        sq.getKnowledges().forEach(q->{
            dimension.getKnowledges().add(new Knowledge(q.getKnowledgeId(),
                q.getKnowledge(),q.getCapabilityId(),q.getCapability()));
        });
        dimension.setOptions(sq.getOptions());
        dimension.setParse(sq.getParse());
        dimension.setQuestionIdMD52(StringUtils.isEmpty(sq.getQuestionIdMD52())?Md5Util.encodeByMd5(dimension.getId()):sq.getQuestionIdMD52());

        dimension.setType(sq.getQuesetionType());
        if(StringUtils.isEmpty(sq.getQuestionId()))
            dimension.setCreateTime(new Date().getTime());
        else
            dimension.setCreateTime(sq.getCreateTime());
        dimension.setUpdateTime(new Date().getTime());
        return dimension;
    }
}
