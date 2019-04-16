package com.jichuangsi.school.user.entity.backstage;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Document(collection = "class_time_table")
public class TimeTableInfo {

    @Id
    private String id;
    private String classId;
    private String className;
    private List<String> monday = new ArrayList<String>();
    private List<String> tuesday = new ArrayList<String>();
    private List<String> wednesday = new ArrayList<String>();
    private List<String> thursday = new ArrayList<String>();
    private List<String> friday = new ArrayList<String>();
    private List<String> classBegin = new ArrayList<String>();
    private String sub;
    private String delete = "0";
    private String creatorId;
    private String creatorName;
    private long createdTime = new Date().getTime();
    private String updatedId;
    private String updatedName;
    private long updatedTime = new Date().getTime();

    public List<String> getClassBegin() {
        return classBegin;
    }

    public void setClassBegin(List<String> classBegin) {
        this.classBegin = classBegin;
    }

    public String getSub() {
        return sub;
    }

    public void setSub(String sub) {
        this.sub = sub;
    }

    public String getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(String creatorId) {
        this.creatorId = creatorId;
    }

    public String getCreatorName() {
        return creatorName;
    }

    public void setCreatorName(String creatorName) {
        this.creatorName = creatorName;
    }

    public long getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(long createdTime) {
        this.createdTime = createdTime;
    }

    public String getUpdatedId() {
        return updatedId;
    }

    public void setUpdatedId(String updatedId) {
        this.updatedId = updatedId;
    }

    public String getUpdatedName() {
        return updatedName;
    }

    public void setUpdatedName(String updatedName) {
        this.updatedName = updatedName;
    }

    public long getUpdatedTime() {
        return updatedTime;
    }

    public void setUpdatedTime(long updatedTime) {
        this.updatedTime = updatedTime;
    }

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

    public List<String> getMonday() {
        return monday;
    }

    public void setMonday(List<String> monday) {
        this.monday = monday;
    }

    public List<String> getTuesday() {
        return tuesday;
    }

    public void setTuesday(List<String> tuesday) {
        this.tuesday = tuesday;
    }

    public List<String> getWednesday() {
        return wednesday;
    }

    public void setWednesday(List<String> wednesday) {
        this.wednesday = wednesday;
    }

    public List<String> getThursday() {
        return thursday;
    }

    public void setThursday(List<String> thursday) {
        this.thursday = thursday;
    }

    public List<String> getFriday() {
        return friday;
    }

    public void setFriday(List<String> friday) {
        this.friday = friday;
    }

    public String getDelete() {
        return delete;
    }

    public void setDelete(String delete) {
        this.delete = delete;
    }
}
