/**
 * 学生接收题目通知
 */
package com.jichuangsi.school.classinteraction.websocket.model;

/**
 * @author huangjiajun
 *
 */
public abstract class AbstractQustionNotifyForStudentModel {

	public static final String WS_TYPE_QUESTION_CLOSE = "QuestionClose";
	public static final String WS_TYPE_QUESTION_PUBLISH = "QuestionForPublish";
	public static final String WS_TYPE_QUESTION_ANSWER_SHARE = "AnswerShare";
	public static final String WS_TYPE_RACE_QUESTION = "RACE";

	protected String courseId;
	protected String questionId;
	protected String wsType;

	public String getCourseId() {
		return courseId;
	}

	public void setCourseId(String courseId) {
		this.courseId = courseId;
	}

	public String getQuestionId() {
		return questionId;
	}

	public void setQuestionId(String questionId) {
		this.questionId = questionId;
	}

	public String getWsType() {
		return wsType;
	}

	protected void setWsType(String wsType) {
		this.wsType = wsType;
	}

}
