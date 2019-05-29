package com.jichuangsi.school.statistics.model.capability;

public class Other {
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


    public Other(Double value, String name){
        this.name=name;
        this.value=value;
    }

    @Override
    public String toString() {
        return "Other:[value="+value+",name="+name+"]";
    }
}
