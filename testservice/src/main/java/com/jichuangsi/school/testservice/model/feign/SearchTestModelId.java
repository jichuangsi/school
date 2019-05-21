package com.jichuangsi.school.testservice.model.feign;

import com.jichuangsi.school.testservice.model.SearchTestModel;

public class SearchTestModelId extends SearchTestModel{
    private String studentId;

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }
}
