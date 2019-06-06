package com.jichuangsi.school.homeworkservice.model.transfer;

public class TransferStudent {

    private String studentId;
    private String studentAccount;
    private String studentName;
    private long completedTime;
    private double totalScore;

    public TransferStudent(){}

    public TransferStudent(String studentId, String studentAccount, String studentName){
        this.studentId = studentId;
        this.studentAccount = studentAccount;
        this.studentName = studentName;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public String getStudentAccount() {
        return studentAccount;
    }

    public void setStudentAccount(String studentAccount) {
        this.studentAccount = studentAccount;
    }

    public long getCompletedTime() {
        return completedTime;
    }

    public void setCompletedTime(long completedTime) {
        this.completedTime = completedTime;
    }

    public double getTotalScore() {
        return totalScore;
    }

    public void setTotalScore(double totalScore) {
        this.totalScore = totalScore;
    }
}