package com.jichuangsi.school.courseservice.util;

import com.jichuangsi.microservice.common.model.UserInfoForToken;
import com.jichuangsi.school.courseservice.constant.QuestionType;
import com.jichuangsi.school.courseservice.constant.Status;
import com.jichuangsi.school.courseservice.entity.*;
import com.jichuangsi.school.courseservice.model.AnswerForStudent;
import com.jichuangsi.school.courseservice.model.AnswerForTeacher;
import com.jichuangsi.school.courseservice.model.CourseForTeacher;
import com.jichuangsi.school.courseservice.model.QuestionForTeacher;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.UUID;

public  final class MappingModel2EntityConverter {

    private MappingModel2EntityConverter(){}

    public static final Course ConvertTeacherCourse(UserInfoForToken userInfo, CourseForTeacher courseForTeacher){
        Course course = new Course();
        course.setId(StringUtils.isEmpty(courseForTeacher.getCourseId())? UUID.randomUUID().toString().replaceAll("-", ""):courseForTeacher.getCourseId());
        course.setName(courseForTeacher.getCourseName());
        course.setInfo(courseForTeacher.getCourseInfo());
        course.setStatus(courseForTeacher.getCourseStatus()!=null?courseForTeacher.getCourseStatus().getName():Status.NOTSTART.getName());
        course.setTeacherId(userInfo.getUserId());
        course.setTeacherName(userInfo.getUserName());
        course.setClassId(courseForTeacher.getClassId());
        course.setClassName(courseForTeacher.getClassName());
        course.setStartTime(courseForTeacher.getCourseStartTime());
        course.setEndTime(courseForTeacher.getCourseEndTime());
        course.setSubjectName(courseForTeacher.getSubjectName());
        course.setSubjectId(courseForTeacher.getSubjectId());
        if(courseForTeacher.getQuestions()!=null&&courseForTeacher.getQuestions().size()>0){
            courseForTeacher.getQuestions().forEach(question -> {
                if(!StringUtils.isEmpty(question.getQuestionId()))
                    course.getQuestionIds().add(question.getQuestionId());
            });
        }
        if(courseForTeacher.getAttachments()!=null&&courseForTeacher.getAttachments().size()>0){
            courseForTeacher.getAttachments().forEach(attachment -> {
                course.getAttachments().add(new Attachment(attachment.getName(), attachment.getSub(), attachment.getContentType()));
            });
        }

        if(StringUtils.isEmpty(courseForTeacher.getCourseId()))
            course.setCreateTime(new Date().getTime());
        else
            course.setCreateTime(courseForTeacher.getCreateTime());
        course.setUpdateTime(new Date().getTime());
        course.setPicAddress(courseForTeacher.getCoursePic());
        return course;
    }

    public static final Question ConvertTeacherQuestion(QuestionForTeacher questionForTeacher){
        Question question = new Question();
        question.setId(StringUtils.isEmpty(questionForTeacher.getQuestionId())?UUID.randomUUID().toString().replaceAll("-", ""):questionForTeacher.getQuestionId());
        question.setContent(questionForTeacher.getQuestionContent());
        if(questionForTeacher.getOptions()!=null && questionForTeacher.getOptions().size()>0){
            questionForTeacher.getOptions().forEach(option -> {
               question.getOptions().add(option);
            });
        }
        question.setAnswer(questionForTeacher.getAnswer());
        question.setAnswerDetail(questionForTeacher.getAnswerDetail());
        question.setParse(questionForTeacher.getParse());
        //question.setType(questionForTeacher.getQuesetionType()!=null?translateQuestionType(questionForTeacher.getQuesetionType()).getName(): null);
        question.setType(questionForTeacher.getQuesetionType());
        question.setTypeInCN(StringUtils.isEmpty(questionForTeacher.getQuestionTypeInCN())?questionForTeacher.getQuesetionType():questionForTeacher.getQuestionTypeInCN());
        question.setDifficulty(questionForTeacher.getDifficulty());
        question.setSubjectId(questionForTeacher.getSubjectId());
        question.setGradeId(questionForTeacher.getGradeId());
        questionForTeacher.getKnowledges().forEach(q->{
            question.getKnowledges().add(new Knowledge(q.getKnowledgeId(),q.getKnowledge(),q.getCapabilityId(),q.getCapability()));
        });
        question.setIdMD52(questionForTeacher.getQuestionIdMD52());
        question.setStatus(questionForTeacher.getQuestionStatus()!=null?questionForTeacher.getQuestionStatus().getName(): Status.NOTSTART.getName());
        question.setPic(questionForTeacher.getQuestionPic());
        if(StringUtils.isEmpty(questionForTeacher.getQuestionId()))
            question.setCreateTime(new Date().getTime());
        else
            question.setCreateTime(questionForTeacher.getCreateTime());
        question.setUpdateTime(new Date().getTime());
        return question;
    }

