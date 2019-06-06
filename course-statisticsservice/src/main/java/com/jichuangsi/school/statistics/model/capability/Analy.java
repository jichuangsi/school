package com.jichuangsi.school.statistics.model.capability;

public class Analy {
    private String name;
    private Double value;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setValue(Double value) {
        this.value = value;
    }

    public Double getValue() {
        return value;
    }public Analy(){}

    public Analy(Double value, String name){
        this.value=value;
        this.name=name;
    }

    @Override
    public String toString() {
        return "Analy:[value="+value+",name="+name+"]";
    }

}
