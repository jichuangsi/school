package com.jichuangsi.school.courseservice.model;

import com.jichuangsi.school.courseservice.constant.Result;

public class AnswerForStudent {
    String answerId;
    String studentId;
    String studentName;
    String answerForObjective;
    String picForSubjective;
    String stubForSubjective;

    Result result;
    double subjectiveScore;

    public String getAnswerId() {
        return answerId;
    }

    public void setAnswerId(String answerId) {
        this.answerId = answerId;
    }

    public String getAnswerForObjective() {
        return answerForObjective;
    }

    public void setAnswerForObjective(String answerForObjective) {
        this.answerForObjective = answerForObjective;
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

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public Result getResult() {
        return result;
    }

    public void setResult(Result result) {
        this.result = result;
    }

    public double getSubjectiveScore() {
        return subjectiveScore;
    }

    public void setSubjectiveScore(double subjectiveScore) {
        this.subjectiveScore = subjectiveScore;
    }
}
