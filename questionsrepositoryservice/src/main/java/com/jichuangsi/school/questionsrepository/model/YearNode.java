package com.jichuangsi.school.questionsrepository.model;

import java.io.Serializable;

public class YearNode implements Serializable {

    private String id;
    private String year;

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
}
