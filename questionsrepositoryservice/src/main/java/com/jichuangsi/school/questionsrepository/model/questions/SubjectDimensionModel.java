package com.jichuangsi.school.questionsrepository.model.questions;

import java.util.ArrayList;
import java.util.List;

public class SubjectDimensionModel {

    private String subject;

    private List<TypeMOdel> type=new ArrayList<TypeMOdel>();

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public List<TypeMOdel> getType() {
        return type;
    }

    public void setType(List<TypeMOdel> type) {
        this.type = type;
    }
}
