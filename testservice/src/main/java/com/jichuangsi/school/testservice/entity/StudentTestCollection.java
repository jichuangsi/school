package com.jichuangsi.school.testservice.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Document(collection = "school_student_test_collection")
public class StudentTestCollection {

    @Id
    private String id;
    private String studentId;
    private String studentAccount;
    private String studentName;
    private List<TestSummary> tests = new ArrayList<TestSummary>();

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public List<TestSummary> getTests() {
        return tests;
    }

    public void setTests(List<TestSummary> tests) {
        this.tests = tests;
    }

    public String getStudentAccount() {
        return studentAccount;
    }

    public void setStudentAccount(String studentAccount) {
        this.studentAccount = studentAccount;
    }
}
