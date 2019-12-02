package com.jichuangsi.school.questionsrepository.model.questions;

public class QuestionModel {
    private String tname;//特征名称

    private String type;//特征类型

    private String tId;//特征id

    public String getTname() {
        return tname;
    }

    public void setTname(String tname) {
        this.tname = tname;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String gettId() {
        return tId;
    }

    public void settId(String tId) {
        this.tId = tId;
    }
}
