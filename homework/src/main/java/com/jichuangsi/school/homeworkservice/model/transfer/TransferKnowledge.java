package com.jichuangsi.school.homeworkservice.model.transfer;

import com.jichuangsi.school.homeworkservice.entity.Knowledge;

import java.util.ArrayList;
import java.util.List;

public class TransferKnowledge {

    private List<Knowledge> knowledges = new ArrayList<Knowledge>();

    public List<Knowledge> getKnowledges() {
        return knowledges;
    }

    public void setKnowledges(List<Knowledge> knowledges) {
        this.knowledges = knowledges;
    }
}
