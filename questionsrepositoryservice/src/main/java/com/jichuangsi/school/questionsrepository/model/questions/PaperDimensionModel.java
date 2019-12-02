package com.jichuangsi.school.questionsrepository.model.questions;

import java.util.ArrayList;
import java.util.List;

public class PaperDimensionModel {

    private String id;

    private List<SubjectDimensionModel> year=new ArrayList<>();

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<SubjectDimensionModel> getYear() {
        return year;
    }

    public void setYear(List<SubjectDimensionModel> year) {
        this.year = year;
    }
}
