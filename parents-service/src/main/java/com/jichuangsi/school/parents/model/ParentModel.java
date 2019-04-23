package com.jichuangsi.school.parents.model;

import javax.validation.constraints.Pattern;

public class ParentModel {

    private String userName;
    private String openId;
    @Pattern(regexp = "^((13[0-9])|(14[5,7])|(15[0-3,5-9])|(17[0,3,5-8])|(18[0-9])|166|198|199|(147))\\d{8}$",message = "手机号码不正确")
    private String phone;
    @Pattern(regexp = "^[a-zA-Z]([-_a-zA-Z0-9]{5,19})+$",message = "6至20位，以字母开头，字母，数字，减号，下划线")
    private String account;
    @Pattern(regexp = "^((?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{6,12})$",message = "密码必须为6-12位数字与字母混合")
    private String pwd;

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
