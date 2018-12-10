package com.jichuangsi.school.questionsrepository.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class KnowledgeTreeNode implements Serializable {
    private String knowledgeName;
    private String kid;
    private List<KnowledgeTreeNode> data = new ArrayList<KnowledgeTreeNode>();

    public String getKnowledgeName() {
        return knowledgeName;
    }

    public void setKnowledgeName(String knowledgeName) {
        this.knowledgeName = knowledgeName;
    }

    public String getKid() {
        return kid;
    }

    public void setKid(String kid) {
        this.kid = kid;
    }

    public List<KnowledgeTreeNode> getData() {
        return data;
    }

    public void setData(List<KnowledgeTreeNode> data) {
        this.data = data;
    }
}
