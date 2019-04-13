package com.jichuangsi.school.user.entity.org;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Document(collection = "school_class")
public class ClassInfo {

    @Id
    private String id;
    private String name;
    private long createTime;
    private long updateTime;
    private String deleteFlag = "0";
    private String creatorId;
    private String creatorName;
    private String updateId;
    private String updateName;
    List<SubjectTeacherInfo> teacherInfos = new ArrayList<SubjectTeacherInfo>();
    private String headMasterId;
    private String headMasterName;

    public String getHeadMasterId() {
        return headMasterId;
    }

    public void setHeadMasterId(String headMasterId) {
        this.headMasterId = headMasterId;
    }

    public String getHeadMasterName() {
        return headMasterName;
    }

    public void setHeadMasterName(String headMasterName) {
        this.headMasterName = headMasterName;
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

    public String getUpdateId() {
        return updateId;
    }

    public void setUpdateId(String updateId) {
        this.updateId = updateId;
    }

    public String getUpdateName() {
        return updateName;
    }

    public void setUpdateName(String updateName) {
        this.updateName = updateName;
    }

    public String getDeleteFlag() {
        return deleteFlag;
    }

    public void setDeleteFlag(String deleteFlag) {
        this.deleteFlag = deleteFlag;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public long getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(long updateTime) {
        this.updateTime = updateTime;
    }

    public List<SubjectTeacherInfo> getTeacherInfos() {
        return teacherInfos;
    }

    public void setTeacherInfos(List<SubjectTeacherInfo> teacherInfos) {
        this.teacherInfos = teacherInfos;
    }
}
