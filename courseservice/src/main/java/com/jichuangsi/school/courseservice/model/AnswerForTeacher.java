package com.jichuangsi.school.courseservice.model;

public class AnswerForTeacher {
    private String answerId;
    private String teacherId;
    private String teacherName;
    private String picForSubjective;
    private String stubForSubjective;
    private boolean isShare;
    private long createTime;
    private long updateTime;
    private long shareTime;

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

    public boolean isShare() {
        return isShare;
    }

    public void setShare(boolean share) {
        isShare = share;
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

    public long getShareTime() {
        return shareTime;
    }

    public void setShareTime(long shareTime) {
        this.shareTime = shareTime;
    }
}
