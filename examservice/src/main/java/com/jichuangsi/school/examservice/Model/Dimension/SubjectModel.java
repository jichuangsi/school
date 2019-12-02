package com.jichuangsi.school.examservice.Model.Dimension;

import java.util.ArrayList;
import java.util.List;

public class SubjectModel {

    public String subjiect;

    List<TypeModel> type=new ArrayList<TypeModel>();

    public String getSubjiect() {
        return subjiect;
    }

    public void setSubjiect(String subjiect) {
        this.subjiect = subjiect;
    }

    public List<TypeModel> getType() {
        return type;
    }

    public void setType(List<TypeModel> type) {
        this.type = type;
    }

    public SubjectModel(String subjiect, List<TypeModel> type) {
        this.subjiect = subjiect;
        this.type = type;
    }
    public SubjectModel() {

    }
}
