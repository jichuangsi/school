package com.jichuangsi.school.statistics.model.homework;

import com.jichuangsi.school.statistics.model.homework.constant.Result;

public class AnswerModelForStudent {
    private String answerId;
    private String studentId;
    private String studentName;
    private String answerForObjective;
    private String reviseForSubjective;
    private String stubForSubjective;

    private Result result;
    private double subjectiveScore;
    private long createTime;
    private long updateTime;
    private long reviseTime;

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

    public String getReviseForSubjective() {
        return reviseForSubjective;
    }

    public void setReviseForSubjective(String reviseForSubjective) {
        this.reviseForSubjective = reviseForSubjective;
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

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public long getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(long updateTime) {
        this.updateTime = updateTime;
    }

    public long getReviseTime() {
        return reviseTime;
    }

    public void setReviseTime(long reviseTime) {
        this.reviseTime = reviseTime;
    }
}
