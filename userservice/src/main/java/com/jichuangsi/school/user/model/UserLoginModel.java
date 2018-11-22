package com.jichuangsi.school.user.model;

public class UserLoginModel {
    private String accessToken;
    private UserBaseInfo userBaseInfo;
    public UserLoginModel(String accessToken, UserBaseInfo userBaseInfo) {
        this.accessToken = accessToken;
        this.userBaseInfo = userBaseInfo;
    }

    public static UserLoginModel successToLogin(String accessToken, UserBaseInfo userBaseInfo){
        return new UserLoginModel(accessToken,userBaseInfo);

    }
    public static UserLoginModel failToLogin(String meg, UserBaseInfo userBaseInfo){
        return new UserLoginModel(meg,userBaseInfo );

    }
    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public UserBaseInfo getUserBaseInfo() {
        return userBaseInfo;
    }

    public void setUserBaseInfo(UserBaseInfo userBaseInfo) {
        this.userBaseInfo = userBaseInfo;
    }

}
