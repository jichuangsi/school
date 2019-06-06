package com.jichuangsi.school.statistics.model.capability;

public class Apply {
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
    }public Apply(){}

    public Apply(Double value, String name){
        this.value=value;
        this.name=name;
    }

    @Override
    public String toString() {
        return "Apply:[value="+value+",name="+name+"]";
    }

}
