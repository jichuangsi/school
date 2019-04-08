package com.jichuangsi.school.homeworkservice.feign.model;

public class QuestionResultModel {

    private String questionType;
    private Integer trueNum;
    private Integer wrongNum;

    public String getQuestionType() {
        return questionType;
    }

    public void setQuestionType(String questionType) {
        this.questionType = questionType;
    }

    public Integer getTrueNum() {
        return trueNum;
    }

    public void setTrueNum(Integer trueNum) {
        this.trueNum = trueNum;
    }

    public Integer getWrongNum() {
        return wrongNum;
    }

    public void setWrongNum(Integer wrongNum) {
        this.wrongNum = wrongNum;
    }
}
