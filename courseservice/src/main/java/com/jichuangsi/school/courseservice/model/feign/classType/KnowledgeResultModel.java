package com.jichuangsi.school.courseservice.model.feign.classType;

public class KnowledgeResultModel {

    private String knowledgeName;
    private Integer trueNum;
    private Integer wrongNum;

    public String getKnowledgeName() {
        return knowledgeName;
    }

    public void setKnowledgeName(String knowledgeName) {
        this.knowledgeName = knowledgeName;
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
