package com.jichuangsi.school.user.entity;

import java.util.ArrayList;
import java.util.List;

public abstract class RoleInfo {

    private String roleName;
    private List<String> privilegeIds = new ArrayList<String>();

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }
}
