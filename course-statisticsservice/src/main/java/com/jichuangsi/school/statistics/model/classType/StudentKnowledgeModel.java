package com.jichuangsi.school.statistics.model.classType;

import java.util.ArrayList;
import java.util.List;

public class StudentKnowledgeModel {

    private String studentId;
    private String studentName;
    private List<KnowledgeResultModel> knowledgeResultModels = new ArrayList<KnowledgeResultModel>();

    public List<KnowledgeResultModel> getKnowledgeResultModels() {
        return knowledgeResultModels;
    }

    public void setKnowledgeResultModels(List<KnowledgeResultModel> knowledgeResultModels) {
        this.knowledgeResultModels = knowledgeResultModels;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }
}
