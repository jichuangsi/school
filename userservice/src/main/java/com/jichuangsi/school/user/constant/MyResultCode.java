package com.jichuangsi.school.user.constant;

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
    public static final String DELETE_FAILED_FLAG = "删除失败,请确定用户已被删除";
    public static final String ID_REQUIED = "需要主键Id";
    public static final String ID_Mistake = "不忽略大小写,主键错误";
    public static final String USER_DELETE_FLAG = "1";
    public static final String TOKEN_ERROR = "生成令牌异常";
    public final static String PARAM_NOT_EXIST = "参数不存在";
    public final static String ROLE_NOT_MATCH = "角色不匹配";

    public final static String SCHOOL_GRADE_NOT_MATCH = "学校与年级不匹配";
    public final static String GRADE_CLASS_NOT_MATCH = "年级与班不匹配";
    public final static String GRADE_CLASS_NOT_SYNC = "年级与班同步失败";
    public final static String CLASS_FAIL2REMOVE = "班移除失败";
    public final static String UPDATE_PORTRAIT_ERROR = "更新用户头像失败";

}
