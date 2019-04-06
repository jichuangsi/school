package cn.com.jichuangsi.school.backstage.util;

import cn.com.jichuangsi.school.backstage.entity.BackRoleInfo;
import cn.com.jichuangsi.school.backstage.model.BackRoleModel;

public final class MappingModel2EntityConverter {

    private MappingModel2EntityConverter(){}

    public static final BackRoleInfo CONVERTERFROMBACKROLEMODEL(BackRoleModel model){
        BackRoleInfo backRoleInfo = new BackRoleInfo();
        backRoleInfo.setId(model.getRoleId());
        backRoleInfo.setRoleName(model.getRoleName());
        return backRoleInfo;
    }
}
