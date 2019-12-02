package com.jichuangsi.school.examservice.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Document(collection = "paper_dimension")
public class PaperDimension {
    @Id
    private String id;

    private String year;

    private String grade;

    private String subject;

    private String type;

    private List<String> paper=new ArrayList<String>();

    private long CreateTime;

    public long getCreateTime() {
        return CreateTime;
    }

    public void setCreateTime(long createTime) {
        CreateTime = createTime;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public PaperDimension(){
    }

    public List<String> getPaper() {
        return paper;
    }

    public void setPaper(List<String> paper) {
        this.paper = paper;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }
}
