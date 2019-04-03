package com.jichuangsi.school.courseservice.model.feign.classType;

import java.util.ArrayList;
import java.util.List;

public class ClassStatisticsModel {

    private String classId;
    private String className;
    private List<KnowledgeResultModel> knowledgeResultModels = new ArrayList<KnowledgeResultModel>();
    private Integer wrongQuestionNum;
    private Integer weakQuestionNum;
    List<String> courseIds = new ArrayList<String>();
    List<String> wrongQuestionIds = new ArrayList<String>();
    List<String> monthQuestionIds = new ArrayList<String>();

    public List<String> getMonthQuestionIds() {
        return monthQuestionIds;
    }

    public void setMonthQuestionIds(List<String> monthQuestionIds) {
        this.monthQuestionIds = monthQuestionIds;
    }

    public List<String> getWrongQuestionIds() {
        return wrongQuestionIds;
    }

    public void setWrongQuestionIds(List<String> wrongQuestionIds) {
        this.wrongQuestionIds = wrongQuestionIds;
    }

    public List<String> getCourseIds() {
        return courseIds;
    }

    public void setCourseIds(List<String> courseIds) {
        this.courseIds = courseIds;
    }

    public String getClassId() {
        return classId;
    }

    public void setClassId(String classId) {
        this.classId = classId;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public List<KnowledgeResultModel> getKnowledgeResultModels() {
        return knowledgeResultModels;
    }

    public void setKnowledgeResultModels(List<KnowledgeResultModel> knowledgeResultModels) {
        this.knowledgeResultModels = knowledgeResultModels;
    }

    public Integer getWrongQuestionNum() {
        return wrongQuestionNum;
    }

    public void setWrongQuestionNum(Integer wrongQuestionNum) {
        this.wrongQuestionNum = wrongQuestionNum;
    }

    public Integer getWeakQuestionNum() {
        return weakQuestionNum;
    }

    public void setWeakQuestionNum(Integer weakQuestionNum) {
        this.weakQuestionNum = weakQuestionNum;
    }
}
