package com.jichuangsi.school.parents.model;

import com.jichuangsi.school.parents.model.file.ParentFile;

public class GrowthModel {

    private String id;
    private String title;
    private long createdTime;
    private String studentId;
    private String studentName;
    private ParentFile parentFile;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public long getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(long createdTime) {
        this.createdTime = createdTime;
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

    public ParentFile getParentFile() {
        return parentFile;
    }

    public void setParentFile(ParentFile parentFile) {
        this.parentFile = parentFile;
    }
}
