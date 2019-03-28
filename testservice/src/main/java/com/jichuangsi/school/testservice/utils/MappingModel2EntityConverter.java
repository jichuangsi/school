package com.jichuangsi.school.testservice.utils;

import com.jichuangsi.microservice.common.model.UserInfoForToken;
import com.jichuangsi.school.testservice.constant.Status;
import com.jichuangsi.school.testservice.entity.*;
import com.jichuangsi.school.testservice.model.AnswerModelForStudent;
import com.jichuangsi.school.testservice.model.AnswerModelForTeacher;
import com.jichuangsi.school.testservice.model.TestModelForTeacher;
import com.jichuangsi.school.testservice.model.QuestionModelForTeacher;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.UUID;

public  final class MappingModel2EntityConverter {

    private MappingModel2EntityConverter(){}

    public static final Test ConvertTeacherTest(UserInfoForToken userInfo, TestModelForTeacher testModelForTeacher){
        Test test = new Test();
        test.setId(StringUtils.isEmpty(testModelForTeacher.getTestId())? UUID.randomUUID().toString().replaceAll("-", ""):testModelForTeacher.getTestId());
        test.setName(testModelForTeacher.getTestName());
        test.setInfo(testModelForTeacher.getTestInfo());
        test.setStatus(testModelForTeacher.getTestStatus()!=null?testModelForTeacher.getTestStatus().getName(): Status.NOTSTART.getName());
        test.setTeacherId(userInfo.getUserId());
        test.setTeacherName(userInfo.getUserName());
        test.setClassId(testModelForTeacher.getClassId());
        test.setClassName(testModelForTeacher.getClassName());
        test.setPublishTime(testModelForTeacher.getTestPublishTime());
        test.setEndTime(testModelForTeacher.getTestEndTime());
        test.setSubjectName(testModelForTeacher.getSubjectName());
        test.setSubjectId(testModelForTeacher.getSubjectId());
        if(testModelForTeacher.getQuestions()!=null&&testModelForTeacher.getQuestions().size()>0){
            testModelForTeacher.getQuestions().forEach(question -> {
                if(!StringUtils.isEmpty(question.getQuestionId()))
                    test.getQuestionIds().add(question.getQuestionId());
            });
        }
        if(testModelForTeacher.getAttachments()!=null&&testModelForTeacher.getAttachments().size()>0){
            testModelForTeacher.getAttachments().forEach(attachment -> {
                test.getAttachments().add(new Attachment(attachment.getName(), attachment.getSub(), attachment.getContentType()));
            });
        }

        if(StringUtils.isEmpty(testModelForTeacher.getTestId()))
            test.setCreateTime(new Date().getTime());
        else
            test.setCreateTime(test.getCreateTime());
        test.setUpdateTime(new Date().getTime());
        return test;
    }

    public static final Question ConvertTeacherQuestion(QuestionModelForTeacher questionModelForTeacher){
        Question question = new Question();
        question.setId(StringUtils.isEmpty(questionModelForTeacher.getQuestionId())?UUID.randomUUID().toString().replaceAll("-", ""):questionModelForTeacher.getQuestionId());
        question.setContent(questionModelForTeacher.getQuestionContent());
        if(questionModelForTeacher.getOptions()!=null && questionModelForTeacher.getOptions().size()>0){
            questionModelForTeacher.getOptions().forEach(option -> {
               question.getOptions().add(option);
            });
        }
        question.setAnswer(questionModelForTeacher.getAnswer());
        question.setAnswerDetail(questionModelForTeacher.getAnswerDetail());
        question.setParse(questionModelForTeacher.getParse());
        //question.setType(questionForTeacher.getQuesetionType()!=null?translateQuestionType(questionForTeacher.getQuesetionType()).getName(): null);
        question.setType(questionModelForTeacher.getQuestionType());
        question.setTypeInCN(StringUtils.isEmpty(questionModelForTeacher.getQuestionTypeInCN())?questionModelForTeacher.getQuestionType():questionModelForTeacher.getQuestionTypeInCN());
        question.setDifficulty(questionModelForTeacher.getDifficulty());
        question.setSubjectId(questionModelForTeacher.getSubjectId());
        question.setGradeId(questionModelForTeacher.getGradeId());
        questionModelForTeacher.getKnowledges().forEach(q->{
            question.getKnowledges().add(new Knowledge(q.getKnowledgeId(),q.getKnowledge(),q.getCapabilityId(),q.getCapability()));
        });
        question.setIdMD52(questionModelForTeacher.getQuestionIdMD52());
        question.setStatus(questionModelForTeacher.getQuestionStatus()!=null?questionModelForTeacher.getQuestionStatus().getName(): Status.NOTSTART.getName());
        question.setPic(questionModelForTeacher.getQuestionPic());
        if(StringUtils.isEmpty(questionModelForTeacher.getQuestionId()))
            question.setCreateTime(new Date().getTime());
        else
            question.setCreateTime(questionModelForTeacher.getCreateTime());
        question.setUpdateTime(new Date().getTime());
        return question;
    }

