package com.jichuangsi.school.questionsrepository.feign.model;

public class SechQuesionModel {

    private String knowledgeId;//知识点id
    private String qtypeId;//题型
    private String paperType;//试卷类型
    private String diff;//难度
    private String year;//年份
    private String area;//年份
    private String page;//分页
    private String pageSize;//页面数量
   private String pharseId;
    private String gradeId;//年级
    private String subjectId;//科目
    private String sort;


    public String getPharseId() {
        return pharseId;
    }

    public void setPharseId(String pharseId) {
        this.pharseId = pharseId;
    }

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

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getPage() {
        return page;
    }

    public void setPage(String page) {
        this.page = page;
    }

    public String getPageSize() {
        return pageSize;
    }

    public void setPageSize(String pageSize) {
        this.pageSize = pageSize;
    }

    public String getGradeId() {
        return gradeId;
    }

    public void setGradeId(String gradeId) {
        this.gradeId = gradeId;
    }

    public String getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(String subjectId) {
        this.subjectId = subjectId;
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }
}
