package com.jichuangsi.school.questionsrepository.model;

import java.io.Serializable;

public class QuestionTypeNode implements Serializable {

    private String id;
    private String subjectId;
    private String pharseId;
    private String typeName;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(String subjectId) {
        this.subjectId = subjectId;
    }

    public String getPharseId() {
        return pharseId;
    }

    public void setPharseId(String pharseId) {
        this.pharseId = pharseId;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }
}
