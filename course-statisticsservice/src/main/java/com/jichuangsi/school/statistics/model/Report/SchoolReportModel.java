package com.jichuangsi.school.statistics.model.Report;

public class SchoolReportModel {

    private String calssId;
    private String subjectName;
    private int passingNumber;//及格人数
    private double passRate;//及格率
    private double averageScoreOfSingleSubject;//单科平均分
    private double totalAverageScore;//总平均分

    public double getAverageScoreOfSingleSubject() {
        return averageScoreOfSingleSubject;
    }

    public double getPassRate() {
        return passRate;
    }

    public double getTotalAverageScore() {
        return totalAverageScore;
    }

    public int getPassingNumber() {
        return passingNumber;
    }

    public String getCalssId() {
        return calssId;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public void setAverageScoreOfSingleSubject(double averageScoreOfSingleSubject) {
        this.averageScoreOfSingleSubject = averageScoreOfSingleSubject;
    }

    public void setCalssId(String calssId) {
        this.calssId = calssId;
    }

    public void setPassingNumber(int passingNumber) {
        this.passingNumber = passingNumber;
    }

    public void setPassRate(double passRate) {
        this.passRate = passRate;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    public void setTotalAverageScore(double totalAverageScore) {
        this.totalAverageScore = totalAverageScore;
    }
}
