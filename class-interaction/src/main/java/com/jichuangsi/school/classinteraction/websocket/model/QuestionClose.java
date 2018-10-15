/**
 * 题目终止作答信息
 */
package com.jichuangsi.school.classinteraction.websocket.model;

/**
 * @author huangjiajun
 *
 */
public class QuestionClose {
	private String courseId;
	private String questionId;
	private String wsType = "QuestionClose";

	public final String getCourseId() {
		return courseId;
	}

	public final void setCourseId(String courseId) {
		this.courseId = courseId;
	}

	public final String getQuestionId() {
		return questionId;
	}

	public final void setQuestionId(String questionId) {
		this.questionId = questionId;
	}

	public final String getWsType() {
		return wsType;
	}

	public final void setWsType(String wsType) {
		this.wsType = wsType;
	}

}
