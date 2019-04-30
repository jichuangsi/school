package com.jichuangsi.school.user.commons;

import com.jichuangsi.microservice.common.constant.ResultCode;

public class MyResultCode extends ResultCode {
    public static final String USER_UNEXITS = "用户不存在";
    public static final String USER_PASSWORD_CHECK = "密码不正确";
    public static final String COMPARE = "账户已经存在,请重新输入";
    public static final String USER_EXISTED = "用户已存在";
    public static final String UPDATE_FAILED = "更新失败,用户不存在,请确定用户Id";
    public static final String DELETE_FAILED = "删除失败";
    public static final String DELETE_SUCCESS = "删除成功";
    public static final String RESTORE_FAILED = "恢复失败,请确定Flag是否正确";
    public static final String DELETE_FAILED_FLAG = "删除失败,请确定Flag是否正确";
    public static final String ID_REQUIED = "需要主键Id";
    public static final String ID_Mistake = "不忽略大小写,主键错误";
    public static final String FILE_SAVE_ERROR = "文件保存失败";
}
