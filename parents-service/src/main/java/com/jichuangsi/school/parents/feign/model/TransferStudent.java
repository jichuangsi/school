package com.jichuangsi.school.parents.feign.model;

public class TransferStudent {

    private String studentId;
    private String studentAccount;
    private String studentName;
    private String signFlag = "0";
    private int commendFlag = 0;

    public TransferStudent(){}

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

    public String getSignFlag() {
        return signFlag;
    }

    public void setSignFlag(String signFlag) {
        this.signFlag = signFlag;
    }

    public int getCommendFlag() {
        return commendFlag;
    }

    public void setCommendFlag(int commendFlag) {
        this.commendFlag = commendFlag;
    }
}

