package com.jichuangsi.school.testservice.constant;

public class ResultCode extends com.jichuangsi.microservice.common.constant.ResultCode{
	public static final String TEST_NOT_EXISTED = "测验不存在";
	public static final String TEST_NOTSTART = "测验未发布";
	public static final String TEST_PROGRESS = "测验已发布";
	public static final String TEST_FINISHED = "测验已完结";
	public static final String QUESTIONS_TO_SAVE_IS_EMPTY = "保存的题目集为空";
	public static final String TEST_QUERY_IS_EMPTY = "请输入测验查询条件";
	public static final String QUESTION_NOT_EXISTED = "所指问题不存在";
	public static final String QUESTION_NOTSTART = "所指问题未发布";
	public static final String QUESTION_PROGRESS = "所指问题已发布";
	public static final String QUESTION_STATUS_EXCEPTION = "所指问题状态异常";
	public static final String FILE_UPLOAD_ERROR = "文件上传错误";
	public static final String FILE_NOT_EXISTED = "文件不存在";
	public static final String FILE_DOWNLOAD_ERROR = "文件下载错误";
	public static final String FILE_REMOVE_ERROR = "文件删除错误";
	public static final String QUESTION_COMPLETE = "所指问题已完结";
	public static final String TEST_ALREADY_EXISTED="测验已经存在";
	public static final String TEST_DELETE_FAIL="测验删除失败";
	public static final String TEST_ALREADY_BEGIN="测验已经发布";
	public static final String TEST_STATUS_ERROR="测验更新状态失败";
	public static final String TEST_FAIL_SAVE = "保存测验失败";
	public static final String QUESTION_FAIL_SAVE = "保存试题失败";
	public static final String TEACHER_ANSWER_NOT_EXISTED = "所指老师作答不存在";
	public static final String TEACHER_INFO_NOT_EXISTED = "老师信息获取错误";
	public static final String STUDENT_INFO_NOT_EXISTED = "学生信息获取错误";
	public static final String STUDENT_ADD_FAVOR_QUESTION_FAIL = "学生收藏题目失败";
	public static final String STUDENT_REMOVE_FAVOR_QUESTION_FAIL = "学生移除收藏题目失败";
	public static final String AI_PUSH_QUESTION_FAIL = "没有关联题目";
	public static final String SETTLE_TEST_WITH_PROGRESS = "试卷正在作答，请中止作答后再结算！";
}
