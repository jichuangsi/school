package com.jichuangsi.school.testservice.model.statistics;

import java.util.ArrayList;
import java.util.List;

public class HomeworkKnoledge {
    private String homeWorkName;
    private List<KnowledgePoints> knowledgePoints=new ArrayList<KnowledgePoints>();

    public List<KnowledgePoints> getKnowledgePoints() {
        return knowledgePoints;
    }

    public String getHomeWorkName() {
        return homeWorkName;
    }

    public void setHomeWorkName(String homeWorkName) {
        this.homeWorkName = homeWorkName;
    }

    public void setKnowledgePoints(List<KnowledgePoints> knowledgePoints) {
        this.knowledgePoints = knowledgePoints;
    }

    public HomeworkKnoledge(){}

    public HomeworkKnoledge(String homeWorkName,List<KnowledgePoints> knowledgePoints){
        this.homeWorkName=homeWorkName;
        this.knowledgePoints=knowledgePoints;
    }

}
