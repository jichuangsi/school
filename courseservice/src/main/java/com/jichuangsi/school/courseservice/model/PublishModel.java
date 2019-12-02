package com.jichuangsi.school.courseservice.model;

import java.util.ArrayList;
import java.util.List;

public class PublishModel {
    private List<String> student=new ArrayList<String>();

    private String courseId;
    private String questionId;

    public List<String> getStudent() {
        return student;
    }

    public void setStudent(List<String> student) {
        this.student = student;
    }

    public String getCourseId() {
        return courseId;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }

    public String getQuestionId() {
        return questionId;
    }

    public void setQuestionId(String questionId) {
        this.questionId = questionId;
    }
}
