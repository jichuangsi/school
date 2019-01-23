package com.jichuangsi.school.questionsrepository.model.common;

public class SendCodePic {

    private String code;
    private String sub;
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

    public String getSub() {
        return sub;
    }

    public void setSub(String sub) {
        this.sub = sub;
    }

    public String getOneCode(){
        return teacherId+code;
    }
}
