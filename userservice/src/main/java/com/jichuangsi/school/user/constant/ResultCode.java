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

    public static final String SUBJECT_IS_EXIST = "科目已存在";
    public static final String PHRASE_IS_EXIST = "年段已存在";
    public static final String GRADE_IS_EXIST = "年级已存在";
    public static final String SCHOOL_IS_EXIST = "学校已存在";
    public static final String USER_IS_EXIST = "账号已存在";

    public static final String EXCEL_IMPORT_MSG = "excel导入失败";

    public static final String ROLE_NOT_RIGHT = "角色不符合";
    public static final String ROLE_EXIST_MSG = "角色已存在";

    public static final String USER_SELECT_NULL_MSG = "查无此用户信息";
    public static final String CLASS_SELECT_NULL_MSG = "查无此班级信息";
    public static final String SCHOOL_SELECT_NULL_MSG = "查无此学校信息";
    public static final String GRADE_SELECT_NULL_MSG = "查无此年级信息";
    public static final String PHRASE_SELECT_NULL_MSG = "查无此年段信息";

    public static final String HEADMASTER_EXIST_MSG = "已有班主任，请先离任";
    public static final String TEACHER_INNER_CLASS_MES = "已执教该班";
    public static final String CLASS_SUBJECT_MES = "该班已有该科目老师，请先离任上一个老师";

    public static final String TEACHER_SUBJECT_MSG = "该老师不教该科目";
    public static final String SUBJECT_ISEXIST_MSG = "该班存在该科目";

    public static final String SUBJECT_ISNOT_EXIST = "科目不存在";
    public static final String SUBJECT_TEACHER_EXIST = "该科目老师尚未离任";

    public static final String FILE_DELETE_ERR ="文件删除失败";

    public static final String FILE_CHANGE_ERR = "文件转换失败";
    public static final String CLASS_TABLE_NULL_MSG = "该班没有课程表";
    public static final String CLASS_TABLE_EXIST_MSG = "该班已有课程表";
    public static final String PHARSE_NAME_ERR = "年段命名不规范";

    public static final String PHRASE_TEACH_EXIST = "该老师已有年级执教任务，如需重置,请先离任";
}
