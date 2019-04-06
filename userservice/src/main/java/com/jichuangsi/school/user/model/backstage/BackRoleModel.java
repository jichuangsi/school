package com.jichuangsi.school.user.model.backstage;

import com.jichuangsi.school.user.model.backstage.orz.PromisedModel;

import java.util.ArrayList;
import java.util.List;

public class BackRoleModel {

    private String roleId;
    private String roleName;
    private List<PromisedModel> promisedModels = new ArrayList<PromisedModel>();

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

    public List<PromisedModel> getPromisedModels() {
        return promisedModels;
    }

    public void setPromisedModels(List<PromisedModel> promisedModels) {
        this.promisedModels = promisedModels;
    }
}
