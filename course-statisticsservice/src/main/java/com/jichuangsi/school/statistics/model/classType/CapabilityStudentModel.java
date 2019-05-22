package com.jichuangsi.school.statistics.model.classType;

import com.jichuangsi.school.statistics.model.capability.Capability;
import com.jichuangsi.school.statistics.model.capability.CapabilityTrueOrFalse;

import java.util.HashMap;
import java.util.Map;

public class CapabilityStudentModel {

    Capability[] knowledges=new Capability[5];//总认知占比


    CapabilityTrueOrFalse[] capabilityTrueOrFalses=null;//每个认知对错占比


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

}
