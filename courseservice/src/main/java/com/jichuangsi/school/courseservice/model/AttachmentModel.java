package com.jichuangsi.school.courseservice.model;

public class AttachmentModel {
    private String name;
    private String contentType;
    private String sub;
    private String publishFlag;

    public AttachmentModel(){}

    public AttachmentModel(String name, String sub, String contentType){
        this.name = name;
        this.sub = sub;
        this.contentType = contentType;
    }

    public AttachmentModel(String name, String sub, String contentType, String publishFlag){
        this.name = name;
        this.sub = sub;
        this.contentType = contentType;
        this.publishFlag = publishFlag;
    }

    public String getPublishFlag() {
        return publishFlag;
    }

    public void setPublishFlag(String publishFlag) {
        this.publishFlag = publishFlag;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSub() {
        return sub;
    }

    public void setSub(String sub) {
        this.sub = sub;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }
}
