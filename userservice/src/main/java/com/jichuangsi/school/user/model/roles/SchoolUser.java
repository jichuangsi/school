package com.jichuangsi.school.user.model.roles;

import com.jichuangsi.school.user.model.System.Role;
import com.jichuangsi.school.user.model.basic.Phrase;
import com.jichuangsi.school.user.model.org.Grade;
import com.jichuangsi.school.user.model.org.School;
import com.jichuangsi.school.user.model.org.Class;

public abstract class SchoolUser extends Role{
    private School school;
    private Phrase phrase;
    private Grade primaryGrade;
    private Class primaryClass;

    public School getSchool() {
        return school;
    }

    public void setSchool(School school) {
        this.school = school;
    }

    public Phrase getPhrase() {
        return phrase;
    }

    public void setPhrase(Phrase phrase) {
        this.phrase = phrase;
    }

    public Grade getPrimaryGrade() {
        return primaryGrade;
    }

    public void setPrimaryGrade(Grade primaryGrade) {
        this.primaryGrade = primaryGrade;
    }

    public Class getPrimaryClass() {
        return primaryClass;
    }

    public void setPrimaryClass(Class primaryClass) {
        this.primaryClass = primaryClass;
    }
}
