package com.jichuangsi.school.statistics.model.capability;

public class CapabilityTrueOrFalse {
    Memory[] memory=new Memory[2];
    Comprehend[] comprehend=new Comprehend[2];
    Analy[] analy=new Analy[2];
    Apply[] apply=new Apply[2];
    Synthesize[]synthesize=new Synthesize[2];
    Other[] others=new Other[2];

    public Other[] getOthers() {
        return others;
    }

    public void setOthers(Other[] others) {
        this.others = others;
    }

    public void setSynthesize(Synthesize[] synthesize) {
        this.synthesize = synthesize;
    }

    public void setComprehend(Comprehend[] comprehend) {
        this.comprehend = comprehend;
    }

    public void setAnaly(Analy[] analy) {
        this.analy = analy;
    }

    public Comprehend[] getComprehend() {
        return comprehend;
    }

   public void setApply(Apply[] apply) {
        this.apply = apply;
    }

    public Synthesize[] getSynthesize() {
        return synthesize;
    }

    public Apply[] getApply() {
        return apply;
    }

    public Analy[] getAnaly() {
        return analy;
    }

    public void setMemory(Memory[] memory) {
        this.memory = memory;
    }

    public Memory[] getMemory() {
        return memory;
    }



    public CapabilityTrueOrFalse(Memory[] memory,Apply[] apply,
                                 Analy[] analy,Comprehend[] comprehend,
                                 Synthesize[] synthesize,Other[]others){
        this.analy=analy;
        this.apply=apply;
        this.comprehend=comprehend;
        this.memory=memory;
        this.synthesize=synthesize;
        this.others=others;
    }


}
