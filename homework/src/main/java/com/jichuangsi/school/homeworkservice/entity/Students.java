package com.jichuangsi.school.homeworkservice.entity;

public class Students {

    private String studentId;

    private String studentName;

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

    public Students(String studentId, String studentName) {
        this.studentId = studentId;
        this.studentName = studentName;
    }

    public Students() {
    }
}
