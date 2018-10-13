package com.jichuangsi.school.courseservice.model;

public class AnswerForTeacher {
    String answerId;
    String teacherId;
    String teacherName;
    String picForSubjective;
    String stubForSubjective;

    double score;

    public String getAnswerId() {
        return answerId;
    }

    public void setAnswerId(String answerId) {
        this.answerId = answerId;
    }

    public String getPicForSubjective() {
        return picForSubjective;
    }

    public void setPicForSubjective(String picForSubjective) {
        this.picForSubjective = picForSubjective;
    }

    public String getStubForSubjective() {
        return stubForSubjective;
    }

    public void setStubForSubjective(String stubForSubjective) {
        this.stubForSubjective = stubForSubjective;
    }

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }

    public String getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(String teacherId) {
        this.teacherId = teacherId;
    }

    public String getTeacherName() {
        return teacherName;
    }

    public void setTeacherName(String teacherName) {
        this.teacherName = teacherName;
    }
}
