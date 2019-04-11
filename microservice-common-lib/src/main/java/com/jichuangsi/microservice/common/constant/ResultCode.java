package com.jichuangsi.microservice.common.constant;

public class ResultCode {
	public static final String SUCESS = "0010";//成功
	public static final String PARAM_MISS = "0020";//缺少参数
	public static final String PARAM_ERR = "0021";//参数不正确
	public static final String TOKEN_MISS = "0030";//缺少token
	public static final String TOKEN_CHECK_ERR = "0031";//token校验异常
	public static final String SYS_ERROR = "0050";//系统内部异常
	public static final String SYS_BUSY = "0051";//熔断
	
	public static final String SUCESS_MSG = "成功";
	public static final String PARAM_MISS_MSG = "缺少参数";
	public static final String PARAM_ERR_MSG = "参数不正确";
	public static final String TOKEN_MISS_MSG = "缺少token";
	public static final String TOKEN_CHECK_ERR_MSG = "token校验异常";
	public static final String SYS_ERROR_MSG = "系统繁忙";
	public static final String SYS_BUSY_MSG = "系统繁忙";

	public static final String SELECT_NULL_MSG = "查无此信息";
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

	public static final String HEADMASTER_EXIST_MSG = "已有班主任，请先离任";
}
