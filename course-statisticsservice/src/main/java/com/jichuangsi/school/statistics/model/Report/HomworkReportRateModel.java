package com.jichuangsi.school.statistics.model.Report;

import java.util.ArrayList;
import java.util.List;

public class HomworkReportRateModel {
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
}
