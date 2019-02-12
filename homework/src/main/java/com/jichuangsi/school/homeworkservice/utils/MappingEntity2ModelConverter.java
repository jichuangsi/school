package com.jichuangsi.school.homeworkservice.utils;

import com.jichuangsi.school.homeworkservice.constant.Result;
import com.jichuangsi.school.homeworkservice.constant.Status;
import com.jichuangsi.school.homeworkservice.entity.Homework;
import com.jichuangsi.school.homeworkservice.entity.Question;
import com.jichuangsi.school.homeworkservice.entity.StudentAnswer;
import com.jichuangsi.school.homeworkservice.entity.TeacherAnswer;
import com.jichuangsi.school.homeworkservice.model.*;

public final class MappingEntity2ModelConverter {

    private MappingEntity2ModelConverter(){}

    public static final HomeworkModelForTeacher ConvertTeacherHomework(Homework homework){
        HomeworkModelForTeacher homeworkModelForTeacher = new HomeworkModelForTeacher();
        homeworkModelForTeacher.setHomeworkId(homework.getId());
        homeworkModelForTeacher.setHomeworkName(homework.getName());
        homeworkModelForTeacher.setHomeworkInfo(homework.getInfo());
        homeworkModelForTeacher.setHomeworkStatus(Status.getStatus(homework.getStatus()));
        homeworkModelForTeacher.setTeacherId(homework.getTeacherId());
        homeworkModelForTeacher.setTeacherName(homework.getTeacherName());
        homeworkModelForTeacher.setClassId(homework.getClassId());
        homeworkModelForTeacher.setClassName(homework.getClassName());
        homeworkModelForTeacher.setHomeworkPublishTime(homework.getPublishTime());
        homeworkModelForTeacher.setHomeworkEndTime(homework.getEndTime());
        homeworkModelForTeacher.setCreateTime(homework.getCreateTime());
        homeworkModelForTeacher.setUpdateTime(homework.getUpdateTime());
        homeworkModelForTeacher.setSubjectName(homework.getSubjectName());
        homeworkModelForTeacher.setSubjectId(homework.getSubjectId());
        if(homework.getAttachments()!=null&&homework.getAttachments().size()>0){
            homework.getAttachments().forEach(attachment -> {
                homeworkModelForTeacher.getAttachments().add(new Attachment(attachment.getName(),attachment.getSub(), attachment.getContentType()));
            });
        }
        return homeworkModelForTeacher;
    }

    public static final HomeworkModelForStudent ConvertStudentHomework(Homework homework){
        HomeworkModelForStudent homeworkModelForStudent = new HomeworkModelForStudent();
        homeworkModelForStudent.setHomeworkId(homework.getId());
        homeworkModelForStudent.setHomeworkName(homework.getName());
        homeworkModelForStudent.setHomeworkInfo(homework.getInfo());
        homeworkModelForStudent.setHomeworkStatus(Status.getStatus(homework.getStatus()));
        homeworkModelForStudent.setTeacherId(homework.getTeacherId());
        homeworkModelForStudent.setTeacherName(homework.getTeacherName());
        homeworkModelForStudent.setClassId(homework.getClassId());
        homeworkModelForStudent.setClassName(homework.getClassName());
        homeworkModelForStudent.setHomeworkPublishTime(homework.getPublishTime());
        homeworkModelForStudent.setHomeworkEndTime(homework.getEndTime());
        homeworkModelForStudent.setCreateTime(homework.getCreateTime());
        homeworkModelForStudent.setUpdateTime(homework.getUpdateTime());
        homeworkModelForStudent.setSubjectName(homework.getSubjectName());
        homeworkModelForStudent.setSubjectId(homework.getSubjectId());
        return homeworkModelForStudent;
    }

