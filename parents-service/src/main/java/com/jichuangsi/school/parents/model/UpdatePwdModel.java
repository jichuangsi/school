package com.jichuangsi.school.parents.model;

import javax.validation.constraints.Pattern;

public class UpdatePwdModel {

    private String oldPwd;
    @Pattern(regexp = "^((?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{6,12})$",message = "密码必须为6-12位数字与字母混合")
    private String newPwd;

    public String getOldPwd() {
        return oldPwd;
    }

    public void setOldPwd(String oldPwd) {
        this.oldPwd = oldPwd;
    }

    public String getNewPwd() {
        return newPwd;
    }

    public void setNewPwd(String newPwd) {
        this.newPwd = newPwd;
    }
}
