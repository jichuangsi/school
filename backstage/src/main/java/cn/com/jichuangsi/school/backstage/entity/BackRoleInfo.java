package cn.com.jichuangsi.school.backstage.entity;

import cn.com.jichuangsi.school.backstage.entity.orz.PromisedInfo;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Document(collection = "back_role_info")
public class BackRoleInfo {
    @Id
    private String id;
    private String roleName;
    private List<PromisedInfo> promisedInfos = new ArrayList<PromisedInfo>();
    private String deleteFlag = "0" ;
    private String creatorId;
    private String creatorName;
    private long createdTime = new Date().getTime();
    private long updatedTime = new Date().getTime();
    private String updatedId;
    private String updatedName;

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

    public long getUpdatedTime() {
        return updatedTime;
    }

    public void setUpdatedTime(long updatedTime) {
        this.updatedTime = updatedTime;
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public List<PromisedInfo> getPromisedInfos() {
        return promisedInfos;
    }

    public void setPromisedInfos(List<PromisedInfo> promisedInfos) {
        this.promisedInfos = promisedInfos;
    }

    public String getDeleteFlag() {
        return deleteFlag;
    }

    public void setDeleteFlag(String deleteFlag) {
        this.deleteFlag = deleteFlag;
    }
}
