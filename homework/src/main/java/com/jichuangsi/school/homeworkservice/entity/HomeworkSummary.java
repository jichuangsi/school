package com.jichuangsi.school.homeworkservice.entity;

public class HomeworkSummary {
    private String homeworkId;
    private String homeworkName;
    private long updateTime;
    private long completedTime;
    private double totalScore;

    public HomeworkSummary() {}

    public HomeworkSummary(String homeworkId, String homeworkName) {
        this.homeworkId = homeworkId;
        this.homeworkName = homeworkName;
    }

    public String getHomeworkId() {
        return homeworkId;
    }

    public void setHomeworkId(String homeworkId) {
        this.homeworkId = homeworkId;
    }

    public String getHomeworkName() {
        return homeworkName;
    }

    public void setHomeworkName(String homeworkName) {
        this.homeworkName = homeworkName;
    }

    public long getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(long updateTime) {
        this.updateTime = updateTime;
    }

    public long getCompletedTime() {
        return completedTime;
    }

    public void setCompletedTime(long completedTime) {
        this.completedTime = completedTime;
    }

    public double getTotalScore() {
        return totalScore;
    }

    public void setTotalScore(double totalScore) {
        this.totalScore = totalScore;
    }
}