    public static final TeacherAnswer ConvertTeacherAnswer(UserInfoForToken userInfo, String questionId, String studentAnswerId, AnswerModelForTeacher answerModelForTeacher){
        TeacherAnswer teacherAnswer = new TeacherAnswer();
        teacherAnswer.setId(StringUtils.isEmpty(answerModelForTeacher.getAnswerId())?UUID.randomUUID().toString().replaceAll("-", ""):answerModelForTeacher.getAnswerId());
        teacherAnswer.setTeacherId(userInfo.getUserId());
        teacherAnswer.setTeacherName(userInfo.getUserName());
        teacherAnswer.setSubjectivePic(answerModelForTeacher.getPicForSubjective());
        teacherAnswer.setSubjectivePicStub(answerModelForTeacher.getStubForSubjective());
        teacherAnswer.setSubjectiveScore(answerModelForTeacher.getScore());
        teacherAnswer.setQuestionId(questionId);
        teacherAnswer.setStudentAnswerId(studentAnswerId);
        teacherAnswer.setShare(answerModelForTeacher.isShare());
        if(StringUtils.isEmpty(answerModelForTeacher.getAnswerId()))
            teacherAnswer.setCreateTime(new Date().getTime());
        else
            teacherAnswer.setCreateTime(answerModelForTeacher.getCreateTime());
        teacherAnswer.setUpdateTime(new Date().getTime());
        teacherAnswer.setShareTime(answerModelForTeacher.getShareTime());
        return teacherAnswer;
    }

    public static final StudentAnswer ConvertStudentAnswer(UserInfoForToken userInfo, String questionId, AnswerModelForStudent answerModelForStudent){
        StudentAnswer studentAnswer = new StudentAnswer();
        studentAnswer.setId(StringUtils.isEmpty(answerModelForStudent.getAnswerId())?UUID.randomUUID().toString().replaceAll("-", ""):answerModelForStudent.getAnswerId());
        studentAnswer.setStudentId(userInfo.getUserId());
        studentAnswer.setStudentName(userInfo.getUserName());
        studentAnswer.setObjectiveAnswer(answerModelForStudent.getAnswerForObjective());
        studentAnswer.setResult(StringUtils.isEmpty(answerModelForStudent.getResult())?null:answerModelForStudent.getResult().getName());
        //studentAnswer.setSubjectivePic(answerForStudent.getPicForSubjective());
        studentAnswer.setSubjectivePicStub(answerModelForStudent.getStubForSubjective());
        //studentAnswer.setSubjectiveScore(answerForStudent.getSubjectiveScore());
        studentAnswer.setQuestionId(questionId);
        if(StringUtils.isEmpty(answerModelForStudent.getAnswerId()))
            studentAnswer.setCreateTime(new Date().getTime());
        else
            studentAnswer.setCreateTime(answerModelForStudent.getCreateTime());
        studentAnswer.setUpdateTime(new Date().getTime());
        return studentAnswer;
    }

}
