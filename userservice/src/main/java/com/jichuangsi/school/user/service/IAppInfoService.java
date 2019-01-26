package com.jichuangsi.school.user.service;

import com.jichuangsi.school.user.exception.UserServiceException;
import com.jichuangsi.school.user.model.app.AppInfoModule;
import com.jichuangsi.school.user.model.app.AppInfoQueryModule;

public interface IAppInfoService {

    AppInfoModule fetchLastestAppInfo(AppInfoQueryModule appInfoQueryModule) throws UserServiceException;
}
