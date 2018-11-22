package com.jichuangsi.school.user.service;

import com.jichuangsi.school.user.exception.UserServiceException;
import com.jichuangsi.school.user.model.UserBaseInfo;
import com.jichuangsi.school.user.entity.UserInfo;
import com.jichuangsi.school.user.model.UserLoginModel;

public interface LoginService {
    UserLoginModel login(UserBaseInfo userBaseInfo) throws UserServiceException;

    boolean regist(UserInfo userInfo);

}
