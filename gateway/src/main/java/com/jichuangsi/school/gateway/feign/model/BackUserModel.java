package com.jichuangsi.school.gateway.feign.model;

import javax.validation.constraints.Pattern;

public class BackUserModel {

    private String id;
    @Pattern(regexp = "^[a-zA-Z0-9_-]{4,16}$",message = "账号必须是4到16位（字母，数字，下划线，减号）")
    private String account;
    private String userName;
    @Pattern(regexp = "^((?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{6,12})$",message = "密码必须为6-12位数字与字母混合")
    private String pwd;
    private String roleId;
    private String roleName;
    private String schoolId;
    private String schoolName;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public String getSchoolId() {
        return schoolId;
    }

    public void setSchoolId(String schoolId) {
        this.schoolId = schoolId;
    }

    public String getSchoolName() {
        return schoolName;
    }

    public void setSchoolName(String schoolName) {
        this.schoolName = schoolName;
    }

}
