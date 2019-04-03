package com.jichuangsi.school.courseservice.model.result;

public class QuestionStatisticsRateModel {

    private String questionId;
    private int trueNum = 0 ;
    private int wrongNum = 0;

    public String getQuestionId() {
        return questionId;
    }

    public void setQuestionId(String questionId) {
        this.questionId = questionId;
    }

    public int getTrueNum() {
        return trueNum;
    }

    public void setTrueNum(int trueNum) {
        this.trueNum = trueNum;
    }

    public int getWrongNum() {
        return wrongNum;
    }

    public void setWrongNum(int wrongNum) {
        this.wrongNum = wrongNum;
    }
}
