package com.jichuangsi.school.questionsrepository.model;

public class QuestionQueryModel {

    private String knowledgeId;//知识点id
    private String qtypeId;//题型
    private String paperType;//试卷类型
    private String diff;//难度
    private String year;//年份
    private String page;//分页

    public String getKnowledgeId() {
        return knowledgeId;
    }

    public void setKnowledgeId(String knowledgeId) {
        this.knowledgeId = knowledgeId;
    }

    public String getQtypeId() {
        return qtypeId;
    }

    public void setQtypeId(String qtypeId) {
        this.qtypeId = qtypeId;
    }

    public String getPaperType() {
        return paperType;
    }

    public void setPaperType(String paperType) {
        this.paperType = paperType;
    }

    public String getDiff() {
        return diff;
    }

    public void setDiff(String diff) {
        this.diff = diff;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getPage() {
        return page;
    }

    public void setPage(String page) {
        this.page = page;
    }
}
