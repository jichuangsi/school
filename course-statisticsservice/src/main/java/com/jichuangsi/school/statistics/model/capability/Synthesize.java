package com.jichuangsi.school.statistics.model.capability;

public class Synthesize {
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


    public Synthesize(Double value, String name){
        this.name=name;
        this.value=value;
    }

    @Override
    public String toString() {
        return "Synthesize:[value="+value+",name="+name+"]";
    }
}
