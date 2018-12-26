package com.jichuangsi.school.questionsrepository.model.translate;

import java.util.ArrayList;
import java.util.List;

public class PicContent {

    private String picSub;
    private List<String> content = new ArrayList<String>();

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
