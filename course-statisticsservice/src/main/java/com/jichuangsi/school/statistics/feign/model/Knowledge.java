package com.jichuangsi.school.statistics.feign.model;

public class Knowledge {

    private String knowledge;
    private String knowledgeId;
    private String capabilityId;
    private String capability;

    public Knowledge(){}

    public Knowledge(String knowledgeId, String knowledge, String capabilityId, String capability){
        this.knowledgeId = knowledgeId;
        this.knowledge = knowledge;
        this.capabilityId = capabilityId;
        this.capability = capability;
    }

    public String getKnowledge() {
        return knowledge;
    }

    public void setKnowledge(String knowledge) {
        this.knowledge = knowledge;
    }

    public String getKnowledgeId() {
        return knowledgeId;
    }

    public void setKnowledgeId(String knowledgeId) {
        this.knowledgeId = knowledgeId;
    }

    public String getCapabilityId() {
        return capabilityId;
    }

    public void setCapabilityId(String capabilityId) {
        this.capabilityId = capabilityId;
    }

    public String getCapability() {
        return capability;
    }

    public void setCapability(String capability) {
        this.capability = capability;
    }

}
