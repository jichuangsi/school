package com.jichuangsi.school.parents.feign.model;

public class CourseForStudentId extends CourseForStudent {
    private String studentId;

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }
}
