package com.jichuangsi.school.statistics.model.Report;

public class StudentTestScoreModel {

    private String testName;
    private String subjectName;
    private String className;
    private Double avg;
    private Double maxScore;
    private Double minScore;
    private int actualNum;
    private int missNum;

    public void setClassName(String className) {
        this.className = className;
    }

    public String getClassName() {
        return className;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    public Double getAvg() {
        return avg;
    }

    public Double getMaxScore() {
        return maxScore;
    }

    public Double getMinScore() {
        return minScore;
    }

    public int getActualNum() {
        return actualNum;
    }

    public int getMissNum() {
        return missNum;
    }

    public String getTestName() {
        return testName;
    }

    public void setActualNum(int actualNum) {
        this.actualNum = actualNum;
    }

    public void setAvg(Double avg) {
        this.avg = avg;
    }

    public void setMaxScore(Double maxScore) {
        this.maxScore = maxScore;
    }

    public void setMinScore(Double minScore) {
        this.minScore = minScore;
    }

    public void setMissNum(int missNum) {
        this.missNum = missNum;
    }

    public void setTestName(String testName) {
        this.testName = testName;
    }

    public StudentTestScoreModel(){}

    public StudentTestScoreModel(String className,String subjectName,String testName,  Double avg,Double maxScore,
           Double minScore,int actualNum,int missNum){
        this.className=className;
        this.testName=testName;
        this.subjectName=subjectName;
        this.avg=avg;
        this.maxScore=maxScore;
        this.minScore=minScore;
        this.actualNum=actualNum;
        this.missNum=missNum;
    }
}