    public static final TeacherAnswer ConvertTeacherAnswer(UserInfoForToken userInfo, String questionId, String studentAnswerId, AnswerForTeacher answerForTeacher){
        TeacherAnswer teacherAnswer = new TeacherAnswer();
        teacherAnswer.setId(StringUtils.isEmpty(answerForTeacher.getAnswerId())?UUID.randomUUID().toString().replaceAll("-", ""):answerForTeacher.getAnswerId());
        teacherAnswer.setTeacherId(userInfo.getUserId());
        teacherAnswer.setTeacherName(userInfo.getUserName());
        teacherAnswer.setSubjectivePic(answerForTeacher.getPicForSubjective());
        teacherAnswer.setSubjectivePicStub(answerForTeacher.getStubForSubjective());
        teacherAnswer.setSubjectiveScore(answerForTeacher.getScore());
        teacherAnswer.setQuestionId(questionId);
        teacherAnswer.setStudentAnswerId(studentAnswerId);
        teacherAnswer.setShare(answerForTeacher.isShare());
        if(StringUtils.isEmpty(answerForTeacher.getAnswerId()))
            teacherAnswer.setCreateTime(new Date().getTime());
        else
            teacherAnswer.setCreateTime(answerForTeacher.getCreateTime());
        teacherAnswer.setUpdateTime(new Date().getTime());
        teacherAnswer.setShareTime(answerForTeacher.getShareTime());
        return teacherAnswer;
    }

    public static final StudentAnswer ConvertStudentAnswer(UserInfoForToken userInfo, String questionId, AnswerForStudent answerForStudent){
        StudentAnswer studentAnswer = new StudentAnswer();
        studentAnswer.setId(StringUtils.isEmpty(answerForStudent.getAnswerId())?UUID.randomUUID().toString().replaceAll("-", ""):answerForStudent.getAnswerId());
        studentAnswer.setStudentId(userInfo.getUserId());
        studentAnswer.setStudentName(userInfo.getUserName());
        studentAnswer.setObjectiveAnswer(answerForStudent.getAnswerForObjective());
        studentAnswer.setResult(StringUtils.isEmpty(answerForStudent.getResult())?null:answerForStudent.getResult().getName());
        //studentAnswer.setSubjectivePic(answerForStudent.getPicForSubjective());
        studentAnswer.setSubjectivePicStub(answerForStudent.getStubForSubjective());
        //studentAnswer.setSubjectiveScore(answerForStudent.getSubjectiveScore());
        studentAnswer.setQuestionId(questionId);
        if(StringUtils.isEmpty(answerForStudent.getAnswerId()))
            studentAnswer.setCreateTime(new Date().getTime());
        else
            studentAnswer.setCreateTime(answerForStudent.getCreateTime());
        studentAnswer.setUpdateTime(new Date().getTime());
        return studentAnswer;
    }

    /*private static QuestionType translateQuestionType(String quTypeInChinese) {
        switch (quTypeInChinese){
            case "选择题" : case "单选题" : case "多选题" : case "判断题" : case "双选题" :
            case "不定项选择题" : case "多项选择题" : case "单项选择题" :
                return QuestionType.OBJECTIVE;
            default: return QuestionType.SUBJECTIVE;
        }
    }*/
}
