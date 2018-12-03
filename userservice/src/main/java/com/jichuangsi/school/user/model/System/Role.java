package com.jichuangsi.school.user.model.System;

import java.util.ArrayList;
import java.util.List;

public class Role {

    private String roleId;
    private String roleName;
    private List<Privilege> privileges = new ArrayList<Privilege>();

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

    public List<Privilege> getPrivileges() {
        return privileges;
    }

    public void setPrivileges(List<Privilege> privileges) {
        this.privileges = privileges;
    }
}
