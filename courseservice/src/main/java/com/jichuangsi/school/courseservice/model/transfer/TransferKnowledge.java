package com.jichuangsi.school.courseservice.model.transfer;

public class TransferKnowledge {

    private String knowledgeId;
    private String knowledge;

    public TransferKnowledge(){}

    public TransferKnowledge(String knowledgeId, String knowledge){
        this.knowledgeId = knowledgeId;
        this.knowledge = knowledge;
    }

    public String getKnowledgeId() {
        return knowledgeId;
    }

    public void setKnowledgeId(String knowledgeId) {
        this.knowledgeId = knowledgeId;
    }

    public String getKnowledge() {
        return knowledge;
    }

    public void setKnowledge(String knowledge) {
        this.knowledge = knowledge;
    }
}
