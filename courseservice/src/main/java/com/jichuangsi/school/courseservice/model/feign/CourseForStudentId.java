package com.jichuangsi.school.courseservice.model.feign;

import com.jichuangsi.school.courseservice.model.CourseForStudent;

public class CourseForStudentId extends CourseForStudent {
    private String studentId;

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }
}
