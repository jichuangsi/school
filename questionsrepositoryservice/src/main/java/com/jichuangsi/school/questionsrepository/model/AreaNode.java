package com.jichuangsi.school.questionsrepository.model;

import java.io.Serializable;

public class AreaNode implements Serializable {

    private String id;
    private String area;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }
}
