package com.jichuangsi.school.courseservice.model;

public class QuestionForStudent extends Question {

    //是否收藏
    private boolean isFavor;

    //学生答案
    private AnswerForStudent answerForStudent;
    //老师批改
    private AnswerForTeacher answerForTeacher;

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

    public boolean isFavor() {
        return isFavor;
    }

    public void setFavor(boolean favor) {
        isFavor = favor;
    }
}
