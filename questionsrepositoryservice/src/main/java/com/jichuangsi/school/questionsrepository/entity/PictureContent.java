package com.jichuangsi.school.questionsrepository.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Document(collection = "school_question_self_content")
public class PictureContent {

    @Id
    private String id;
    private String picSub;
    private List<String> content = new ArrayList<String>();

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPicSub() {
        return picSub;
    }

    public void setPicSub(String picSub) {
        this.picSub = picSub;
    }

    public List<String> getContent() {
        return content;
    }

    public void setContent(List<String> content) {
        this.content = content;
    }
}
