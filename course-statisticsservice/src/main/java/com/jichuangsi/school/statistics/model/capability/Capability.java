package com.jichuangsi.school.statistics.model.capability;

public class Capability {
    private String name;

    private double value;
    public Capability(){}
    public Capability(Double value,String name){
    this.value=value;
    this.name=name;
}

    public double getValue() {
        return value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setValue(double value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "Capability [value=" + value + ", name=" + name + "]";
    }
}
