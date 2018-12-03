package com.jichuangsi.school.user.model.roles;

import com.jichuangsi.school.user.model.basic.Subject;
import com.jichuangsi.school.user.model.org.Grade;
import com.jichuangsi.school.user.model.org.Class;

import java.util.ArrayList;
import java.util.List;

public class Teacher extends SchoolUser {
    private Subject primarySubject;
    private List<Subject> secondarySubjects = new ArrayList<Subject>();
    private List<Grade> secondaryGrades = new ArrayList<Grade>();
    private List<Class> secondaryClass = new ArrayList<Class>();

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

    public List<Grade> getSecondaryGrades() {
        return secondaryGrades;
    }

    public void setSecondaryGrades(List<Grade> secondaryGrades) {
        this.secondaryGrades = secondaryGrades;
    }

    public List<Class> getSecondaryClass() {
        return secondaryClass;
    }

    public void setSecondaryClass(List<Class> secondaryClass) {
        this.secondaryClass = secondaryClass;
    }

    public boolean equals(Object obj) {
        if (obj instanceof Teacher) {
            return true;
        }
        return super.equals(obj);
    }
}
