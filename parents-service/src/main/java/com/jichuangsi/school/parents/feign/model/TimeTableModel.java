package com.jichuangsi.school.parents.feign.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TimeTableModel {

    private String id;
    private String classId;
    private String className;
    Map<String,List<String>> map = new HashMap<String, List<String>>();
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

    public Map<String, List<String>> getMap() {
        return map;
    }

    public void setMap(Map<String, List<String>> map) {
        this.map = map;
    }

    public String getDelete() {
        return delete;
    }

    public void setDelete(String delete) {
        this.delete = delete;
    }
}
