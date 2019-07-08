package com.jichuangsi.school.statistics.model.Report;

public class TestScoreModel {
    private String testId;
    private String testName;
    private Double maxScore;
    private Double minScore;
    private Double avg;
    private int actualNum;

    public String getTestId() {
        return testId;
    }

    public void setTestId(String testId) {
        this.testId = testId;
    }

    public void setMinScore(Double minScore) {
        this.minScore = minScore;
    }

    public void setMaxScore(Double maxScore) {
        this.maxScore = maxScore;
    }

    public Double getMinScore() {
        return minScore;
    }

    public Double getMaxScore() {
        return maxScore;
    }

    public void setAvg(Double avg) {
        this.avg = avg;
    }

    public Double getAvg() {
        return avg;
    }

    public String getTestName() {
        return testName;
    }

    public void setTestName(String testName) {
        this.testName = testName;
    }

    public void setActualNum(int actualNum) {
        this.actualNum = actualNum;
    }

    public int getActualNum() {
        return actualNum;
    }

    public TestScoreModel(String testId,String testName, Double maxScore, Double minScore, Double avg,int actualNum){
       this.testId=testId;
        this.testName=testName;
        this.avg=avg;
        this.maxScore=maxScore;
        this.minScore=minScore;
        this.actualNum=actualNum;
    }

    public TestScoreModel(String testId,String testName){
        this.testId=testId;
        this.testName=testName;
    }
    public TestScoreModel(){

    }
}