    public static final QuestionModelForTeacher ConvertTeacherQuestion(Question question){
        QuestionModelForTeacher questionForTeacher = new QuestionModelForTeacher();
        questionForTeacher.setQuestionId(question.getId());
        questionForTeacher.setQuestionContent(question.getContent());
        if(question.getOptions()!=null && question.getOptions().size()>0){
            question.getOptions().forEach(option -> {
                questionForTeacher.getOptions().add(option);
            });
        }
        questionForTeacher.setAnswer(question.getAnswer());
        questionForTeacher.setAnswerDetail(question.getAnswerDetail());
        questionForTeacher.setParse(question.getParse());
        questionForTeacher.setQuestionType(question.getType());
        questionForTeacher.setQuestionTypeInCN(question.getTypeInCN());
        questionForTeacher.setDifficulty(question.getDifficulty());
        questionForTeacher.setSubjectId(question.getSubjectId());
        questionForTeacher.setGradeId(question.getGradeId());
        question.getKnowledges().forEach(q->{
            questionForTeacher.getKnowledges().add(new KnowledgeModel(q.getKnowledgeId(),q.getKnowledge(),q.getCapabilityId(),q.getCapability()));
        });
        questionForTeacher.setQuestionIdMD52(question.getIdMD52());
        questionForTeacher.setQuestionStatus(Status.getStatus(question.getStatus()));
        questionForTeacher.setQuestionPic(question.getPic());
        questionForTeacher.setCreateTime(question.getCreateTime());
        questionForTeacher.setUpdateTime(question.getUpdateTime());
        return questionForTeacher;
    }

    public static final QuestionModelForStudent ConvertStudentQuestion(Question question){
        QuestionModelForStudent questionForStudent = new QuestionModelForStudent();
        questionForStudent.setQuestionId(question.getId());
        questionForStudent.setQuestionContent(question.getContent());
        if(question.getOptions()!=null && question.getOptions().size()>0){
            question.getOptions().forEach(option -> {
                questionForStudent.getOptions().add(option);
            });
        }
        questionForStudent.setAnswer(question.getAnswer());
        questionForStudent.setAnswerDetail(question.getAnswerDetail());
        questionForStudent.setParse(question.getParse());
        questionForStudent.setQuestionType(question.getType());
        questionForStudent.setQuestionTypeInCN(question.getTypeInCN());
        questionForStudent.setDifficulty(question.getDifficulty());
        questionForStudent.setSubjectId(question.getSubjectId());
        questionForStudent.setGradeId(question.getGradeId());
        question.getKnowledges().forEach(q->{
            questionForStudent.getKnowledges().add(new KnowledgeModel(q.getKnowledgeId(),q.getKnowledge(),q.getCapabilityId(),q.getCapability()));
        });
        questionForStudent.setQuestionIdMD52(question.getIdMD52());
        questionForStudent.setQuestionStatus(Status.getStatus(question.getStatus()));
        questionForStudent.setQuestionPic(question.getPic());
        questionForStudent.setCreateTime(question.getCreateTime());
        questionForStudent.setUpdateTime(question.getUpdateTime());
        return questionForStudent;
    }

    public static final AnswerModelForStudent ConvertStudentAnswer(StudentAnswer studentAnswer){
        AnswerModelForStudent answerForStudent = new AnswerModelForStudent();
        answerForStudent.setAnswerId(studentAnswer.getId());
        answerForStudent.setStudentId(studentAnswer.getStudentId());
        answerForStudent.setStudentName(studentAnswer.getStudentName());
        answerForStudent.setAnswerForObjective(studentAnswer.getObjectiveAnswer());
        //answerForStudent.setPicForSubjective(studentAnswer.getSubjectivePic());
        answerForStudent.setStubForSubjective(studentAnswer.getSubjectivePicStub());
        answerForStudent.setResult(Result.getResult(studentAnswer.getResult()));
        answerForStudent.setSubjectiveScore(null==studentAnswer.getSubjectiveScore()?0:studentAnswer.getSubjectiveScore());
        answerForStudent.setCreateTime(studentAnswer.getCreateTime());
        answerForStudent.setUpdateTime(studentAnswer.getUpdateTime());
        answerForStudent.setReviseTime(studentAnswer.getReviseTime());
        return answerForStudent;
    }

    public static final AnswerModelForTeacher ConvertTeacherAnswer(TeacherAnswer teacherAnswer){
        AnswerModelForTeacher answerForTeacher = new AnswerModelForTeacher();
        answerForTeacher.setAnswerId(teacherAnswer.getId());
        answerForTeacher.setTeacherId(teacherAnswer.getTeacherId());
        answerForTeacher.setTeacherName(teacherAnswer.getTeacherName());
        answerForTeacher.setPicForSubjective(teacherAnswer.getSubjectivePic());
        answerForTeacher.setStubForSubjective(teacherAnswer.getSubjectivePicStub());
        answerForTeacher.setShare(teacherAnswer.isShare());
        answerForTeacher.setCreateTime(teacherAnswer.getCreateTime());
        answerForTeacher.setUpdateTime(teacherAnswer.getUpdateTime());
        answerForTeacher.setShareTime(teacherAnswer.getShareTime());
        return answerForTeacher;
    }
}
