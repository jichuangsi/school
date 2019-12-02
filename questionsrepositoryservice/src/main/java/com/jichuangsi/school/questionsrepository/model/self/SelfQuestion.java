package com.jichuangsi.school.questionsrepository.model.self;

import com.jichuangsi.school.questionsrepository.model.common.Question;

import java.util.ArrayList;
import java.util.List;

public class SelfQuestion extends Question {
    private  String code;
    List<String> tid=new ArrayList<String>();

    public List<String> getTid() {
        return tid;
    }

    public void setTid(List<String> tid) {
        this.tid = tid;
    }
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
