package com.jichuangsi.school.testservice.model.statistics;

public class TestScoreModel {
    private String testName;
    private Double maxScore;
    private Double minScore;
    private int actualNum;
    private Double avg;


    public void setActualNum(int actualNum) {
        this.actualNum = actualNum;
    }

    public int getActualNum() {
        return actualNum;
    }

    public String getTestName() {
        return testName;
    }

    public void setTestName(String testName) {
        this.testName = testName;
    }

    public void setAvg(Double avg) {
        this.avg = avg;
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

    public void setMaxScore(Double maxScore) {
        this.maxScore = maxScore;
    }

    public void setMinScore(Double minScore) {
        this.minScore = minScore;
    }

    public TestScoreModel(String testName, Double maxScore, Double minScore, Double avg,int actualNum){
        this.testName=testName;
        this.avg=avg;
        this.maxScore=maxScore;
        this.minScore=minScore;
        this.actualNum=actualNum;
    }

    public TestScoreModel(){

    }

}
