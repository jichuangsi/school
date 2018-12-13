package com.jichuangsi.school.courseservice.constant;

public class ResultCode extends com.jichuangsi.microservice.common.constant.ResultCode{
	public static final String COURSE_NOT_EXISTED = "课堂不存在";
	public static final String COURSE_NOTSTART = "课堂未开始";
	public static final String COURSE_PROGRESS = "课堂已开始";
	public static final String COURSE_FINISHED = "课堂已完结";
	public static final String QUESTIONS_TO_SAVE_IS_EMPTY = "保存的问题集为空";
	public static final String COURSE_QUERY_IS_EMPTY = "请输入课堂查询条件";
	public static final String QUESTION_NOT_EXISTED = "所指问题不存在";
	public static final String QUESTION_NOTSTART = "所指问题未发布";
	public static final String QUESTION_PROGRESS = "所指问题已发布";
	public static final String QUESTION_STATUS_EXCEPTION = "所指问题状态异常";
	public static final String FILE_UPLOAD_ERROR = "文件上传错误";
	public static final String FILE_NOT_EXISTED = "文件不存在";
	public static final String FILE_DOWNLOAD_ERROR = "文件下载错误";
	public static final String FILE_REMOVE_ERROR = "文件删除错误";
	public static final String QUESTION_COMPLETE = "所指问题已完结";
	public static final String COURSE_ALREADY_EXISTED="课程已经存在";
	public static final String COURSE_DELETE_FAIL="课程删除失败";
	public static final String COURSE_ALREADY_BEGIN="课程已经开始";
	public static final String COURSE_FAIL_SAVE = "保存课程失败";
	public static final String QUESTION_FAIL_SAVE = "保存试题失败";
	public static final String TEACHER_ANSWER_NOT_EXISTED = "所指老师作答不存在";

}
