package cn.com.jichuangsi.school.backstage.service;

import cn.com.jichuangsi.school.backstage.exception.BackUserException;
import cn.com.jichuangsi.school.backstage.model.BackRoleModel;
import com.jichuangsi.microservice.common.model.UserInfoForToken;

public interface IBackRoleService {

    void insertRole(UserInfoForToken userInfo , BackRoleModel model) throws BackUserException;

    void deleteRole(UserInfoForToken userInfo, String roleId) throws BackUserException;
}
