package com.jichuangsi.school.statistics.model.capability;

public class Comprehend {
    private String name;
    private Double value;

    public void setValue(Double value) {
        this.value = value;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public Double getValue() {
        return value;
    }


    public Comprehend(Double value, String name){
        this.name=name;
        this.value=value;
    }

    @Override
    public String toString() {
        return "Comprehend:[value="+value+",name="+name+"]";
    }
}
