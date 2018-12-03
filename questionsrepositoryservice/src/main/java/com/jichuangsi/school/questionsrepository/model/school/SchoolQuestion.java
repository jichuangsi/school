package com.jichuangsi.school.questionsrepository.model.school;

import com.jichuangsi.school.questionsrepository.model.common.Question;

public class SchoolQuestion extends Question {

    private String schoolId;
    private String schoolName;

    public String getSchoolId() {
        return schoolId;
    }

    public void setSchoolId(String schoolId) {
        this.schoolId = schoolId;
    }

    public String getSchoolName() {
        return schoolName;
    }

    public void setSchoolName(String schoolName) {
        this.schoolName = schoolName;
    }
}
