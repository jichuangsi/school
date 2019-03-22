package com.jichuangsi.school.statistics.model.result;

public class StudentResultModel {

    private String knowledgeName;
    private Double knowledgeRate;
    private Double resultRate;
    private Double classResultRate;
    private Double gradeResultRate;


    public Double getGradeResultRate() {
        return gradeResultRate;
    }

    public void setGradeResultRate(Double gradeResultRate) {
        this.gradeResultRate = gradeResultRate;
    }

    public String getKnowledgeName() {
        return knowledgeName;
    }

    public void setKnowledgeName(String knowledgeName) {
        this.knowledgeName = knowledgeName;
    }

    public Double getKnowledgeRate() {
        return knowledgeRate;
    }

    public void setKnowledgeRate(Double knowledgeRate) {
        this.knowledgeRate = knowledgeRate;
    }

    public Double getResultRate() {
        return resultRate;
    }

    public void setResultRate(Double resultRate) {
        this.resultRate = resultRate;
    }

    public Double getClassResultRate() {
        return classResultRate;
    }

    public void setClassResultRate(Double classResultRate) {
        this.classResultRate = classResultRate;
    }
}