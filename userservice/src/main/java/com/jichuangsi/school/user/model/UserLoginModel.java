package com.jichuangsi.school.user.model;

import com.jichuangsi.school.user.model.System.User;

public class UserLoginModel {
    private String accessToken;
    private User user;

    public UserLoginModel(String accessToken, User user) {
        this.accessToken = accessToken;
        this.user = user;
    }

    public static UserLoginModel successToLogin(String accessToken, User user){
        return new UserLoginModel(accessToken, user);

    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

}
