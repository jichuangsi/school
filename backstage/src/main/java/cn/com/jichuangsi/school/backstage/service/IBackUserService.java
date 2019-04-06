package cn.com.jichuangsi.school.backstage.service;

import cn.com.jichuangsi.school.backstage.exception.BackUserException;
import cn.com.jichuangsi.school.backstage.feign.model.SchoolModel;
import cn.com.jichuangsi.school.backstage.model.BackUserModel;
import cn.com.jichuangsi.school.backstage.model.UpdatePwdModel;
import com.jichuangsi.microservice.common.model.UserInfoForToken;

import java.util.List;

public interface IBackUserService {

    void registBackUser(BackUserModel model) throws BackUserException;

    String loginBackUser(BackUserModel model) throws BackUserException;

    void deleteBackUser(UserInfoForToken userInfo,BackUserModel model) throws BackUserException;

    void updateBackUserPwd(UserInfoForToken userInfo, UpdatePwdModel model) throws BackUserException;

    List<SchoolModel> getBackSchools() throws BackUserException;
}


