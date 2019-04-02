package com.jichuangsi.school.courseservice.entity;

public class Attachment {

    private String name;
    private String sub;
    private String contentType;
    private String publishStatus = "0";

    public String getPublishStatus() {
        return publishStatus;
    }

    public void setPublishStatus(String publishStatus) {
        this.publishStatus = publishStatus;
    }

    public Attachment(){}

    public Attachment(String name, String sub, String contentType){
        this.name = name;
        this.sub = sub;
        this.contentType = contentType;
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
