package com.jichuangsi.school.examservice.Model;

import java.util.List;

public class ExamModel {
    private String examId;
    private String examName;
    private List<QuestionModel> questionModels;
    private long createTime;
    private long updateTime;

    private Integer pageSize;
    private Integer pageIndex;
    private String examSecondName;

    public String getExamSecondName() {
        return examSecondName;
    }

    public void setExamSecondName(String examSecondName) {
        this.examSecondName = examSecondName;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public Integer getPageIndex() {
        return pageIndex;
    }

    public void setPageIndex(Integer pageIndex) {
        this.pageIndex = pageIndex;
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

    public String getExamId() {
        return examId;
    }

    public void setExamId(String examId) {
        this.examId = examId;
    }

    public String getExamName() {
        return examName;
    }

    public void setExamName(String examName) {
        this.examName = examName;
    }

    public List<QuestionModel> getQuestionModels() {
        return questionModels;
    }

    public void setQuestionModels(List<QuestionModel> questionModels) {
        this.questionModels = questionModels;
    }
}
