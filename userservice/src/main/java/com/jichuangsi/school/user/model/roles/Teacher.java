package com.jichuangsi.school.user.model.roles;

import com.jichuangsi.school.user.model.basic.Subject;
import com.jichuangsi.school.user.model.org.ClassModel;
import com.jichuangsi.school.user.model.school.GradeModel;

import java.util.ArrayList;
import java.util.List;

public class Teacher extends SchoolUser {
    private Subject primarySubject;
    private List<Subject> secondarySubjects = new ArrayList<Subject>();
    private List<GradeModel> secondaryGrades = new ArrayList<GradeModel>();
    private List<ClassModel> secondaryClass = new ArrayList<ClassModel>();

    public Subject getPrimarySubject() {
        return primarySubject;
    }

    public void setPrimarySubject(Subject primarySubject) {
        this.primarySubject = primarySubject;
    }

    public List<Subject> getSecondarySubjects() {
        return secondarySubjects;
    }

    public void setSecondarySubjects(List<Subject> secondarySubjects) {
        this.secondarySubjects = secondarySubjects;
    }

    public List<GradeModel> getSecondaryGrades() {
        return secondaryGrades;
    }

    public void setSecondaryGrades(List<GradeModel> secondaryGrades) {
        this.secondaryGrades = secondaryGrades;
    }

    public List<ClassModel> getSecondaryClass() {
        return secondaryClass;
    }

    public void setSecondaryClass(List<ClassModel> secondaryClass) {
        this.secondaryClass = secondaryClass;
    }

    public boolean equals(Object obj) {
        if (obj instanceof Teacher) {
            return true;
        }
        return super.equals(obj);
    }
}
