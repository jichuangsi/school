package com.jichuangsi.school.statistics.model.result;

import java.util.ArrayList;
import java.util.List;

public class TeacherResultModel {

    private Integer studentNum;
    private List<StudentResultModel> studentResultModels = new ArrayList<StudentResultModel>();

    public Integer getStudentNum() {
        return studentNum;
    }

    public void setStudentNum(Integer studentNum) {
        this.studentNum = studentNum;
    }

    public List<StudentResultModel> getStudentResultModels() {
        return studentResultModels;
    }

    public void setStudentResultModels(List<StudentResultModel> studentResultModels) {
        this.studentResultModels = studentResultModels;
    }
}
