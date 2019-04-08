package com.jichuangsi.school.user.service;

import com.jichuangsi.microservice.common.model.UserInfoForToken;
import com.jichuangsi.school.user.exception.BackUserException;
import com.jichuangsi.school.user.model.backstage.BackUserModel;
import com.jichuangsi.school.user.model.backstage.UpdatePwdModel;
import com.jichuangsi.school.user.model.school.SchoolModel;

import java.util.List;

public interface IBackUserService {

    String registBackUser(BackUserModel model) throws BackUserException;

    String loginBackUser(BackUserModel model) throws BackUserException;

    void deleteBackUser(UserInfoForToken userInfo, BackUserModel model) throws BackUserException;

    void updateBackUserPwd(UserInfoForToken userInfo, UpdatePwdModel model) throws BackUserException;

    List<SchoolModel> getBackSchools() throws BackUserException;

    List<BackUserModel> getSchoolUserInfo(UserInfoForToken userInfo) throws BackUserException;

    BackUserModel getUserInfoAndPromised(UserInfoForToken userInfo) throws BackUserException;

    void insertSuperMan() throws BackUserException;
}


