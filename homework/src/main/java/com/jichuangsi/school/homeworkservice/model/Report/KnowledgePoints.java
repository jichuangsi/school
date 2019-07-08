package com.jichuangsi.school.homeworkservice.model.Report;

public class KnowledgePoints {
    private String name;
    private Integer rate;
    private Double rateC;
    private Double rateE;
    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public Integer getRate() {
        return rate;
    }

    public void setRate(Integer rate) {
        this.rate = rate;
    }

    public Double getRateC() {
        return rateC;
    }

    public Double getRateE() {
        return rateE;
    }

    public void setRateC(Double rateC) {
        this.rateC = rateC;
    }

    public void setRateE(Double rateE) {
        this.rateE = rateE;
    }

    public KnowledgePoints(){}
    public KnowledgePoints(String name,Integer rate,Double rateC,Double rateE){
        this.name=name;
        this.rate=rate;
        this.rateC=rateC;
        this.rateE=rateE;
    }
}
