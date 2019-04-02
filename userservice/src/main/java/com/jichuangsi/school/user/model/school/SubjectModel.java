package com.jichuangsi.school.user.model.school;

public class SubjectModel {

    private String id;
    private String subjectName;

    public SubjectModel(){}

    public SubjectModel(String id,String subjectName){
        this.subjectName = subjectName;
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }
}
