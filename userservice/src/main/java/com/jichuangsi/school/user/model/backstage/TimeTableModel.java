package com.jichuangsi.school.user.model.backstage;

import java.util.ArrayList;
import java.util.List;

public class TimeTableModel {

    private String id;
    private String classId;
    private String className;
    List<List<String>> dataInfo = new ArrayList<List<String>>();
    private String delete = "0";

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getClassId() {
        return classId;
    }

    public void setClassId(String classId) {
        this.classId = classId;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public List<List<String>> getDataInfo() {
        return dataInfo;
    }

    public void setDataInfo(List<List<String>> dataInfo) {
        this.dataInfo = dataInfo;
    }

    public String getDelete() {
        return delete;
    }

    public void setDelete(String delete) {
        this.delete = delete;
    }
}
