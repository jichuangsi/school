package com.jichuangsi.school.courseservice.model;

import java.util.UUID;

public class CourseFile {
    String name = UUID.randomUUID().toString();
    String originalName;
    String contentType;
    byte[] content;

    public CourseFile(){}

    public CourseFile(String originalName, String contentType, byte[] content){
        this.originalName = originalName;
        this.contentType = contentType;
        this.content = content;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public byte[] getContent() {
        return content;
    }

    public void setContent(byte[] content) {
        this.content = content;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public String getOriginalName() {
        return originalName;
    }

    public void setOriginalName(String originalName) {
        this.originalName = originalName;
    }

    public String getStoredName(){
        return name + originalName.substring(originalName.lastIndexOf("."));
    }
}