package com.jichuangsi.school.user.service;

import com.jichuangsi.school.user.entity.UserInfo;
import com.jichuangsi.school.user.exception.UserServiceException;
import com.jichuangsi.school.user.model.System.User;
import com.jichuangsi.school.user.model.UserLoginModel;

public interface LoginService {
    UserLoginModel login(User user) throws UserServiceException;

    UserInfo register(User user) throws UserServiceException;

}
