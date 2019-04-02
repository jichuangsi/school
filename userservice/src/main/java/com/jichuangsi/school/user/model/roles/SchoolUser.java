package com.jichuangsi.school.user.model.roles;

import com.jichuangsi.school.user.model.System.Role;
import com.jichuangsi.school.user.model.basic.Phrase;
import com.jichuangsi.school.user.model.org.ClassModel;
import com.jichuangsi.school.user.model.school.GradeModel;
import com.jichuangsi.school.user.model.school.SchoolModel;

public abstract class SchoolUser extends Role{
    private SchoolModel school;
    private Phrase phrase;
    private GradeModel primaryGrade;
    private ClassModel primaryClass;

    public SchoolModel getSchool() {
        return school;
    }

    public void setSchool(SchoolModel school) {
        this.school = school;
    }

    public Phrase getPhrase() {
        return phrase;
    }

    public void setPhrase(Phrase phrase) {
        this.phrase = phrase;
    }

    public GradeModel getPrimaryGrade() {
        return primaryGrade;
    }

    public void setPrimaryGrade(GradeModel primaryGrade) {
        this.primaryGrade = primaryGrade;
    }

    public ClassModel getPrimaryClass() {
        return primaryClass;
    }

    public void setPrimaryClass(ClassModel primaryClass) {
        this.primaryClass = primaryClass;
    }
}
