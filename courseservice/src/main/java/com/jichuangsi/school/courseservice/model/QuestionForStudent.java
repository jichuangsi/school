package com.jichuangsi.school.courseservice.model;

public class QuestionForStudent extends Question {

    //学生答案
    AnswerForStudent answerForStudent;
    //老师批改
    AnswerForTeacher answerForTeacher;

    public AnswerForStudent getAnswerForStudent() {
        return answerForStudent;
    }

    public void setAnswerForStudent(AnswerForStudent answerForStudent) {
        this.answerForStudent = answerForStudent;
    }

    public AnswerForTeacher getAnswerForTeacher() {
        return answerForTeacher;
    }

    public void setAnswerForTeacher(AnswerForTeacher answerForTeacher) {
        this.answerForTeacher = answerForTeacher;
    }
}
