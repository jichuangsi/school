package com.jichuangsi.school.homeworkservice.model;

public class QuestionModelForStudent extends QuestionModel {

    //是否收藏
    private boolean isFavor;

    //学生答案
    private AnswerModelForStudent answerModelForStudent;
    //老师批改
    private AnswerModelForTeacher answerModelForTeacher;

    public AnswerModelForStudent getAnswerModelForStudent() {
        return answerModelForStudent;
    }

    public void setAnswerModelForStudent(AnswerModelForStudent answerModelForStudent) {
        this.answerModelForStudent = answerModelForStudent;
    }

    public AnswerModelForTeacher getAnswerModelForTeacher() {
        return answerModelForTeacher;
    }

    public void setAnswerModelForTeacher(AnswerModelForTeacher answerModelForTeacher) {
        this.answerModelForTeacher = answerModelForTeacher;
    }

    public boolean isFavor() {
        return isFavor;
    }

    public void setFavor(boolean favor) {
        isFavor = favor;
    }
}
