package com.jichuangsi.school.gateway.feign.model;
import java.util.ArrayList;
import java.util.List;

public class User {
    private String userId;
    private String userPwd;
    private String userAccount;
    private String userName;
    private List<SchoolRole> roles = new ArrayList<SchoolRole>();
    private long createTime;
    private long updateTime;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserPwd() {
        return userPwd;
    }

    public void setUserPwd(String userPwd) {
        this.userPwd = userPwd;
    }

    public String getUserAccount() {
        return userAccount;
    }

    public void setUserAccount(String userAccount) {
        this.userAccount = userAccount;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public List<SchoolRole> getRoles() {
        return roles;
    }

    public void setRoles(List<SchoolRole> roles) {
        this.roles = roles;
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
}
