package com.jichuangsi.school.statistics.model.classType;

import com.jichuangsi.school.statistics.entity.common.KnowledgeEntity;
import org.springframework.util.AutoPopulatingList;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StudentKnowledgeModel {
    private String studentId;
    private String studentName;
    private List<KnowledgeResultModel> knowledgeResultModels = new ArrayList<KnowledgeResultModel>();
    private Map<String,Double> capability=new HashMap<String,Double>();

    public void setCapability(Map<String, Double> capability) {
        this.capability = capability;
    }

    public Map<String, Double> getCapability() {
        return capability;
    }

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
