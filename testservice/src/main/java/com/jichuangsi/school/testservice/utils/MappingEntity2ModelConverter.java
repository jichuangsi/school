package com.jichuangsi.school.testservice.utils;

import com.jichuangsi.school.testservice.constant.Result;
import com.jichuangsi.school.testservice.constant.Status;
import com.jichuangsi.school.testservice.entity.Test;
import com.jichuangsi.school.testservice.entity.Question;
import com.jichuangsi.school.testservice.entity.StudentAnswer;
import com.jichuangsi.school.testservice.entity.TeacherAnswer;
import com.jichuangsi.school.testservice.model.*;
import com.jichuangsi.school.testservice.model.TestModelForStudent;

public final class MappingEntity2ModelConverter {

    private MappingEntity2ModelConverter(){}

    public static final TestModelForTeacher ConvertTeacherTest(Test test){
        TestModelForTeacher testModelForTeacher = new TestModelForTeacher();
        testModelForTeacher.setTestId(test.getId());
        testModelForTeacher.setTestName(test.getName());
        testModelForTeacher.setTestInfo(test.getInfo());
        testModelForTeacher.setTestStatus(Status.getStatus(test.getStatus()));
        testModelForTeacher.setTeacherId(test.getTeacherId());
        testModelForTeacher.setTeacherName(test.getTeacherName());
        testModelForTeacher.setClassId(test.getClassId());
        testModelForTeacher.setClassName(test.getClassName());
        testModelForTeacher.setTestPublishTime(test.getPublishTime());
        testModelForTeacher.setTestEndTime(test.getEndTime());
        testModelForTeacher.setCreateTime(test.getCreateTime());
        testModelForTeacher.setUpdateTime(test.getUpdateTime());
        testModelForTeacher.setSubjectName(test.getSubjectName());
        testModelForTeacher.setSubjectId(test.getSubjectId());
        if(test.getAttachments()!=null&&test.getAttachments().size()>0){
            test.getAttachments().forEach(attachment -> {
                testModelForTeacher.getAttachments().add(new Attachment(attachment.getName(),attachment.getSub(), attachment.getContentType()));
            });
        }
        return testModelForTeacher;
    }

    public static final TestModelForStudent ConvertStudentTest(Test test){
        TestModelForStudent testModelForStudent = new TestModelForStudent();
        testModelForStudent.setTestId(test.getId());
        testModelForStudent.setTestName(test.getName());
        testModelForStudent.setTestInfo(test.getInfo());
        testModelForStudent.setTestStatus(Status.getStatus(test.getStatus()));
        testModelForStudent.setTeacherId(test.getTeacherId());
        testModelForStudent.setTeacherName(test.getTeacherName());
        testModelForStudent.setClassId(test.getClassId());
        testModelForStudent.setClassName(test.getClassName());
        testModelForStudent.setTestPublishTime(test.getPublishTime());
        testModelForStudent.setTestEndTime(test.getEndTime());
        testModelForStudent.setCreateTime(test.getCreateTime());
        testModelForStudent.setUpdateTime(test.getUpdateTime());
        testModelForStudent.setSubjectName(test.getSubjectName());
        testModelForStudent.setSubjectId(test.getSubjectId());
        return testModelForStudent;
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
        answerForTeacher.setScore(teacherAnswer.getSubjectiveScore());
        return answerForTeacher;
    }
}
