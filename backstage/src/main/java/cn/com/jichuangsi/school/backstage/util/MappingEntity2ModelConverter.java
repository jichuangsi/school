package cn.com.jichuangsi.school.backstage.util;

import cn.com.jichuangsi.school.backstage.entity.BackUserInfo;
import com.jichuangsi.microservice.common.model.UserInfoForToken;

public final class MappingEntity2ModelConverter {

    private MappingEntity2ModelConverter(){}

    public final static UserInfoForToken CONVERTERFROMBACKUSERINFO(BackUserInfo userInfo){
        UserInfoForToken userInfoForToken = new UserInfoForToken();
        userInfoForToken.setRoleName(userInfo.getRoleName());
        userInfoForToken.setSchoolId(userInfo.getSchoolId());
        userInfoForToken.setUserId(userInfo.getId());
        userInfoForToken.setUserName(userInfo.getUserName());
        userInfoForToken.setUserNum(userInfo.getAccount());
        return userInfoForToken;
    }
}
