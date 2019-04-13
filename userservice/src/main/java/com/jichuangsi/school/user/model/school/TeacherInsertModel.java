package com.jichuangsi.school.user.model.school;

public class TeacherInsertModel {

    private String primaryClassId;
    private String secondaryClassId;

    private String subjectId;
    private String subjectName;

    public String getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(String subjectId) {
        this.subjectId = subjectId;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    public String getPrimaryClassId() {
        return primaryClassId;
    }

    public void setPrimaryClassId(String primaryClassId) {
        this.primaryClassId = primaryClassId;
    }

    public String getSecondaryClassId() {
        return secondaryClassId;
    }

    public void setSecondaryClassId(String secondaryClassId) {
        this.secondaryClassId = secondaryClassId;
    }
}
