package com.jichuangsi.school.user.service;

import com.jichuangsi.microservice.common.model.UserInfoForToken;
import com.jichuangsi.school.user.exception.BackUserException;
import com.jichuangsi.school.user.model.backstage.BackRoleModel;
import com.jichuangsi.school.user.model.backstage.orz.PromisedModel;

import java.util.List;

public interface IBackRoleService {

    void insertRole(UserInfoForToken userInfo, BackRoleModel model) throws BackUserException;

    void deleteRole(UserInfoForToken userInfo, String roleId) throws BackUserException;

    void bindRole(UserInfoForToken userInfo, String roleId, String userId) throws BackUserException;

    void insertPromised(UserInfoForToken userInfo, PromisedModel model) throws BackUserException;

    List<BackRoleModel> getRoles(UserInfoForToken userInfo) throws BackUserException;

    void bindPromised(UserInfoForToken userInfo, String roleId, List<PromisedModel> models) throws BackUserException;

    List<PromisedModel> getPromised(UserInfoForToken userInfo) throws BackUserException;

    void  deletePromised(UserInfoForToken userInfo , String promisedId) throws BackUserException;
}
