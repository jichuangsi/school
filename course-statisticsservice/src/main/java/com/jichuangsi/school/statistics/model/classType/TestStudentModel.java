package com.jichuangsi.school.statistics.model.classType;

import com.jichuangsi.school.statistics.model.capability.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TestStudentModel {
    private String StudentId;
    private String StudentName;
    Capability[] knowledges=new Capability[5];//总认知占比


    CapabilityTrueOrFalse[] capabilityTrueOrFalses=null;//每个认知对错占比

    //Map<String,CapabilityTrueOrFalse>
    Map<String,CapabilityTrueOrFalse> studentKnowledges=new HashMap<String,CapabilityTrueOrFalse>();


    public Map<String, CapabilityTrueOrFalse>getStudentKnowledges() {
        return studentKnowledges;
    }

    public void setStudentKnowledges(Map<String, CapabilityTrueOrFalse> studentKnowledges) {
        this.studentKnowledges = studentKnowledges;
    }

    public CapabilityTrueOrFalse[] getCapabilityTrueOrFalses() {
        return capabilityTrueOrFalses;
    }

    public void setCapabilityTrueOrFalses(CapabilityTrueOrFalse[] capabilityTrueOrFalses) {
        this.capabilityTrueOrFalses = capabilityTrueOrFalses;
    }
    public void setKnowledges(Capability[] knowledges) {
        this.knowledges = knowledges;
    }

    public Capability[] getKnowledges() {
        return knowledges;
    }
    public String getStudentId() {
        return StudentId;
    }
    public String getStudentName() {
        return StudentName;
    }
    public void setStudentId(String studentId) {
        StudentId = studentId;
    }

    public void setStudentName(String studentName) {
        StudentName = studentName;
    }

}
