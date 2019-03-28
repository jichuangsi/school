package com.jichuangsi.school.homeworkservice.feign.model;

public class HomeWorkRateModel {

    private String questionId;
    private String homeworkId;

    public String getQuestionId() {
        return questionId;
    }

    public void setQuestionId(String questionId) {
        this.questionId = questionId;
    }

    public String getHomeworkId() {
        return homeworkId;
    }

    public void setHomeworkId(String homeworkId) {
        this.homeworkId = homeworkId;
    }
}
