package com.jichuangsi.school.questionsrepository.model.common;

public class SendCodePic {

    private String code;
    private String teacherId;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(String teacherId) {
        this.teacherId = teacherId;
    }

    public String getOneCode(){
        return teacherId+code;
    }
}
