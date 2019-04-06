package com.jichuangsi.school.user.service;

import com.jichuangsi.microservice.common.model.UserInfoForToken;
import com.jichuangsi.school.user.exception.BackUserException;
import com.jichuangsi.school.user.model.backstage.BackRoleModel;

public interface IBackRoleService {

    void insertRole(UserInfoForToken userInfo, BackRoleModel model) throws BackUserException;

    void deleteRole(UserInfoForToken userInfo, String roleId) throws BackUserException;
}
