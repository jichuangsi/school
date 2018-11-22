package com.jichuangsi.school.user.model;

public class UserBaseInfo {
    private String userId;
    private String Pwd;

    public UserBaseInfo() {
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPwd() {
        return Pwd;
    }

    public void setPwd(String pwd) {
        Pwd = pwd;
    }

    public UserBaseInfo(String userId, String pwd) {
        this.userId = userId;
        this.Pwd = pwd;
    }
}
