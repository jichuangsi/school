package com.jichuangsi.school.user.model.System;

import java.util.ArrayList;
import java.util.List;

public class Role {

    private List<String> roleIds = new ArrayList<String>();
    private String roleName;
    private List<Privilege> privileges = new ArrayList<Privilege>();

    public List<String> getRoleIds() {
        return roleIds;
    }

    public void setRoleIds(List<String> roleIds) {
        this.roleIds = roleIds;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public List<Privilege> getPrivileges() {
        return privileges;
    }

    public void setPrivileges(List<Privilege> privileges) {
        this.privileges = privileges;
    }
}
