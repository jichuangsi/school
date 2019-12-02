package com.jichuangsi.school.examservice.Model.Dimension;

import java.util.ArrayList;
import java.util.List;

public class GradesModel {

    private String grade;

    List<SubjectModel> subject=new ArrayList<SubjectModel>();

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public List<SubjectModel> getSubject() {
        return subject;
    }

    public void setSubject(List<SubjectModel> subject) {
        this.subject = subject;
    }

    public GradesModel(String grade, List<SubjectModel> subject) {
        this.grade = grade;
        this.subject = subject;
    }
    public GradesModel() {

    }
}
