package com.jichuangsi.school.user.model.file;

import java.util.UUID;

public class UserFile {

    private String name = UUID.randomUUID().toString();
    private String contentType;
    private String originalName;
    private byte[] content;

    public UserFile() {
    }

    public UserFile(String contentType, String originalName, byte[] content) {
        this.contentType = contentType;
        this.originalName = originalName;
        this.content = content;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public String getOriginalName() {
        return originalName;
    }

    public void setOriginalName(String originalName) {
        this.originalName = originalName;
    }

    public byte[] getContent() {
        return content;
    }

    public void setContent(byte[] content) {
        this.content = content;
    }

    public String getSubName(){return name + originalName.substring(originalName.lastIndexOf("."));}
}
