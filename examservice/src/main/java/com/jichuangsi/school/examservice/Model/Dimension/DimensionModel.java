package com.jichuangsi.school.examservice.Model.Dimension;


import java.util.ArrayList;
import java.util.List;

public class DimensionModel {

    public String  year;

    public List<GradesModel> grades=new ArrayList<GradesModel>();

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public List<GradesModel> getGrades() {
        return grades;
    }

    public void setGrades(List<GradesModel> grades) {
        this.grades = grades;
    }

    public DimensionModel(String year, List<GradesModel> grades) {
        this.year = year;
        this.grades = grades;
    }

    public DimensionModel() {
    }
}
