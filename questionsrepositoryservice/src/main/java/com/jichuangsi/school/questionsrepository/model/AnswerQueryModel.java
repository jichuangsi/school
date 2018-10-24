package com.jichuangsi.school.questionsrepository.model;

import java.util.ArrayList;
import java.util.List;

public class AnswerQueryModel {
    private List<String> qids = new ArrayList<String>();

    public List<String> getQids() {
        return qids;
    }

    public void setQids(List<String> qids) {
        this.qids = qids;
    }
}
