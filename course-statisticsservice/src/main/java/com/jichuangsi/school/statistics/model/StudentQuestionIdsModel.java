package com.jichuangsi.school.statistics.model;

import java.util.ArrayList;
import java.util.List;

public class StudentQuestionIdsModel {
    private String id;
    private String studentId;
    private String studentName;
    private String result;
    //private List<String> questionIds=new ArrayList<String>();
    private String questionId;

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public void setQuestionId(String questionId) {
        this.questionId = questionId;
    }

    public String getQuestionId() {
        return questionId;
    }

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

    public void setResult(String result) {
        this.result = result;
    }

    public String getResult() {
        return result;
    }

}
