package com.jichuangsi.school.examservice.Model.Dimension;

import java.util.ArrayList;
import java.util.List;

public class TypeModel {

    public String id;
    public String type;
    public List<PaperModel> paper =new ArrayList<PaperModel>();


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<PaperModel> getPaper() {
        return paper;
    }

    public void setPaper(List<PaperModel> paper) {
        this.paper = paper;
    }

    public TypeModel(String id, String type, List<PaperModel> paper) {
        this.id = id;
        this.type = type;
        this.paper = paper;
    }
}
