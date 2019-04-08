package com.jichuangsi.school.homeworkservice.feign.model;

import java.util.ArrayList;
import java.util.List;

public class QuestionRateModel {

    private List<String> questionIds = new ArrayList<String>();
    private String studentId;

    public List<String> getQuestionIds() {
        return questionIds;
    }

    public void setQuestionIds(List<String> questionIds) {
        this.questionIds = questionIds;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }
}
