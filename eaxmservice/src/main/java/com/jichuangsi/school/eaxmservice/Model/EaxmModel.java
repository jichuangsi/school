package com.jichuangsi.school.eaxmservice.Model;

import java.util.List;

public class EaxmModel {
    private String eaxmId;
    private String eaxmName;
    private List<QuestionModel> questionModels;
    private long createTime;
    private long updateTime;

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

    public String getEaxmId() {
        return eaxmId;
    }

    public void setEaxmId(String eaxmId) {
        this.eaxmId = eaxmId;
    }

    public String getEaxmName() {
        return eaxmName;
    }

    public void setEaxmName(String eaxmName) {
        this.eaxmName = eaxmName;
    }

    public List<QuestionModel> getQuestionModels() {
        return questionModels;
    }

    public void setQuestionModels(List<QuestionModel> questionModels) {
        this.questionModels = questionModels;
    }
}
