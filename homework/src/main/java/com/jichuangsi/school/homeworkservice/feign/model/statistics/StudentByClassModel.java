package com.jichuangsi.school.homeworkservice.feign.model.statistics;

public class StudentByClassModel {
    private String studentId;
    private String studentAccount;
    private String studentName;

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public void setStudentAccount(String studentAccount) {
        this.studentAccount = studentAccount;
    }

    public String getStudentId() {
        return studentId;
    }

    public String getStudentAccount() {
        return studentAccount;
    }
}
