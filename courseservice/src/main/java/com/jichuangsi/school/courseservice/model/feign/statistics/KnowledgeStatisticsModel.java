package com.jichuangsi.school.courseservice.model.feign.statistics;

public class KnowledgeStatisticsModel {

    private String knowledgeName;
    private double knowledgeRate;
    private double studentRightRate;
    private double classRightAvgRate;

    public String getKnowledgeName() {
        return knowledgeName;
    }

    public void setKnowledgeName(String knowledgeName) {
        this.knowledgeName = knowledgeName;
    }

    public double getKnowledgeRate() {
        return knowledgeRate;
    }

    public void setKnowledgeRate(double knowledgeRate) {
        this.knowledgeRate = knowledgeRate;
    }

    public double getStudentRightRate() {
        return studentRightRate;
    }

    public void setStudentRightRate(double studentRightRate) {
        this.studentRightRate = studentRightRate;
    }

    public double getClassRightAvgRate() {
        return classRightAvgRate;
    }

    public void setClassRightAvgRate(double classRightAvgRate) {
        this.classRightAvgRate = classRightAvgRate;
    }
}
