package com.jichuangsi.school.courseservice.model;

public class Attachment {
    private String name;
    private String contentType;
    private String sub;

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
