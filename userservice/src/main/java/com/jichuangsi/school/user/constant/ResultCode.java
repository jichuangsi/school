package com.jichuangsi.school.user.constant;

public class ResultCode extends com.jichuangsi.microservice.common.constant.ResultCode{

    public final static String ACCOUNT_ISEXIST_MSG = "账号已存在";
    public final static String ACCOUNT_NOTEXIST_MSG = "账号不存在，或者未被激活";

    public final static String ACCOUNT_DELETEPOWER_MSG = "无权利删除此账号";
    public final static String PWD_NOT_MSG = "密码错误";
    public final static String ROLE_ISEXIST_MSG = "角色已存在";

    public final static String USER_ISNOT_EXIST = "用户信息不存在";
    public final static String ROLE_ISNOT_EXIST = "角色信息不存在";

    public final static String ADMIN_DELETE_MSG = "不可删除管理员";
}
