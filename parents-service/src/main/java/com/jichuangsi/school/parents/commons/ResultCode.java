package com.jichuangsi.school.parents.commons;

public class ResultCode extends com.jichuangsi.microservice.common.constant.ResultCode {

    public static final String STUDNET_NOTBIND_MSG = "尚未绑定学生";
    public static final String PARENT_NOTFOUND_MSG = "未找到该家长";
    public static final String STUDNET_BING_ERR = "此学生并非该家长绑定";

    public static final String FILE_CHANGE_ERR = "转换文件错误";
    public static final String FILE_DELETE_ERR = "文件删除错误";

    public static final String GROWTH_NOTEXIST_ERR = "成长记录不存在";

    public static final String ACCOUNT_EXIST_MSG = "账号已绑定";
    public static final String ACCOUNT_ISEXIST_MSG = "该账号已注册";

    public static final String ACCOUNT_NOTBIND_MSG = "尚未绑定账号";

    public static final String PWD_VALIDATE_ERR = "密码验证失败";

    public static final String ACCOUNT_REGIST_ERR = "账号尚未注册或已注销";

    public static final String HTTP_IO_MSG = "http请求失败";

    public static final String PHP_CORRECT_CODE = "1";
}
